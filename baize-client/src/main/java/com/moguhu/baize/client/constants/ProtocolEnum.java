package com.moguhu.baize.client.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * API 支持协议
 *
 * @author xuefeihu
 */
public enum ProtocolEnum {

    HTTP, HTTPS;

    private static final Map<String, ProtocolEnum> mappings = new HashMap<>(2);

    static {
        for (ProtocolEnum protocol : values()) {
            mappings.put(protocol.name(), protocol);
        }
    }

    public static ProtocolEnum resolve(String protocol) {
        return (protocol != null ? mappings.get(protocol) : null);
    }

    public boolean matches(String protocol) {
        return (this == resolve(protocol));
    }

}
