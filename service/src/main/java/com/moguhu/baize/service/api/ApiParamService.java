package com.moguhu.baize.service.api;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.api.*;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import com.moguhu.baize.metadata.response.api.ApiResponse;

/**
 * API Param 管理
 *
 * @author xuefeihu
 */
public interface ApiParamService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<ApiParamResponse> pageList(ApiParamSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param paramId
     * @return
     */
    ApiParamResponse selectById(Long paramId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(ApiParamUpdateRequest request);

    /**
     * 删除
     *
     * @param paramId
     */
    void deleteById(Long paramId);

    /**
     * 新增
     *
     * @param request
     */
    void save(ApiParamSaveRequest request);

}
