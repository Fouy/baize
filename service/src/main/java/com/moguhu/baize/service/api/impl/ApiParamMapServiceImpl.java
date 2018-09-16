package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.api.ApiParamStatuslEnum;
import com.moguhu.baize.common.constants.api.ParamMapTypeEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiParamMapEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiParamEntity;
import com.moguhu.baize.metadata.entity.api.ApiParamMapEntity;
import com.moguhu.baize.metadata.request.api.ApiParamMapSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapUpdateRequest;
import com.moguhu.baize.metadata.request.api.ApiParamUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import com.moguhu.baize.service.api.ApiParamMapService;
import com.moguhu.baize.service.api.ApiParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * API Param 映射 管理
 *
 * @author xuefeihu
 */
@Service
public class ApiParamMapServiceImpl implements ApiParamMapService {

    private static final Logger logger = LoggerFactory.getLogger(ApiParamMapServiceImpl.class);

    @Autowired
    private ApiParamService apiParamService;

    @Autowired
    private ApiParamMapEntityMapper apiParamMapEntityMapper;

    @Override
    public PageListDto<ApiParamMapResponse> pageList(ApiParamMapSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ApiParamMapEntity> entityList = apiParamMapEntityMapper.queryAll(request);
        List<ApiParamMapResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiParamMapResponse.class);
        }
        PageListDto<ApiParamMapResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public ApiParamMapResponse selectById(Long mapId) {
        ApiParamMapResponse response = new ApiParamMapResponse();
        ApiParamMapEntity entity = apiParamMapEntityMapper.selectById(mapId);
        if (entity != null) {
            response = DozerUtil.map(entity, ApiParamMapResponse.class);
            ApiParamResponse apiParamResponse = apiParamService.selectById(entity.getParamId());
            if (null != apiParamResponse) {
                response.setParamName(apiParamResponse.getName());
            }
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(ApiParamMapUpdateRequest request) {
        if (request != null) {
            ApiParamMapEntity param = DozerUtil.map(request, ApiParamMapEntity.class);

            apiParamMapEntityMapper.lock(request.getMapId());
            apiParamMapEntityMapper.updateById(param);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long mapId) {
        ApiParamMapEntity apiParamMapEntity = apiParamMapEntityMapper.selectById(mapId);
        // 解绑原参数
        ParamMapTypeEnum resolve = ParamMapTypeEnum.resolve(apiParamMapEntity.getMapType());
        if (resolve != null && resolve.matches(ParamMapTypeEnum.MAP.name())) {
            ApiParamUpdateRequest param = new ApiParamUpdateRequest();
            param.setParamId(apiParamMapEntity.getParamId());
            param.setStatus(ApiParamStatuslEnum.UNBIND.name());
            apiParamService.updateById(param);
        }

        apiParamMapEntityMapper.deleteById(mapId);
    }

    @Override
    @Transactional
    public void save(ApiParamMapSaveRequest request) {
        ParamMapTypeEnum mapTypeEnum = ParamMapTypeEnum.resolve(request.getMapType());
        if (mapTypeEnum.matches(ParamMapTypeEnum.MAP.name())) {
            // 更新原参数
            ApiParamEntity apiParamEntity = apiParamService.selectById(request.getParamId());
            if (null != apiParamEntity) {
                ApiParamUpdateRequest param = new ApiParamUpdateRequest();
                param.setParamId(request.getParamId());
                param.setStatus(ApiParamStatuslEnum.BIND.name());
                apiParamService.updateById(param);
            }

            request.setNeed(apiParamEntity.getNeed());
            request.setType(apiParamEntity.getType());
        }

        apiParamMapEntityMapper.insert(request);
    }

    @Override
    public List<ApiParamMapResponse> all(ApiParamMapSearchRequest request) {
        List<ApiParamMapEntity> entityList = apiParamMapEntityMapper.queryAll(request);
        List<ApiParamMapResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiParamMapResponse.class);
        }
        return list;
    }

}
