package com.example.ssm.shop.enums;

/**
 * @author admin
 */

public enum BiddingStatusEnum {

    /**
     * 未成功
     */
    NORMAL(0),

    /**
     * 成功
     */
    SUCCESS(1);


    private Integer value;

    BiddingStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
