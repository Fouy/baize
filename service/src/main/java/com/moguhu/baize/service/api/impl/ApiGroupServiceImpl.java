package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiGroupEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * API分组 管理
 *
 * @author xuefeihu
 */
@Service
public class ApiGroupServiceImpl implements ApiGroupService {

    private static final Logger logger = LoggerFactory.getLogger(ApiGroupServiceImpl.class);

    @Autowired
    private ApiGroupEntityMapper apiGroupEntityMapper;

    @Override
    public PageListDto<ApiGroupResponse> pageList(ApiGroupSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ApiGroupEntity> entityList = apiGroupEntityMapper.queryAll(request);
        List<ApiGroupResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiGroupResponse.class);
        }
        PageListDto<ApiGroupResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public ApiGroupResponse selectById(Long groupId) {
        ApiGroupResponse response = new ApiGroupResponse();
        ApiGroupEntity entity = apiGroupEntityMapper.selectById(groupId);
        if (entity != null) {
            response = DozerUtil.map(entity, ApiGroupResponse.class);
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(ApiGroupUpdateRequest request) {
        if (request != null) {
            ApiGroupEntity param = DozerUtil.map(request, ApiGroupEntity.class);

            apiGroupEntityMapper.lock(request.getGroupId());
            apiGroupEntityMapper.updateById(param);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long groupId) {
        int count = apiGroupEntityMapper.deleteById(groupId);
        if (count != 1) {
            throw new RuntimeException("wrong count error!");
        }
    }

    @Override
    @Transactional
    public void save(ApiGroupSaveRequest request) {
        request.setStatus(StatusEnum.OFF.name());
        apiGroupEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void option(Long groupId, String status) {
        ApiGroupEntity param = new ApiGroupEntity();
        param.setGroupId(groupId);
        param.setStatus(status);
        apiGroupEntityMapper.lock(groupId);
        int count = apiGroupEntityMapper.updateById(param);
        if (count != 1) {
            throw new RuntimeException("wrong effected row.");
        }
    }

    @Override
    public List<ApiGroupResponse> all() {
        ApiGroupSearchRequest param = new ApiGroupSearchRequest();
        param.setStatus(StatusEnum.ON.name());
        List<ApiGroupEntity> entityList = apiGroupEntityMapper.queryAll(param);
        List<ApiGroupResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiGroupResponse.class);
        }
        return list;
    }

}
