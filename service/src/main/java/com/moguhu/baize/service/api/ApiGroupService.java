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
 * <p>
 * Created by xuefeihu on 18/9/6.
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
     * 将Group 下的 已启用的API 同步至 ZooKeeper
     *
     * @param groupId
     */
    void syncZookeeper(Long groupId);

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

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<ApiGroupResponse> queryAll(ApiGroupSearchRequest request);
}
