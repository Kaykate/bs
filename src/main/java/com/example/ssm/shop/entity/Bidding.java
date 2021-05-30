package com.example.ssm.shop.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞价记录
 * @author admin
 */
@Data
public class Bidding {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 竞价状态：1成功，0失败
     */
    private Integer status;

    /**
     * 用户
     */
    private User user;

    /**
     * 商品
     */
    private Product product;


}

