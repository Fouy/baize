package com.moguhu.baize.service.common;

import com.moguhu.baize.metadata.entity.common.RichContentEntity;

/**
 * 富文本服务
 * <p>
 * Created by xuefeihu on 18/9/13.
 */
public interface RichContentService {

    /**
     * 根据ID查询
     *
     * @param contentId
     * @return
     */
    RichContentEntity selectById(Long contentId);

}
