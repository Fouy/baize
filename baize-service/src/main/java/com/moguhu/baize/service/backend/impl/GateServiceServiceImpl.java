package com.moguhu.baize.service.backend.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.core.task.SingleServiceSyncTask;
import com.moguhu.baize.metadata.mapper.backend.GateServiceEntityMapper;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.request.backend.GateServiceSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceUpdateRequest;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.CommonThreadService;
import com.moguhu.baize.service.backend.GateServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关服务 管理
 *
 * Created by xuefeihu on 18/9/8.
 */
@Service
public class GateServiceServiceImpl implements GateServiceService {

    private static final Logger logger = LoggerFactory.getLogger(GateServiceServiceImpl.class);

    @Autowired
    private CommonThreadService commonThreadService;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    private GateServiceEntityMapper gateServiceEntityMapper;

    @Override
    public PageListDto<GateServiceResponse> pageList(GateServiceSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<GateServiceEntity> entityList = gateServiceEntityMapper.queryAll(request);
        List<GateServiceResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, GateServiceResponse.class);
        }
        PageListDto<GateServiceResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public GateServiceResponse selectById(Long serviceId) {
        GateServiceResponse response = new GateServiceResponse();
        GateServiceEntity entity = gateServiceEntityMapper.selectById(serviceId);
        if (entity != null) {
            response = DozerUtil.map(entity, GateServiceResponse.class);
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(GateServiceUpdateRequest request) {
        if (request != null) {
            GateServiceEntity param = DozerUtil.map(request, GateServiceEntity.class);

            gateServiceEntityMapper.lock(request.getServiceId());
            gateServiceEntityMapper.updateById(param);
        }
    }

    @Override
    public void deleteById(Long serviceId) {
        gateServiceEntityMapper.deleteById(serviceId);
    }

    @Override
    @Transactional
    public void save(GateServiceSaveRequest request) {
        request.setStatus(StatusEnum.OFF.name());

        gateServiceEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void option(Long serviceId, String status) {
        // 创建 sync task
        SingleServiceSyncTask task = new SingleServiceSyncTask(serviceId, status, null);
        autowireCapableBeanFactory.autowireBean(task);
        commonThreadService.submit(task);

        GateServiceEntity param = new GateServiceEntity();
        param.setServiceId(serviceId);
        param.setStatus(status);
        gateServiceEntityMapper.lock(serviceId);
        gateServiceEntityMapper.updateById(param);
    }

    @Override
    public List<GateServiceResponse> all() {
        GateServiceSearchRequest param = new GateServiceSearchRequest();
        List<GateServiceEntity> entityList = gateServiceEntityMapper.queryAll(param);
        List<GateServiceResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, GateServiceResponse.class);
            list.forEach(gateServiceResponse -> {
                if (StatusEnum.OFF.name().equals(gateServiceResponse.getStatus())) {
                    gateServiceResponse.setName(gateServiceResponse.getName() + "[已下线]");
                }
            });
        }
        return list;
    }
}
