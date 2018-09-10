package com.moguhu.baize.service.backend;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.backend.GateServiceSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceUpdateRequest;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;

import java.util.List;

/**
 * 网关服务 管理
 *
 * @author xuefeihu
 */
public interface GateServiceService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<GateServiceResponse> pageList(GateServiceSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param serviceId
     * @return
     */
    GateServiceResponse selectById(Long serviceId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(GateServiceUpdateRequest request);

    /**
     * 删除
     *
     * @param serviceId
     */
    void deleteById(Long serviceId);

    /**
     * 新增
     *
     * @param request
     */
    void save(GateServiceSaveRequest request);

    /**
     * 停启用
     *
     * @param serviceId
     * @param status
     */
    void option(Long serviceId, String status);

    /**
     * 查询可用服务列表
     *
     * @return
     */
    List<GateServiceResponse> all();
}
