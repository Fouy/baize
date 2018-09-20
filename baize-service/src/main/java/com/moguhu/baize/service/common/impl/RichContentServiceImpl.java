package com.moguhu.baize.service.common.impl;

import com.moguhu.baize.metadata.mapper.common.RichContentEntityMapper;
import com.moguhu.baize.metadata.entity.common.RichContentEntity;
import com.moguhu.baize.service.common.RichContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void updateById(RichContentEntity record) {
        richContentEntityMapper.lock(record.getContentId());
        int count = richContentEntityMapper.updateById(record);
        if (count != 1) {
            throw new RuntimeException("wrong effected rows");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long contentId) {
        int count = richContentEntityMapper.deleteById(contentId);
        if (count != 1) {
            throw new RuntimeException("wrong effected rows");
        }
    }

    @Override
    public void insert(RichContentEntity richContent) {
        richContentEntityMapper.insert(richContent);
    }

}
