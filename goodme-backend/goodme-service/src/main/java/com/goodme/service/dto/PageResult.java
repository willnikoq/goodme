package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
@Schema(description = "分页结果")
public class PageResult<T> {
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "数据列表")
    private List<T> list;
    
    @Schema(description = "页码")
    private Integer page;
    
    @Schema(description = "每页条数")
    private Integer size;
    
    @Schema(description = "总页数")
    private Integer pages;
    
    public PageResult() {
    }
    
    public PageResult(Long total, List<T> list, Integer page, Integer size) {
        this.total = total;
        this.list = list;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }
    
    public static <T> PageResult<T> of(Long total, List<T> list, Integer page, Integer size) {
        return new PageResult<>(total, list, page, size);
    }
} 