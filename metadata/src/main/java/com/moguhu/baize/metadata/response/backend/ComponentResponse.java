package com.moguhu.baize.metadata.response.backend;

import com.moguhu.baize.metadata.entity.backend.ComponentEntity;

/**
 * 组件 响应
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
public class ComponentResponse extends ComponentEntity {

    private boolean checked = false;

    private String typeName;

    private String execPositionName;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getExecPositionName() {
        return execPositionName;
    }

    public void setExecPositionName(String execPositionName) {
        this.execPositionName = execPositionName;
    }
}
