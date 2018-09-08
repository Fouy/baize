package com.moguhu.baize.service.backend.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.backend.GateServiceEntityMapper;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.request.backend.GateServiceSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceUpdateRequest;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.backend.GateServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关服务 管理
 *
 * @author xuefeihu
 */
@Service
public class GateServiceServiceImpl implements GateServiceService {

    private static final Logger logger = LoggerFactory.getLogger(GateServiceServiceImpl.class);

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
        GateServiceEntity param = new GateServiceEntity();
        param.setServiceId(serviceId);
        param.setStatus(status);
        gateServiceEntityMapper.lock(serviceId);
        gateServiceEntityMapper.updateById(param);
    }
}
