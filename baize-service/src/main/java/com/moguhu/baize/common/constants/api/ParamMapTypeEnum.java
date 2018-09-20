package com.moguhu.baize.common.constants.api;

import java.util.HashMap;
import java.util.Map;

/**
 * API Param 映射类型
 * <p>
 * 映射 MAP  自定义 CUSTOM
 *
 * @author xuefeihu
 */
public enum ParamMapTypeEnum {

    MAP, CUSTOM;

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
