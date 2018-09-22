package com.moguhu.baize.core;

import com.google.common.collect.Lists;
import com.moguhu.baize.client.model.ApiDto;
import com.moguhu.baize.client.model.ApiGroupDto;
import com.moguhu.baize.client.model.ApiParamDto;
import com.moguhu.baize.client.model.ApiParamMapDto;
import com.moguhu.baize.metadata.entity.api.ApiEntity;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.backend.GateServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Zookeeper Model Convert
 * <p>
 * Created by xuefeihu on 18/9/20.
 */
@Component
public class ZookeeperModelConvert {

    @Autowired
    private GateServiceService gateServiceService;

    /**
     * API 参数转换
     *
     * @param apiParamList
     * @return
     */
    public List<ApiParamDto> convertParams(List<ApiParamResponse> apiParamList) {
        List<ApiParamDto> paramsDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(apiParamList)) {
            apiParamList.forEach(apiParamResponse -> {
                ApiParamDto apiParamDto = new ApiParamDto();
                apiParamDto.setApiId(apiParamResponse.getApiId());
                apiParamDto.setName(apiParamResponse.getName());
                apiParamDto.setNeed(apiParamResponse.getNeed());
                apiParamDto.setParamId(apiParamResponse.getParamId());
                apiParamDto.setPosition(apiParamResponse.getPosition());
                apiParamDto.setType(apiParamResponse.getType());
                paramsDtoList.add(apiParamDto);
            });
        }

        return paramsDtoList;
    }

    /**
     * 映射参数转换
     *
     * @param apiParamMapList
     * @return
     */
    public List<ApiParamMapDto> convertMappings(List<ApiParamMapResponse> apiParamMapList) {
        List<ApiParamMapDto> mapsDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(apiParamMapList)) {
            apiParamMapList.forEach(mapResponse -> {
                ApiParamMapDto mapDto = new ApiParamMapDto();
                mapDto.setMapType(mapResponse.getMapType());
                mapDto.setName(mapResponse.getName());
                mapDto.setMapId(mapResponse.getMapId());
                mapDto.setDefaultValue(mapResponse.getDefaultValue());
                mapDto.setApiId(mapResponse.getApiId());
                mapDto.setNeed(mapResponse.getNeed());
                mapDto.setParamId(mapResponse.getParamId());
                mapDto.setPosition(mapResponse.getPosition());
                mapDto.setType(mapResponse.getType());
                mapsDtoList.add(mapDto);
            });
        }

        return mapsDtoList;
    }

    /**
     * API Group 转换
     *
     * @param apiGroup
     * @param compIds
     * @return
     */
    public ApiGroupDto convertApiGroup(ApiGroupEntity apiGroup, List<Long> compIds) {
        ApiGroupDto groupDto = new ApiGroupDto();
        if (null != apiGroup) {
            GateServiceResponse gateService = gateServiceService.selectById(apiGroup.getServiceId());

            groupDto.setCompIds(compIds);
            groupDto.setGateServiceCode(gateService.getServiceCode());
            groupDto.setGroupId(apiGroup.getGroupId());
            groupDto.setHosts(gateService.getHosts());
            groupDto.setPath(apiGroup.getPath());
            groupDto.setType(apiGroup.getType());
        }

        return groupDto;
    }

    /**
     * API 转换
     *
     * @param apiDto
     * @param api
     */
    public void convertApi(ApiDto apiDto, ApiEntity api) {
        if (apiDto != null && api != null) {
            apiDto.setApiId(api.getApiId());
            apiDto.setGroupId(api.getGroupId());
            apiDto.setPath(api.getPath());
            apiDto.setMethods(api.getMethods());
            apiDto.setVersion(api.getVersion());
            apiDto.setCached(api.getCached());
            apiDto.setProtocol(api.getProtocol());
        }
    }

}
