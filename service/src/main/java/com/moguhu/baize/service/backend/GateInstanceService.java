package com.moguhu.baize.service.backend;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.response.backend.GateInstanceResponse;

/**
 * 网关实例 管理
 *
 * Created by xuefeihu on 18/9/9.
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
