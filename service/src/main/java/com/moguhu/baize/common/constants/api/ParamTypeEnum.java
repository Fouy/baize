package com.moguhu.baize.common.constants.api;

import java.util.HashMap;
import java.util.Map;

/**
 * API Param 类型
 * <p>
 * INT 整数; DECIMAL 小数; CHAR 字符; TIME 时间; BOOLEAN 布尔类型 (true/false)
 *
 * @author xuefeihu
 */
public enum ParamTypeEnum {

    /**
     * 整数
     */
    INT,
    /**
     * 小数
     */
    DECIMAL,
    /**
     * 字符
     */
    CHAR,
    /**
     * 时间
     */
    TIME,
    /**
     * 布尔类型 (true/false)
     */
    BOOLEAN,;

    private static final Map<String, ParamTypeEnum> mappings = new HashMap<>(5);

    static {
        for (ParamTypeEnum type : values()) {
            mappings.put(type.name(), type);
        }
    }

    public static ParamTypeEnum resolve(String type) {
        return (type != null ? mappings.get(type) : null);
    }

    public boolean matches(String type) {
        return (this == resolve(type));
    }

}
