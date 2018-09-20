package com.moguhu.baize.client.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * API Param 位置
 * <p>
 * PATH参数; GET参数; POST参数; HEAD参数; body参数(如图片上传)
 *
 * @author xuefeihu
 */
public enum PositionEnum {

    /**
     * PATH参数
     */
    PATH,
    /**
     * GET参数
     */
    GET,
    /**
     * POST参数
     */
    POST,
    /**
     * HEAD参数
     */
    HEAD,
    /**
     * body参数(如图片上传)
     */
    BODY,;

    private static final Map<String, PositionEnum> mappings = new HashMap<>(5);

    static {
        for (PositionEnum position : values()) {
            mappings.put(position.name(), position);
        }
    }

    public static PositionEnum resolve(String position) {
        return (position != null ? mappings.get(position) : null);
    }

    public boolean matches(String position) {
        return (this == resolve(position));
    }

}
