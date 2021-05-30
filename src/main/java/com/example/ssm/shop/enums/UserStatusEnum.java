package com.example.ssm.shop.enums;

/**
 * @author admin
 */

public enum  UserStatusEnum {

    /**
     * 正常
     */
    NORMAL(1),

    /**
     * 禁止登录
     */
    BAN(0);

    private Integer value;

    UserStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
