package com.moguhu.baize.metadata.request.backend;

import com.moguhu.baize.metadata.entity.backend.ComponentEntity;

/**
 * 更新请求
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
public class ComponentUpdateRequest extends ComponentEntity {

    private String groovyCode;

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }
}
