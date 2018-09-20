package com.moguhu.baize.client.model;

import com.moguhu.baize.client.constants.BooleanEnum;
import com.moguhu.baize.client.constants.ParamMapTypeEnum;
import com.moguhu.baize.client.constants.ParamTypeEnum;
import com.moguhu.baize.client.constants.PositionEnum;

import java.io.Serializable;

/**
 * API Param 映射 响应
 * <p>
 * Created by xuefeihu on 18/9/20.
 */
public class ApiParamMapDto implements Serializable {

    /**
     * 参数映射 ID.
     */
    private Long mapId;

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
     * 映射类型： 映射 MAP  自定义 CUSTOM.
     *
     * @see ParamMapTypeEnum
     */
    private String mapType;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
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
}
