package com.moguhu.baize.service.api.impl;

import com.moguhu.baize.metadata.dao.mapper.api.GroupCompRelaEntityMapper;
import com.moguhu.baize.metadata.entity.api.GroupCompRelaEntity;
import com.moguhu.baize.service.api.GroupCompRelaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * API Group-组件关系
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
@Service
public class GroupCompRelaServiceImpl implements GroupCompRelaService {

    private static final Logger logger = LoggerFactory.getLogger(GroupCompRelaServiceImpl.class);

    @Autowired
    private GroupCompRelaEntityMapper groupCompRelaEntityMapper;


    @Override
    public List<GroupCompRelaEntity> all(GroupCompRelaEntity request) {
        return groupCompRelaEntityMapper.queryAll(request);
    }
}
