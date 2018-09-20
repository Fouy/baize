package com.moguhu.baize.common.constants.content;

import java.util.HashMap;
import java.util.Map;

/**
 * 富文本类型
 * <p>
 * GROOVY groovy脚本
 *
 * @author xuefeihu
 */
public enum RichContentTypeEnum {

    /**
     * groovy脚本
     */
    GROOVY,;

    private static final Map<String, RichContentTypeEnum> mappings = new HashMap<>(1);

    static {
        for (RichContentTypeEnum type : values()) {
            mappings.put(type.name(), type);
        }
    }

    public static RichContentTypeEnum resolve(String type) {
        return (type != null ? mappings.get(type) : null);
    }

    public boolean matches(String type) {
        return (this == resolve(type));
    }

}
