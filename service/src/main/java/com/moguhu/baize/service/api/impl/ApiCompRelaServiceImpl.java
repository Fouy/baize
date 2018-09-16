package com.moguhu.baize.service.api.impl;

import com.moguhu.baize.metadata.dao.mapper.api.ApiCompRelaEntityMapper;
import com.moguhu.baize.metadata.entity.api.ApiCompRelaEntity;
import com.moguhu.baize.service.api.ApiCompRelaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * API-组件关系
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
@Service
public class ApiCompRelaServiceImpl implements ApiCompRelaService {

    private static final Logger logger = LoggerFactory.getLogger(ApiCompRelaServiceImpl.class);

    @Autowired
    private ApiCompRelaEntityMapper apiCompRelaEntityMapper;


    @Override
    public List<ApiCompRelaEntity> all(ApiCompRelaEntity request) {
        return apiCompRelaEntityMapper.queryAll(request);
    }
}
