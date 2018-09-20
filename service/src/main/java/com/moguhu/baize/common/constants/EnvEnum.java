package com.moguhu.baize.common.constants;

/**
 * 运行环境编码
 * <p>
 * 开发 DEV  测试 TEST  预发布 UAT  生产 ONLINE.
 *
 * @author xuefeihu
 */
public enum EnvEnum {

    DEV("DEV", "开发"),

    TEST("TEST", "测试"),

    UAT("UAT", "预发布"),

    ONLINE("ONLINE", "生产"), ;

    private String code;

    private String msg;

    EnvEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static final EnvEnum getFromKey(String code) {
        for (EnvEnum e : EnvEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
