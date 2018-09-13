package com.moguhu.baize.common.constants.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * 组件类型
 * <p>
 * 认证 AUTH; 鉴权(角色权限) ALLOW; 流量控制 TRAFFIC; 缓存 CACHE; 路由 ROUTE; 日志 LOG; 协议转换 PROTO_CONVERT; 其他 OTHER
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
public enum ComponentTypeEnum {

    /**
     * 认证
     */
    AUTH,
    /**
     * 鉴权(角色权限)
     */
    ALLOW,
    /**
     * 流量控制
     */
    TRAFFIC,
    /**
     * 缓存
     */
    CACHE,
    /**
     * 路由
     */
    ROUTE,
    /**
     * 日志
     */
    LOG,
    /**
     * 协议转换
     */
    PROTO_CONVERT,
    /**
     * 其他
     */
    OTHER,;

    private static final Map<String, ComponentTypeEnum> mappings = new HashMap<>(8);

    static {
        for (ComponentTypeEnum type : values()) {
            mappings.put(type.name(), type);
        }
    }

    public static ComponentTypeEnum resolve(String type) {
        return (type != null ? mappings.get(type) : null);
    }

    public boolean matches(String type) {
        return (this == resolve(type));
    }

}
