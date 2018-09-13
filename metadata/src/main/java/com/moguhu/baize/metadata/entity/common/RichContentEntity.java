package com.moguhu.baize.metadata.entity.common;

import com.moguhu.baize.common.constants.content.RichContentTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class RichContentEntity implements Serializable {
    /**
     * 主键ID.
     */
    private Long contentId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 类型：groovy脚本  GROOVY.
     *
     * @see RichContentTypeEnum
     */
    private String type;

    /**
     * 内容.
     */
    private String content;

    private static final long serialVersionUID = 1L;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}