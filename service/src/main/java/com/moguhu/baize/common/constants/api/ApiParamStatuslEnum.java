package com.moguhu.baize.common.constants.api;

import java.util.HashMap;
import java.util.Map;

/**
 * API Param 状态
 * <p>
 * BIND 已绑定  UNBIND 未绑定
 *
 * @author xuefeihu
 */
public enum ApiParamStatuslEnum {

    BIND, UNBIND;

    private static final Map<String, ApiParamStatuslEnum> mappings = new HashMap<>(2);

    static {
        for (ApiParamStatuslEnum status : values()) {
            mappings.put(status.name(), status);
        }
    }

    public static ApiParamStatuslEnum resolve(String status) {
        return (status != null ? mappings.get(status) : null);
    }

    public boolean matches(String status) {
        return (this == resolve(status));
    }

}
