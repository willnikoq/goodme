package com.goodme.framework.security;

import com.goodme.common.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT授权过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 如果是排除的路径，直接放行
        if (isExcludedPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 获取token
        String token = resolveToken(request);
        
        // 验证token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            try {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                
                // 检查token是否在黑名单中（用户登出）
                String blacklistKey = "token:blacklist:" + token;
                Boolean isBlacklisted = redisTemplate.hasKey(blacklistKey);
                if (Boolean.TRUE.equals(isBlacklisted)) {
                    chain.doFilter(request, response);
                    return;
                }
                
                // 将用户ID设置到请求属性中，方便后续使用
                request.setAttribute("userId", userId);
                
            } catch (Exception e) {
                log.error("JWT校验异常", e);
            }
        }
        
        chain.doFilter(request, response);
    }
    
    /**
     * 判断是否为排除的路径
     */
    private boolean isExcludedPath(String requestURI) {
        // 定义排除的路径
        List<String> excludedPaths = Arrays.asList(
                "/auth/**", 
                "/swagger-resources/**", 
                "/webjars/**", 
                "/v2/api-docs", 
                "/doc.html", 
                "/swagger-ui.html", 
                "/druid/**"
        );
        
        // 判断当前请求是否匹配排除的路径
        return excludedPaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }
    
    /**
     * 从请求中获取token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
} 