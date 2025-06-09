package com.goodme.api.controller;

import com.goodme.common.result.Result;
import com.goodme.service.dto.CategoryDTO;
import com.goodme.service.dto.PageResult;
import com.goodme.service.dto.ProductDTO;
import com.goodme.service.dto.ProductDetailDTO;
import com.goodme.service.dto.ProductQueryRequest;
import com.goodme.service.service.CategoryService;
import com.goodme.service.service.ProductService;
import com.goodme.service.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 */
@Tag(name = "商品管理", description = "商品相关接口")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductServiceImpl productServiceImpl;

    @Operation(summary = "获取商品分类", description = "获取商品分类树形结构")
    @GetMapping("/categories")
    public Result<List<CategoryDTO>> getCategories() {
        return Result.success(categoryService.getCategoryTree());
    }

    @Operation(summary = "获取商品列表", description = "分页获取商品列表")
    @PostMapping("/list")
    public Result<PageResult<ProductDTO>> getProductList(@RequestBody ProductQueryRequest request) {
        return Result.success(productService.getProductPage(request));
    }

    @Operation(summary = "获取商品详情", description = "根据商品ID获取商品详情")
    @GetMapping("/detail/{id}")
    public Result<ProductDetailDTO> getProductDetail(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long id) {
        return Result.success(productService.getProductDetail(id));
    }
    
    @Operation(summary = "获取热门商品", description = "获取热门商品列表")
    @GetMapping("/hot")
    public Result<List<ProductDTO>> getHotProducts(
            @Parameter(description = "返回数量", required = false)
            @RequestParam(defaultValue = "5") Integer limit) {
        // 获取热门商品ID列表
        List<Long> hotProductIds = productServiceImpl.getHotProducts(limit);
        
        // 根据ID批量查询商品信息
        ProductQueryRequest request = new ProductQueryRequest();
        request.setPage(1);
        request.setSize(limit);
        // 热门商品显示
        PageResult<ProductDTO> productPage = productService.getProductPage(request);
        
        return Result.success(productPage.getList());
    }
} 