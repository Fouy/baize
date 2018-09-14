package com.moguhu.baize.service.api;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiGroupCompResponse;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;

import java.util.List;

/**
 * API分组 管理
 *
 * @author xuefeihu
 */
public interface ApiGroupService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<ApiGroupResponse> pageList(ApiGroupSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param groupId
     * @return
     */
    ApiGroupResponse selectById(Long groupId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(ApiGroupUpdateRequest request);

    /**
     * 删除
     *
     * @param groupId
     */
    void deleteById(Long groupId);

    /**
     * 新增
     *
     * @param request
     */
    void save(ApiGroupSaveRequest request);

    /**
     * 停启用
     *
     * @param groupId
     * @param status
     */
    void option(Long groupId, String status);

    /**
     * 查询所有 API GROUP
     *
     * @return
     */
    List<ApiGroupResponse> all();

    /**
     * 查询组件列表
     *
     * @param groupId
     * @return
     */
    ApiGroupCompResponse complist(Long groupId);

    /**
     * 保存组件
     *
     * @param groupId
     * @param compIds
     */
    void savecomp(Long groupId, String compIds);
}
