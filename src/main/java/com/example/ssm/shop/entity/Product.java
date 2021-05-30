package com.example.ssm.shop.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品
 *
 * @author admin
 */
@Data
public class Product {

    /**
     * ID
     */
    private Long id;

    /**
     * 发布人用户ID
     */
    private Long userId;

    /**
     * 分类ID
     */
    private Long cateId;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String summary;

    /**
     * 起拍价
     */
    private BigDecimal startPrice;

    /**
     * 当前价格
     */
    private BigDecimal currentPrice;

    /**
     * 图片，多个以逗号分隔
     */
    private String imgUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 状态：1正在拍卖 0流拍 2成交 3下架
     */
    private Integer status;

    /**
     * 用户
     */
    private User user;

    /**
     * 分类
     */
    private Category category;

    /**
     * 订单
     */
    private Order order;

    /**
     * 竞价列表
     */
    private List<Bidding> biddingList;
}
