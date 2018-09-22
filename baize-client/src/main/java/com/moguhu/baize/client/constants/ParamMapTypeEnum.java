package com.moguhu.baize.client.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * API Param 映射类型
 * <p>
 * 映射 MAP  常量 CONSTANT  自定义 CUSTOM (自定义表达式, 暂不支持)
 *
 * @author xuefeihu
 */
public enum ParamMapTypeEnum {

    MAP, CONSTANT, CUSTOM;

    private static final Map<String, ParamMapTypeEnum> mappings = new HashMap<>(2);

    static {
        for (ParamMapTypeEnum mapType : values()) {
            mappings.put(mapType.name(), mapType);
        }
    }

    public static ParamMapTypeEnum resolve(String mapType) {
        return (mapType != null ? mappings.get(mapType) : null);
    }

    public boolean matches(String mapType) {
        return (this == resolve(mapType));
    }

}
