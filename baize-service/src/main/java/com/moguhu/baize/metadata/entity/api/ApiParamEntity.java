package com.moguhu.baize.metadata.entity.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.moguhu.baize.common.constants.BooleanEnum;
import com.moguhu.baize.common.constants.api.ApiParamStatuslEnum;
import com.moguhu.baize.common.constants.api.ParamTypeEnum;
import com.moguhu.baize.common.constants.api.PositionEnum;

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
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间.
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * API ID.
     */
    private Long apiId;

    /**
     * 参数位置：PATH参数; GET参数; POST参数; HEAD参数; body参数(如图片上传).
     *
     * @see PositionEnum
     */
    private String position;

    /**
     * 类型：INT 整数; DECIMAL 小数; CHAR 字符; TIME 时间; BOOLEAN 布尔类型 (true/false).
     *
     * @see ParamTypeEnum
     */
    private String type;

    /**
     * 名称.
     */
    private String name;

    /**
     * 是否必须：是 YES  否 NO.
     *
     * @see BooleanEnum
     */
    private String need;

    /**
     * 描述说明.
     */
    private String info;

    /**
     * 状态：BIND 已绑定  UNBIND 未绑定.
     *
     * @see ApiParamStatuslEnum
     */
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}