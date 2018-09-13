package com.moguhu.baize.zuul.common;

import com.moguhu.baize.metadata.entity.backend.ComponentEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a ZuulFilter for representing and storing in a database
 */
public class FilterInfo extends ComponentEntity {

    private String groovyCode;

    private String groovyFileName;

    public FilterInfo() {
    }

    public FilterInfo(String compCode, String filterType, int filterOrder) {
        super();
        this.setCompCode(compCode);
        if (ZuulFilterTypeEnum.resolve(filterType) != null) { // zuul filter type mapping with component exec position
            this.setExecPosition(ZuulFilterTypeEnum.resolve(filterType).desc);
        }
        this.setPriority(filterOrder);
    }

    /**
     * builds the unique filterId key
     *
     * @param gateType
     * @param filterType
     * @param filterName
     * @return key is gateType:filterName:filterType
     */
    public static String buildFilterId(String gateType, String filterType, String filterName) {
        return gateType + ":" + filterName + ":" + filterType;
    }

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }

    public String getGroovyFileName() {
        return groovyFileName;
    }

    public void setGroovyFileName(String groovyFileName) {
        this.groovyFileName = groovyFileName;
    }

    /**
     * zuul filter type mapping with component exec position
     */
    enum ZuulFilterTypeEnum {

        /**
         * 前置
         */
        PRE("pre", "PRE"),
        /**
         * 路由
         */
        ROUTE("route", "ROUTE"),
        /**
         * 后置
         */
        POST("post", "POST"),
        /**
         * 错误
         */
        ERROR("error", "ERROR"),;

        private static final Map<String, ZuulFilterTypeEnum> mappings = new HashMap<>(4);

        static {
            for (ZuulFilterTypeEnum type : values()) {
                mappings.put(type.code, type);
            }
        }

        private String code;

        private String desc;

        ZuulFilterTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static ZuulFilterTypeEnum resolve(String code) {
            return (code != null ? mappings.get(code) : null);
        }

        public boolean matches(String code) {
            return (this == resolve(code));
        }

    }

}

