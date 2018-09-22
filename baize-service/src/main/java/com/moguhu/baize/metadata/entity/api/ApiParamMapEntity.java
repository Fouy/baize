package com.moguhu.baize.metadata.entity.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.moguhu.baize.client.constants.ParamMapTypeEnum;
import com.moguhu.baize.common.constants.BooleanEnum;
import com.moguhu.baize.common.constants.api.ParamTypeEnum;
import com.moguhu.baize.common.constants.api.PositionEnum;

import java.io.Serializable;
import java.util.Date;

public class ApiParamMapEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long mapId;

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
     * 参数ID.
     */
    private Long paramId;

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
     * 默认值.
     */
    private String defaultValue;

    /**
     * 描述说明.
     */
    private String info;

    /**
     * 映射类型： 映射 MAP  常量 CONSTANT  自定义 CUSTOM
     *
     * @see ParamMapTypeEnum
     */
    private String mapType;

    /**
     * 扩展信息.
     */
    private String extInfo;

    private static final long serialVersionUID = 1L;

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
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

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
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

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}