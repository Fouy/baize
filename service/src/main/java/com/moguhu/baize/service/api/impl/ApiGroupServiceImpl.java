package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.common.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.common.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.common.response.api.ApiGroupResponse;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.api.ApiGroupEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
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
    public void deleteById(Long groupId) {
        apiGroupEntityMapper.deleteById(groupId);
    }

    @Override
    @Transactional
    public void save(ApiGroupSaveRequest request) {
        apiGroupEntityMapper.insert(request);
    }

}
