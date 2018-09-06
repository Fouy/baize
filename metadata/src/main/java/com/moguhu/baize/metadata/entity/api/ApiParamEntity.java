package com.moguhu.baize.metadata.entity.api;

import java.io.Serializable;
import java.util.Date;

public class ApiParamEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long paramId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date modifyTime;

    /**
     * API ID.
     */
    private Long apiId;

    /**
     * 参数位置：路径参数 PATH  head参数 HEAD  表单参数 FORM.
     */
    private String position;

    /**
     * 类型：字符 CHAR  整数 INT  小数 DECIMAL.
     */
    private String type;

    /**
     * 名称.
     */
    private String name;

    /**
     * 是否必须：是 YES  否 NO.
     */
    private String need;

    /**
     * 描述说明.
     */
    private String info;

    /**
     * 扩展信息.
     */
    private String extInfo;

    private static final long serialVersionUID = 1L;

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
}