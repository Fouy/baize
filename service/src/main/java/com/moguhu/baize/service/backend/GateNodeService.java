package com.moguhu.baize.service.backend;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.metadata.response.backend.GateNodeResponse;

/**
 * 网关节点 管理
 *
 * Created by xuefeihu on 18/9/7.
 */
public interface GateNodeService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<GateNodeResponse> pageList(GateNodeSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param nodeId
     * @return
     */
    GateNodeResponse selectById(Long nodeId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(GateNodeUpdateRequest request);

    /**
     * 删除
     *
     * @param nodeId
     */
    void deleteById(Long nodeId);

    /**
     * 新增
     *
     * @param request
     */
    void save(GateNodeSaveRequest request);

    /**
     * 停启用
     *
     * @param nodeId
     * @param status
     */
    void option(Long nodeId, String status);
}
