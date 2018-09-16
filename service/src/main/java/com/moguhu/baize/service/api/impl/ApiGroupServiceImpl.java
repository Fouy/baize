package com.moguhu.baize.service.api.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.backend.ComponentTypeEnum;
import com.moguhu.baize.common.constants.backend.ExecPositionEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.core.task.ApiGroupSyncTask;
import com.moguhu.baize.metadata.dao.mapper.api.ApiGroupEntityMapper;
import com.moguhu.baize.metadata.dao.mapper.api.GroupCompRelaEntityMapper;
import com.moguhu.baize.metadata.dao.mapper.backend.ComponentEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.entity.api.GroupCompRelaEntity;
import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.response.api.ApiGroupCompResponse;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;
import com.moguhu.baize.service.CommonThreadService;
import com.moguhu.baize.service.api.ApiGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ComponentEntityMapper componentEntityMapper;

    @Autowired
    private GroupCompRelaEntityMapper groupCompRelaEntityMapper;

    @Autowired
    private CommonThreadService commonThreadService;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

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
        apiGroupEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void syncZookeeper(Long groupId) {
        // 创建 sync task
        ApiGroupSyncTask task = new ApiGroupSyncTask(groupId);
        autowireCapableBeanFactory.autowireBean(task);
        commonThreadService.submit(task);
    }

    @Override
    public List<ApiGroupResponse> all() {
        ApiGroupSearchRequest param = new ApiGroupSearchRequest();
        List<ApiGroupEntity> entityList = apiGroupEntityMapper.queryAll(param);
        List<ApiGroupResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ApiGroupResponse.class);
        }
        return list;
    }

    @Override
    public ApiGroupCompResponse complist(Long groupId) {
        ApiGroupCompResponse response = new ApiGroupCompResponse();

        ApiGroupEntity apiGroupEntity = apiGroupEntityMapper.selectById(groupId);
        response.setGroupId(groupId);
        response.setName(apiGroupEntity.getName());

        ComponentSearchRequest param = new ComponentSearchRequest();
        param.setStatus(StatusEnum.ON.name());
        List<ComponentEntity> allList = componentEntityMapper.queryAll(param);
        List<ComponentEntity> compList = componentEntityMapper.queryByApiGroup(groupId);

        if (!CollectionUtils.isEmpty(allList)) {
            List<ComponentResponse> componentResponses = DozerUtil.mapList(allList, ComponentResponse.class);
            Map<String, List<ComponentResponse>> componentMaps = ComponentConvert.convert2Map(componentResponses);
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
    public void savecomp(Long groupId, String compIds) {
        // 删除所有关联
        groupCompRelaEntityMapper.deleteByGroupId(groupId);

        if (!StringUtils.isEmpty(compIds)) {
            List<GroupCompRelaEntity> batchList = new ArrayList<>();
            String[] compIdArray = compIds.split(",");
            for (int i = 0; i < compIdArray.length; i++) {
                GroupCompRelaEntity relaEntity = new GroupCompRelaEntity();
                relaEntity.setGroupId(groupId);
                relaEntity.setCompId(Long.parseLong(compIdArray[i]));
                batchList.add(relaEntity);
            }
            groupCompRelaEntityMapper.batchInsert(batchList);
        }
    }

}
