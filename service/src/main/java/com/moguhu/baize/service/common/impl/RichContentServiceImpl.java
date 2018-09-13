package com.moguhu.baize.service.common.impl;

import com.moguhu.baize.metadata.dao.mapper.common.RichContentEntityMapper;
import com.moguhu.baize.metadata.entity.common.RichContentEntity;
import com.moguhu.baize.service.common.RichContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 富文本服务
 * <p>
 * Created by xuefeihu on 18/9/13.
 */
@Service
public class RichContentServiceImpl implements RichContentService {

    @Autowired
    private RichContentEntityMapper richContentEntityMapper;


    @Override
    public RichContentEntity selectById(Long contentId) {
        return richContentEntityMapper.selectById(contentId);
    }
}
