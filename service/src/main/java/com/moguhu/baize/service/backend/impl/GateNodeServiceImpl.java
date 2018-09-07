package com.moguhu.baize.service.backend.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moguhu.baize.common.utils.DozerUtil;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.backend.GateNodeEntityMapper;
import com.moguhu.baize.metadata.entity.backend.GateNodeEntity;
import com.moguhu.baize.metadata.request.backend.GateNodeSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeUpdateRequest;
import com.moguhu.baize.metadata.response.backend.GateNodeResponse;
import com.moguhu.baize.service.backend.GateNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关节点 管理
 *
 * @author xuefeihu
 */
@Service
public class GateNodeServiceImpl implements GateNodeService {

    private static final Logger logger = LoggerFactory.getLogger(GateNodeServiceImpl.class);

    @Autowired
    private GateNodeEntityMapper gateNodeEntityMapper;

    @Override
    public PageListDto<GateNodeResponse> pageList(GateNodeSearchRequest request) {
        Page<?> page = PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<GateNodeEntity> entityList = gateNodeEntityMapper.queryAll(request);
        List<GateNodeResponse> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            list = DozerUtil.mapList(entityList, GateNodeResponse.class);
        }
        PageListDto<GateNodeResponse> pageListDto = new PageListDto<>();
        pageListDto.setTotal(page.getTotal());
        pageListDto.setRows(list);
        pageListDto.setPageNumber(request.getPageNumber());
        pageListDto.setPageSize(request.getPageSize());
        return pageListDto;
    }

    @Override
    public GateNodeResponse selectById(Long GateNodeId) {
        GateNodeResponse response = new GateNodeResponse();
        GateNodeEntity entity = gateNodeEntityMapper.selectById(GateNodeId);
        if (entity != null) {
            response = DozerUtil.map(entity, GateNodeResponse.class);
        }
        return response;
    }

    @Override
    @Transactional
    public void updateById(GateNodeUpdateRequest request) {
        if (request != null) {
            GateNodeEntity param = DozerUtil.map(request, GateNodeEntity.class);

            gateNodeEntityMapper.lock(request.getNodeId());
            gateNodeEntityMapper.updateById(param);
        }
    }

    @Override
    public void deleteById(Long nodeId) {
        gateNodeEntityMapper.deleteById(nodeId);
    }

    @Override
    @Transactional
    public void save(GateNodeSaveRequest request) {
        gateNodeEntityMapper.insert(request);
    }

    @Override
    @Transactional
    public void option(Long nodeId, String status) {
        GateNodeEntity param = new GateNodeEntity();
        param.setNodeId(nodeId);
        param.setStatus(status);
        gateNodeEntityMapper.lock(nodeId);
        gateNodeEntityMapper.updateById(param);
    }
}
