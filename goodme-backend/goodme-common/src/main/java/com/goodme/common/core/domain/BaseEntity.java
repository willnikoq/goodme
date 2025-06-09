package com.goodme.common.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
} 