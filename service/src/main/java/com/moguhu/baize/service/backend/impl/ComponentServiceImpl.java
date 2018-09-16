package com.moguhu.baize.service.backend.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.content.RichContentTypeEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.backend.ComponentEntityMapper;
import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.entity.common.RichContentEntity;
import com.moguhu.baize.metadata.request.backend.ComponentSaveRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.request.backend.ComponentUpdateRequest;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;
import com.moguhu.baize.service.backend.ComponentService;
import com.moguhu.baize.service.common.RichContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 组件 管理
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
@Service
public class ComponentServiceImpl implements ComponentService {

    private static final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);

    @Autowired
    private ComponentEntityMapper componentEntityMapper;

    @Autowired
    private RichContentService richContentService;

    @Override
    public PageListDto<ComponentResponse> pageList(ComponentSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ComponentEntity> entityList = componentEntityMapper.queryAll(request);
        List<ComponentResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ComponentResponse.class);
        }
        PageListDto<ComponentResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public ComponentResponse selectById(Long compId) {
        ComponentResponse response = new ComponentResponse();
        ComponentEntity entity = componentEntityMapper.selectById(compId);
        if (entity != null) {
            response = DozerUtil.map(entity, ComponentResponse.class);
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(ComponentUpdateRequest request) {
        if (request != null) {
            ComponentEntity componentEntity = componentEntityMapper.selectById(request.getCompId());
            RichContentEntity param = new RichContentEntity();
            param.setContentId(componentEntity.getContentId());
            param.setContent(request.getGroovyCode());
            richContentService.updateById(param);

            ComponentEntity compParam = DozerUtil.map(request, ComponentEntity.class);
            componentEntityMapper.lock(request.getCompId());
            int count = componentEntityMapper.updateById(compParam);
            if (count != 1) {
                throw new RuntimeException("wrong effected rows");
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(Long compId) {
        ComponentEntity componentEntity = componentEntityMapper.selectById(compId);
        if (componentEntity == null) {
            throw new RuntimeException("wrong compId error.");
        }
        richContentService.deleteById(componentEntity.getContentId());
        componentEntityMapper.deleteById(compId);
    }

    @Override
    @Transactional
    public void save(ComponentSaveRequest request) {
        // 写入 groovyCode
        RichContentEntity richContent = new RichContentEntity();
        richContent.setType(RichContentTypeEnum.GROOVY.name());
        richContent.setContent(request.getGroovyCode());
        richContentService.insert(richContent);

        request.setStatus(StatusEnum.OFF.name());
        request.setContentId(richContent.getContentId());
        componentEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void option(Long compId, String status) {
        ComponentEntity param = new ComponentEntity();
        param.setCompId(compId);
        param.setStatus(status);
        componentEntityMapper.lock(compId);
        componentEntityMapper.updateById(param);
    }

    @Override
    public List<ComponentResponse> all() {
        ComponentSearchRequest param = new ComponentSearchRequest();
        param.setStatus(StatusEnum.ON.name());
        List<ComponentEntity> entityList = componentEntityMapper.queryAll(param);
        List<ComponentResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, ComponentResponse.class);
        }
        return list;
    }

    @Override
    public List<Long> queryByApiGroup(Long groupId) {
        List<Long> result = new ArrayList<>();
        List<ComponentEntity> componentEntities = componentEntityMapper.queryByApiGroup(groupId);
        if (!CollectionUtils.isEmpty(componentEntities)) {
            componentEntities.forEach(componentEntity -> result.add(componentEntity.getCompId()));
        }
        return result;
    }

    @Override
    public List<Long> queryByApi(Long apiId) {
        List<Long> result = new ArrayList<>();
        List<ComponentEntity> componentEntities = componentEntityMapper.queryByApi(apiId);
        if (!CollectionUtils.isEmpty(componentEntities)) {
            componentEntities.forEach(componentEntity -> result.add(componentEntity.getCompId()));
        }
        return result;
    }
}
