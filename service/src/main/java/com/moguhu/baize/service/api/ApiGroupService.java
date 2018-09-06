package com.moguhu.baize.service.api;


import com.moguhu.baize.common.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.common.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.common.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.common.response.api.ApiGroupResponse;
import com.moguhu.baize.common.vo.PageListDto;

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
}
