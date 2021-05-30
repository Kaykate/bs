package com.example.ssm.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (Order)实体类
 *
 */
@Data
public class Order implements Serializable {
//serialVersionUID 用来表明类的不同版本间的兼容性。
//序列化的时候，被序列化的类要有一个唯一标记。客户端和服务端必须需要同一个对象，serialVersionUID的唯一值判定其为同一个对象。
    private static final long serialVersionUID = -25485605794925400L;

    /**
     * ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long productId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户
     */
    private User user;

    /**
     * 商品
     */
    private Product product;

    /**
     * 收货地址
     */
    private String address;

}