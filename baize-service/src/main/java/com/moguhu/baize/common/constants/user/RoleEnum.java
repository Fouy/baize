package com.moguhu.baize.common.constants.user;

/**
 * 用户角色
 *
 * @author xuefeihu
 */
public enum RoleEnum {

    /**
     * 管理员
     */
    ADMIN("ADMIN"),

    /**
     * 普通用户
     */
    USER("USER"),

    ;

    private String desc;

    public String desc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    RoleEnum(String desc) {
        this.desc = desc;
    }
}
