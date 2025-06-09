package com.goodme.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goodme.common.exception.BusinessException;
import com.goodme.common.utils.ValidatorUtils;
import com.goodme.framework.security.JwtTokenProvider;
import com.goodme.service.dto.LoginRequest;
import com.goodme.service.dto.LoginResponse;
import com.goodme.service.dto.UserDTO;
import com.goodme.service.entity.User;
import com.goodme.service.mapper.UserMapper;
import com.goodme.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final Long SMS_CODE_EXPIRE = 5L; // 验证码有效期5分钟

    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据登录类型处理
        switch (request.getLoginType()) {
            case 1:
                // 密码登录
                return passwordLogin(request.getPhone(), request.getPassword());
            case 2:
                // 验证码登录
                return smsCodeLogin(request.getPhone(), request.getCode());
            case 3:
                // 微信登录
                return wxLogin(request.getWxCode());
            default:
                throw new BusinessException("不支持的登录类型");
        }
    }

    @Override
    public boolean sendSmsCode(String phone) {
        // 检查手机号是否有效
        if (!StringUtils.hasText(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }

        // 生成6位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        
        // 存储验证码到Redis，设置过期时间
        String key = SMS_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, code, SMS_CODE_EXPIRE, TimeUnit.MINUTES);
        
        log.info("发送短信验证码：手机号 {}，验证码 {}", phone, code);
        
        // 实际项目中调用短信发送服务
        // TODO: 对接短信服务商API发送短信
        
        return true;
    }

    @Override
    public boolean register(String phone, String password, String code) {
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        
        // 验证码校验
        String cachedCode = redisTemplate.opsForValue().get(SMS_CODE_PREFIX + phone);
        if (!StringUtils.hasText(cachedCode) || !cachedCode.equals(code)) {
            throw new BusinessException("验证码无效或已过期");
        }

        // 检查用户是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        if (count(queryWrapper) > 0) {
            throw new BusinessException("该手机号已注册");
        }

        // 创建新用户
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname("用户" + phone.substring(phone.length() - 4));
        user.setStatus(1);
        user.setPoints(0);
        user.setMemberLevelId(1L);
        user.setGender(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        boolean result = save(user);
        
        // 使用后删除验证码
        if (result) {
            redisTemplate.delete(SMS_CODE_PREFIX + phone);
        }
        
        return result;
    }

    @Override
    public UserDTO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        return convertToDTO(user);
    }

    @Override
    public boolean updateUserInfo(User user) {
        if (user == null || user.getId() == null) {
            throw new BusinessException("用户信息不完整");
        }
        
        // 只允许更新部分字段
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setNickname(user.getNickname());
        updateUser.setAvatar(user.getAvatar());
        updateUser.setGender(user.getGender());
        updateUser.setBirthday(user.getBirthday());
        updateUser.setUpdateTime(LocalDateTime.now());
        
        return updateById(updateUser);
    }

    @Override
    public LoginResponse wxLogin(String wxCode) {
        if (!StringUtils.hasText(wxCode)) {
            throw new BusinessException("微信授权码不能为空");
        }
        
        // TODO: 调用微信接口获取openid
        String openid = "mock_openid_" + System.currentTimeMillis();
        
        // 查询是否已关联用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        User user = getOne(queryWrapper);
        
        // 如果用户不存在，则创建新用户
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname("微信用户");
            user.setStatus(1);
            user.setPoints(0);
            user.setMemberLevelId(1L);
            user.setGender(0);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            save(user);
        }
        
        // 更新最后登录时间
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(LocalDateTime.now());
        updateById(updateUser);
        
        // 生成token
        String token = jwtTokenProvider.generateToken(user.getId());
        
        // 构建响应对象
        return LoginResponse.builder()
                .token(token)
                .expireIn(jwtTokenProvider.getExpirationInSeconds())
                .userInfo(convertToDTO(user))
                .build();
    }

    /**
     * 密码登录
     */
    private LoginResponse passwordLogin(String phone, String password) {
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = getOne(queryWrapper);
        
        // 验证用户是否存在
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 更新最后登录时间
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(LocalDateTime.now());
        updateById(updateUser);
        
        // 生成token
        String token = jwtTokenProvider.generateToken(user.getId());
        
        // 构建响应对象
        return LoginResponse.builder()
                .token(token)
                .expireIn(jwtTokenProvider.getExpirationInSeconds())
                .userInfo(convertToDTO(user))
                .build();
    }

    /**
     * 验证码登录
     */
    private LoginResponse smsCodeLogin(String phone, String code) {
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        
        // 验证验证码格式
        if (!ValidatorUtils.isSmsCode(code)) {
            throw new BusinessException("验证码格式不正确");
        }
        
        // 验证码校验
        String cachedCode = redisTemplate.opsForValue().get(SMS_CODE_PREFIX + phone);
        if (!StringUtils.hasText(cachedCode) || !cachedCode.equals(code)) {
            throw new BusinessException("验证码无效或已过期");
        }
        
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = getOne(queryWrapper);
        
        // 如果用户不存在，则自动注册
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setNickname("用户" + phone.substring(phone.length() - 4));
            user.setStatus(1);
            user.setPoints(0);
            user.setMemberLevelId(1L);
            user.setGender(0);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            save(user);
        } else if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 更新最后登录时间
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(LocalDateTime.now());
        updateById(updateUser);
        
        // 使用后删除验证码
        redisTemplate.delete(SMS_CODE_PREFIX + phone);
        
        // 生成token
        String token = jwtTokenProvider.generateToken(user.getId());
        
        // 构建响应对象
        return LoginResponse.builder()
                .token(token)
                .expireIn(jwtTokenProvider.getExpirationInSeconds())
                .userInfo(convertToDTO(user))
                .build();
    }

    /**
     * 转换User实体到UserDTO
     */
    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        
        // TODO: 填充会员等级名称
        userDTO.setMemberLevelName("普通会员");
        
        return userDTO;
    }
} 