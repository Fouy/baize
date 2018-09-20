package com.moguhu.baize.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 记录状态
 * <p>
 * 启用 ON 停用 OFF
 *
 * @author xuefeihu
 */
public enum StatusEnum {

    ON, OFF;

    private static final Map<String, StatusEnum> mappings = new HashMap<>(2);

    static {
        for (StatusEnum status : values()) {
            mappings.put(status.name(), status);
        }
    }

    public static StatusEnum resolve(String status) {
        return (status != null ? mappings.get(status) : null);
    }

    public boolean matches(String status) {
        return (this == resolve(status));
    }

}
