package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.backend.ComponentTypeEnum;
import com.moguhu.baize.common.constants.backend.ExecPositionEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiCompRelaEntityMapper;
import com.moguhu.baize.metadata.dao.mapper.api.ApiEntityMapper;
import com.moguhu.baize.metadata.dao.mapper.api.ApiGroupEntityMapper;
import com.moguhu.baize.metadata.dao.mapper.backend.ComponentEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiCompRelaEntity;
import com.moguhu.baize.metadata.entity.api.ApiEntity;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.response.api.ApiCompResponse;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;
import com.moguhu.baize.service.api.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API 管理
 *
 * @author xuefeihu
 */
@Service
public class ApiServiceImpl implements ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Autowired
    private ApiEntityMapper apiEntityMapper;

    @Autowired
    private ApiGroupEntityMapper apiGroupEntityMapper;

    @Autowired
    private ComponentEntityMapper componentEntityMapper;

    @Autowired
    private ApiCompRelaEntityMapper apiCompRelaEntityMapper;

    @Override
    public PageListDto<ApiResponse> pageList(ApiSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ApiEntity> entityList = apiEntityMapper.queryAll(request);
        List<ApiResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiResponse.class);
            list.forEach(apiResponse -> {
                ApiGroupEntity apiGroupEntity = apiGroupEntityMapper.selectById(apiResponse.getGroupId());
                apiResponse.setGroupName(apiGroupEntity.getName());
            });
        }
        PageListDto<ApiResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public ApiResponse selectById(Long apiId) {
        ApiResponse response = new ApiResponse();
        ApiEntity entity = apiEntityMapper.selectById(apiId);
        if (entity != null) {
            response = DozerUtil.map(entity, ApiResponse.class);
            ApiGroupEntity apiGroupEntity = apiGroupEntityMapper.selectById(response.getGroupId());
            response.setGroupName(apiGroupEntity.getName());
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(ApiUpdateRequest request) {
        if (request != null) {
            ApiEntity param = DozerUtil.map(request, ApiEntity.class);

            apiEntityMapper.lock(request.getApiId());
            apiEntityMapper.updateById(param);
        }
    }

    @Override
    public void deleteById(Long apiId) {
        apiEntityMapper.deleteById(apiId);
    }

    @Override
    @Transactional
    public void save(ApiSaveRequest request) {
        request.setStatus(StatusEnum.OFF.name());
        apiEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void option(Long apiId, String status) {
        ApiEntity param = new ApiEntity();
        param.setApiId(apiId);
        param.setStatus(status);
        apiEntityMapper.lock(apiId);
        apiEntityMapper.updateById(param);
    }

    @Override
    public ApiCompResponse complist(Long apiId) {
        ApiCompResponse response = new ApiCompResponse();

        ApiEntity apiEntity = apiEntityMapper.selectById(apiId);
        response.setApiId(apiId);
        response.setName(apiEntity.getName());

        ComponentSearchRequest param = new ComponentSearchRequest();
        param.setStatus(StatusEnum.ON.name());
        List<ComponentEntity> allList = componentEntityMapper.queryAll(param);
        List<ComponentEntity> compList = componentEntityMapper.queryByApi(apiId);

        if (!CollectionUtils.isEmpty(allList)) {
            List<ComponentResponse> componentResponses = DozerUtil.mapList(allList, ComponentResponse.class);
            Map<String, List<ComponentResponse>> componentMaps = convert2Map(componentResponses);
            response.setComponentMap(componentMaps);

            componentResponses.forEach(componentResponse -> {
                if (compList.contains(componentResponse)) {
                    componentResponse.setChecked(true);
                }
                componentResponse.setTypeName(ComponentTypeEnum.resolve(componentResponse.getType()).getDesc());
                componentResponse.setExecPositionName(ExecPositionEnum.resolve(componentResponse.getExecPosition()).getDesc());
            });
        }

        return response;
    }

    @Override
    @Transactional
    public void savecomp(Long apiId, String compIds) {
        // 删除所有关联
        apiCompRelaEntityMapper.deleteByApiId(apiId);

        if (!StringUtils.isEmpty(compIds)) {
            List<ApiCompRelaEntity> batchList = new ArrayList<>();
            String[] compIdArray = compIds.split(",");
            for (int i = 0; i < compIdArray.length; i++) {
                ApiCompRelaEntity relaEntity = new ApiCompRelaEntity();
                relaEntity.setApiId(apiId);
                relaEntity.setCompId(Long.parseLong(compIdArray[i]));
                batchList.add(relaEntity);
            }
            apiCompRelaEntityMapper.batchInsert(batchList);
        }
    }

    /**
     * 组件转Map
     *
     * @param componentResponses
     * @return
     */
    private Map<String, List<ComponentResponse>> convert2Map(List<ComponentResponse> componentResponses) {
        Map<String, List<ComponentResponse>> map = new HashMap<>();
        ComponentTypeEnum[] types = ComponentTypeEnum.values();
        for (int i = 0; i < types.length; i++) {
            map.put(types[i].name(), new ArrayList<>());
        }

        componentResponses.forEach(componentResponse -> {
            List<ComponentResponse> tempList = map.get(componentResponse.getType());
            tempList.add(componentResponse);
        });
        return map;
    }

}
