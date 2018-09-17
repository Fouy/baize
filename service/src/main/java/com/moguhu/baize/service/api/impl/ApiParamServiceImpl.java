package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.api.ApiParamStatuslEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiParamEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiParamEntity;
import com.moguhu.baize.metadata.request.api.ApiParamSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiParamSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
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
 * API 管理
 *
 * Created by xuefeihu on 18/9/11.
 */
@Service
public class ApiParamServiceImpl implements ApiParamService {

    private static final Logger logger = LoggerFactory.getLogger(ApiParamServiceImpl.class);

    @Autowired
    private ApiParamEntityMapper apiParamEntityMapper;

    @Override
    public PageListDto<ApiParamResponse> pageList(ApiParamSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ApiParamEntity> entityList = apiParamEntityMapper.queryAll(request);
        List<ApiParamResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiParamResponse.class);
        }
        PageListDto<ApiParamResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public ApiParamResponse selectById(Long paramId) {
        ApiParamResponse response = new ApiParamResponse();
        ApiParamEntity entity = apiParamEntityMapper.selectById(paramId);
        if (entity != null) {
            response = DozerUtil.map(entity, ApiParamResponse.class);
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(ApiParamUpdateRequest request) {
        if (request != null) {
            ApiParamEntity param = DozerUtil.map(request, ApiParamEntity.class);

            apiParamEntityMapper.lock(request.getParamId());
            apiParamEntityMapper.updateById(param);
        }
    }

    @Override
    public void deleteById(Long paramId) {
        apiParamEntityMapper.deleteById(paramId);
    }

    @Override
    @Transactional
    public void save(ApiParamSaveRequest request) {
        request.setStatus(ApiParamStatuslEnum.UNBIND.name());
        apiParamEntityMapper.insert(request);
    }

    @Override
    public List<ApiParamResponse> all(ApiParamSearchRequest request) {
        List<ApiParamEntity> entityList = apiParamEntityMapper.queryAll(request);
        List<ApiParamResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiParamResponse.class);
        }

        return list;
    }

}
