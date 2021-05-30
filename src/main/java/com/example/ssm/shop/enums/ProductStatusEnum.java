package com.example.ssm.shop.enums;

/**
 * @author admin
 */

public enum ProductStatusEnum {

    /**
     * 流拍
     */
    FAIL(0),

    /**
     * 正在拍卖
     */
    NORMAL(1),

    /**
     * 成交
     */
    SUCCESS(2),

    /**
     * 下架
     */
    OFF(3);


    private Integer value;

    ProductStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
