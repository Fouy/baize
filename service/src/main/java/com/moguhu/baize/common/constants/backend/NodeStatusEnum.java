package com.moguhu.baize.common.constants.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点状态枚举
 * <p>
 * 已创建 CREATED 收集中 COLLECTING 运行中 RUNNING  已关闭 CLOSED
 *
 * @author xuefeihu
 */
public enum NodeStatusEnum {

    CREATED("CREATED", "已创建"),

    COLLECTING("COLLECTING", "收集中"),

    RUNNING("RUNNING", "运行中"),

    CLOSED("CLOSED", "已关闭"),;

    private static final Map<String, NodeStatusEnum> mappings = new HashMap<>(4);

    static {
        for (NodeStatusEnum bool : values()) {
            mappings.put(bool.name(), bool);
        }
    }

    private String code;

    private String msg;

    NodeStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static NodeStatusEnum resolve(String bool) {
        return (bool != null ? mappings.get(bool) : null);
    }

    public boolean matches(String bool) {
        return (this == resolve(bool));
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
