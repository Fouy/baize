package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiParamMapEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiParamMapEntity;
import com.moguhu.baize.metadata.request.api.ApiParamMapSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.service.api.ApiParamMapService;
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
    public void deleteById(Long mapId) {
        apiParamMapEntityMapper.deleteById(mapId);
    }

    @Override
    @Transactional
    public void save(ApiParamMapSaveRequest request) {
        apiParamMapEntityMapper.insert(request);
    }

}
