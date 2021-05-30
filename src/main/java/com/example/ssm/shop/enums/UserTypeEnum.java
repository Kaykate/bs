package com.example.ssm.shop.enums;

/**
 * @author admin
 */

public enum UserTypeEnum {

    /**
     * 管理员
     */
    ADMIN(1),

    /**
     * 普通用户
     */
    USER(0);

    private Integer value;

    UserTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
