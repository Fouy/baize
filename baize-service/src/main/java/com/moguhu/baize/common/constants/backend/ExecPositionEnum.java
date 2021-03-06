package com.moguhu.baize.common.constants.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * 组件类型
 * <p>
 * 执行位置：前置 PRE  路由 ROUTE  后置 POST  错误 ERROR
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
public enum ExecPositionEnum {

    /**
     * 前置
     */
    PRE("PRE", "前置"),
    /**
     * 路由
     */
    ROUTE("ROUTE", "路由"),
    /**
     * 后置
     */
    POST("POST", "后置"),
    /**
     * 错误
     */
    ERROR("ERROR", "错误"),;

    private static final Map<String, ExecPositionEnum> mappings = new HashMap<>(4);

    static {
        for (ExecPositionEnum position : values()) {
            mappings.put(position.name(), position);
        }
    }

    private String code;

    private String desc;

    ExecPositionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ExecPositionEnum resolve(String position) {
        return (position != null ? mappings.get(position) : null);
    }

    public boolean matches(String position) {
        return (this == resolve(position));
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
