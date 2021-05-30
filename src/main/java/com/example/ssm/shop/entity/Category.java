package com.example.ssm.shop.entity;

import lombok.Data;

/**
 * 商品分类
 * @author admin
 */
@Data
public class Category {

    /**
     * ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 数量
     */
    private Integer count;
}
