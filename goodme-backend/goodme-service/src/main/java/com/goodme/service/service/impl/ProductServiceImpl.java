package com.goodme.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goodme.common.exception.BusinessException;
import com.goodme.common.utils.RedisUtils;
import com.goodme.service.dto.*;
import com.goodme.service.entity.Product;
import com.goodme.service.entity.ProductSpec;
import com.goodme.service.entity.ProductSpecValue;
import com.goodme.service.mapper.ProductMapper;
import com.goodme.service.mapper.ProductSpecMapper;
import com.goodme.service.mapper.ProductSpecValueMapper;
import com.goodme.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final ProductSpecMapper productSpecMapper;
    private final ProductSpecValueMapper productSpecValueMapper;
    private final RedisUtils redisUtils;
    
    // 缓存key前缀
    private static final String PRODUCT_DETAIL_KEY = "product:detail:";
    private static final String PRODUCT_CATEGORY_KEY = "product:category:";
    private static final String PRODUCT_HOT_KEY = "product:hot";
    // 缓存过期时间（秒）
    private static final long PRODUCT_CACHE_EXPIRE = 3600; // 1小时
    private static final long CATEGORY_CACHE_EXPIRE = 7200; // 2小时

    @Override
    public PageResult<ProductDTO> getProductPage(ProductQueryRequest request) {
        // 创建分页对象
        Page<ProductDTO> page = new Page<>(request.getPage(), request.getSize());
        
        // 构建缓存key
        String cacheKey = buildProductListCacheKey(request);
        
        // 尝试从缓存获取
        Object cacheResult = redisUtils.get(cacheKey);
        if (cacheResult != null) {
            return (PageResult<ProductDTO>) cacheResult;
        }
        
        // 缓存未命中，执行数据库查询
        IPage<ProductDTO> productPage = productMapper.queryProductPage(page, request);
        
        // 处理每个商品的图片列表
        List<ProductDTO> records = productPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(this::processProductImages);
        }
        
        // 构建结果并缓存
        PageResult<ProductDTO> result = PageResult.of(
            productPage.getTotal(), 
            records, 
            request.getPage(), 
            request.getSize()
        );
        
        // 保存到缓存，设置过期时间
        redisUtils.set(cacheKey, result, PRODUCT_CACHE_EXPIRE);
        
        return result;
    }

    @Override
    public ProductDetailDTO getProductDetail(Long id) {
        String cacheKey = PRODUCT_DETAIL_KEY + id;
        
        // 尝试从缓存获取
        Object cacheResult = redisUtils.get(cacheKey);
        if (cacheResult != null) {
            return (ProductDetailDTO) cacheResult;
        }
        
        // 缓存未命中，查询商品基本信息
        Product product = getById(id);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }
        
        // 构建商品详情DTO
        ProductDetailDTO detailDTO = new ProductDetailDTO();
        BeanUtils.copyProperties(product, detailDTO);
        
        // 处理商品图片
        processProductImages(detailDTO);
        
        // 查询商品规格
        detailDTO.setSpecs(getProductSpecs(id));
        
        // 查询相关商品推荐（同类商品，限制5个）
        detailDTO.setRelatedProducts(getRelatedProducts(product.getCategoryId(), id, 5));
        
        // 保存到缓存，设置过期时间
        redisUtils.set(cacheKey, detailDTO, PRODUCT_CACHE_EXPIRE);
        
        // 更新热门商品排行榜
        updateHotProductRanking(id);
        
        return detailDTO;
    }
    
    /**
     * 更新热门商品排行榜
     */
    private void updateHotProductRanking(Long productId) {
        redisUtils.zIncrementScore(PRODUCT_HOT_KEY, productId.toString(), 1);
    }
    
    /**
     * 获取热门商品ID列表
     */
    public List<Long> getHotProducts(int limit) {
        Set<Object> hotProducts = redisUtils.zReverseRange(PRODUCT_HOT_KEY, 0, limit - 1);
        if (hotProducts == null || hotProducts.isEmpty()) {
            return Collections.emptyList();
        }
        
        return hotProducts.stream()
                .map(item -> Long.parseLong(item.toString()))
                .collect(Collectors.toList());
    }
    
    /**
     * 构建商品列表缓存key
     */
    private String buildProductListCacheKey(ProductQueryRequest request) {
        StringBuilder sb = new StringBuilder("product:list:");
        
        if (request.getCategoryId() != null) {
            sb.append("cid_").append(request.getCategoryId()).append(":");
        }
        
        if (StringUtils.hasText(request.getKeyword())) {
            sb.append("kw_").append(request.getKeyword()).append(":");
        }
        
        if (request.getIsNew() != null) {
            sb.append("new_").append(request.getIsNew()).append(":");
        }
        
        if (request.getIsHot() != null) {
            sb.append("hot_").append(request.getIsHot()).append(":");
        }
        
        if (StringUtils.hasText(request.getSortField())) {
            sb.append("sort_").append(request.getSortField())
              .append("_").append(request.getSortOrder()).append(":");
        }
        
        sb.append("page_").append(request.getPage())
          .append("_").append(request.getSize());
        
        return sb.toString();
    }

    /**
     * 处理商品图片，将逗号分隔的图片字符串转为列表
     */
    private void processProductImages(ProductDTO product) {
        if (product != null && StringUtils.hasText(product.getThumbnail())) {
            List<String> imageList = new ArrayList<>();
            // 添加缩略图作为第一张图片
            imageList.add(product.getThumbnail());
            
            // 如果有其他图片，也添加进去
            if (product instanceof ProductDetailDTO) {
                ProductDetailDTO detailDTO = (ProductDetailDTO) product;
                String images = detailDTO.getImages();
                if (StringUtils.hasText(images)) {
                    imageList.addAll(Arrays.asList(images.split(",")));
                }
            }
            
            product.setImageList(imageList);
        }
    }
    
    /**
     * 获取商品的规格信息
     */
    private List<ProductSpecDTO> getProductSpecs(Long productId) {
        // 尝试从缓存获取
        String cacheKey = "product:spec:" + productId;
        Object cacheResult = redisUtils.get(cacheKey);
        if (cacheResult != null) {
            return (List<ProductSpecDTO>) cacheResult;
        }
        
        // 查询商品的所有规格值
        List<ProductSpecValue> specValues = productSpecValueMapper.getByProductId(productId);
        if (CollectionUtils.isEmpty(specValues)) {
            return new ArrayList<>();
        }
        
        // 获取所有规格ID
        Set<Long> specIds = specValues.stream()
                .map(ProductSpecValue::getSpecId)
                .collect(Collectors.toSet());
        
        // 查询规格信息
        LambdaQueryWrapper<ProductSpec> specQueryWrapper = new LambdaQueryWrapper<>();
        specQueryWrapper.in(ProductSpec::getId, specIds)
                       .orderByAsc(ProductSpec::getSort);
        List<ProductSpec> specs = productSpecMapper.selectList(specQueryWrapper);
        
        // 将规格值按规格ID分组
        Map<Long, List<ProductSpecValue>> specValueMap = specValues.stream()
                .collect(Collectors.groupingBy(ProductSpecValue::getSpecId));
        
        // 构建规格DTO列表
        List<ProductSpecDTO> result = specs.stream().map(spec -> {
            ProductSpecDTO specDTO = new ProductSpecDTO();
            BeanUtils.copyProperties(spec, specDTO);
            
            // 设置规格值
            List<ProductSpecValue> values = specValueMap.getOrDefault(spec.getId(), new ArrayList<>());
            specDTO.setValues(values.stream()
                    .map(this::convertToSpecValueDTO)
                    .collect(Collectors.toList()));
            
            return specDTO;
        }).collect(Collectors.toList());
        
        // 缓存规格信息
        redisUtils.set(cacheKey, result, PRODUCT_CACHE_EXPIRE);
        
        return result;
    }
    
    /**
     * 获取相关商品
     */
    private List<ProductDTO> getRelatedProducts(Long categoryId, Long currentProductId, int limit) {
        // 尝试从缓存获取
        String cacheKey = "product:related:" + categoryId + ":" + currentProductId + ":" + limit;
        Object cacheResult = redisUtils.get(cacheKey);
        if (cacheResult != null) {
            return (List<ProductDTO>) cacheResult;
        }
        
        // 查询相关商品
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getCategoryId, categoryId)
                   .eq(Product::getStatus, 1)
                   .ne(Product::getId, currentProductId)
                   .orderByDesc(Product::getSales)
                   .last("LIMIT " + limit);
        
        List<Product> products = list(queryWrapper);
        
        List<ProductDTO> result = products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            BeanUtils.copyProperties(product, dto);
            processProductImages(dto);
            return dto;
        }).collect(Collectors.toList());
        
        // 缓存相关商品
        redisUtils.set(cacheKey, result, PRODUCT_CACHE_EXPIRE);
        
        return result;
    }
    
    /**
     * 转换规格值实体到DTO
     */
    private ProductSpecValueDTO convertToSpecValueDTO(ProductSpecValue specValue) {
        if (specValue == null) {
            return null;
        }
        
        ProductSpecValueDTO dto = new ProductSpecValueDTO();
        BeanUtils.copyProperties(specValue, dto);
        return dto;
    }
    
    /**
     * 清除商品缓存
     */
    public void clearProductCache(Long productId) {
        redisUtils.del(PRODUCT_DETAIL_KEY + productId);
        redisUtils.del("product:spec:" + productId);
        // 清除可能包含该商品的相关缓存
        // 由于无法确定哪些列表缓存包含该商品，可以设置较短的过期时间让缓存自动失效
        // 或者使用缓存前缀批量删除
        // TODO: 实现更精细的缓存清除策略
    }
} 