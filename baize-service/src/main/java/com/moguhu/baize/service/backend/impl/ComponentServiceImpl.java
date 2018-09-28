package com.moguhu.baize.service.backend.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.moguhu.baize.client.model.ComponentDto;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.content.RichContentTypeEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.entity.backend.ComponentEntity;
import com.moguhu.baize.metadata.entity.common.RichContentEntity;
import com.moguhu.baize.metadata.mapper.backend.ComponentEntityMapper;
import com.moguhu.baize.metadata.request.backend.ComponentSaveRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.request.backend.ComponentUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiCompResponse;
import com.moguhu.baize.metadata.response.api.ApiGroupCompResponse;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import com.moguhu.baize.service.api.ApiService;
import com.moguhu.baize.service.backend.ComponentService;
import com.moguhu.baize.service.common.RichContentService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiGroupService apiGroupService;

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
        ApiGroupCompResponse complist = apiGroupService.complist(groupId);
        if (null != complist && MapUtils.isNotEmpty(complist.getComponentMap())) {
            Map<String, List<ComponentResponse>> map = complist.getComponentMap();
            map.entrySet().forEach(entry -> {
                List<ComponentResponse> value = entry.getValue();
                value.forEach(componentResponse -> {
                    if (componentResponse.isChecked()) {
                        result.add(componentResponse.getCompId());
                    }
                });
            });
        }
        return result;
    }

    @Override
    public List<Long> queryByApi(Long apiId) {
        List<Long> result = new ArrayList<>();
        ApiCompResponse compResponse = apiService.complist(apiId);
        if (null != compResponse && MapUtils.isNotEmpty(compResponse.getComponentMap())) {
            Map<String, List<ComponentResponse>> map = compResponse.getComponentMap();
            map.entrySet().forEach(entry -> {
                List<ComponentResponse> value = entry.getValue();
                value.forEach(componentResponse -> {
                    if (componentResponse.isChecked()) {
                        result.add(componentResponse.getCompId());
                    }
                });
            });
        }
        return result;
    }

    @Override
    public ComponentDto getComponent(Long compId) {
        ComponentDto response = new ComponentDto();
        ComponentEntity entity = componentEntityMapper.selectById(compId);
        if (entity != null) {
            response = DozerUtil.map(entity, ComponentDto.class);
            RichContentEntity contentEntity = richContentService.selectById(entity.getContentId());
            response.setCompContent(contentEntity.getContent());
        }
        return response;
    }

    @Override
    public List<ComponentDto> getComponents(List<Long> compList) {
        List<ComponentEntity> entityList = componentEntityMapper.selectByIds(compList);
        return convertComponentDto(entityList);
    }

    @Override
    public List<ComponentDto> allComponents() {
        List<ComponentEntity> entityList = componentEntityMapper.queryAll(new ComponentSearchRequest());
        return convertComponentDto(entityList);
    }

    private List<ComponentDto> convertComponentDto(List<ComponentEntity> entityList) {
        final List<ComponentDto> componentDtos = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach(entity -> {
                ComponentDto componentDto = DozerUtil.map(entity, ComponentDto.class);
                RichContentEntity contentEntity = richContentService.selectById(entity.getContentId());
                componentDto.setCompContent(contentEntity.getContent());
                componentDtos.add(componentDto);
            });
        }
        return componentDtos;
    }

}
