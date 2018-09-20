package com.moguhu.baize.metadata.response.backend;

/**
 * 网关实例
 * <p>
 * Created by xuefeihu on 18/9/9.
 */
public class GateInstanceResponse {

    /**
     * 服务名称.
     */
    private String name;

    /**
     * 服务编码.
     */
    private String serviceCode;

    /**
     * HOST
     */
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
