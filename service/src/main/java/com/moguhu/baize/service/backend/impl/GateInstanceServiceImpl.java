package com.moguhu.baize.service.backend.impl;

import com.moguhu.baize.common.constants.zookeeper.ZookeeperKey;
import com.moguhu.baize.common.utils.curator.CuratorClient;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.dao.mapper.backend.GateServiceEntityMapper;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.response.backend.GateInstanceResponse;
import com.moguhu.baize.service.backend.GateInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关实例 管理
 *
 * Created by xuefeihu on 18/9/9.
 */
@Service
public class GateInstanceServiceImpl implements GateInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(GateInstanceServiceImpl.class);

    @Autowired
    private CuratorClient curatorClient;

    @Autowired
    private GateServiceEntityMapper gateServiceEntityMapper;

    @Override
    public PageListDto<GateInstanceResponse> pageList(Long serviceId) {
        PageListDto<GateInstanceResponse> pageListDto = new PageListDto<>();

        GateServiceEntity gateServiceEntity = gateServiceEntityMapper.selectById(serviceId);
        if (null == gateServiceEntity) {
            return pageListDto;
        }

        String path = ZookeeperKey.BAIZE_ZUUL + "/" + gateServiceEntity.getServiceCode() + "/" + ZookeeperKey.SERVICECODE_NODES;
        if (!curatorClient.checkExists(path)) {
            return pageListDto;
        }
        List<String> instanceList = curatorClient.getChildren(path);
        if (!CollectionUtils.isEmpty(instanceList)) {
            List<GateInstanceResponse> rows = new ArrayList<>();
            instanceList.forEach(instance -> {
                GateInstanceResponse instanceResponse = new GateInstanceResponse();
                instanceResponse.setName(gateServiceEntity.getName());
                instanceResponse.setServiceCode(gateServiceEntity.getServiceCode());
                instanceResponse.setHost(instance);
                rows.add(instanceResponse);
            });
            pageListDto.setRows(rows);
        }

        return pageListDto;
    }

}
