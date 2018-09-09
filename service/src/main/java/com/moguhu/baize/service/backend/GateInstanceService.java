package com.moguhu.baize.service.backend;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.response.backend.GateInstanceResponse;

/**
 * 网关实例 管理
 *
 * @author xuefeihu
 */
public interface GateInstanceService {

    /**
     * 列表查询
     *
     * @param serviceId
     * @return
     */
    PageListDto<GateInstanceResponse> pageList(Long serviceId);

}
