package com.moguhu.baize.client.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 布尔枚举
 * <p>
 * 是 YES  否 NO
 *
 * @author xuefeihu
 */
public enum BooleanEnum {

    YES, NO;

    private static final Map<String, BooleanEnum> mappings = new HashMap<>(2);

    static {
        for (BooleanEnum bool : values()) {
            mappings.put(bool.name(), bool);
        }
    }

    public static BooleanEnum resolve(String bool) {
        return (bool != null ? mappings.get(bool) : null);
    }

    public boolean matches(String bool) {
        return (this == resolve(bool));
    }

}
