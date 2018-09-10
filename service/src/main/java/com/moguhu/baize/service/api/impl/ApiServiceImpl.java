package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.dao.mapper.api.ApiGroupEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiEntity;
import com.moguhu.baize.service.api.ApiService;
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
 * @author xuefeihu
 */
@Service
public class ApiServiceImpl implements ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Autowired
    private ApiEntityMapper apiEntityMapper;
    
    @Autowired
    private ApiGroupEntityMapper apiGroupEntityMapper;

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
}
