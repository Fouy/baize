package com.moguhu.baize.service.api;


import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.common.vo.PageListDto;

/**
 * API 管理
 *
 * @author xuefeihu
 */
public interface ApiService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<ApiResponse> pageList(ApiSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param apiId
     * @return
     */
    ApiResponse selectById(Long apiId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(ApiUpdateRequest request);

    /**
     * 删除
     *
     * @param apiId
     */
    void deleteById(Long apiId);

    /**
     * 新增
     *
     * @param request
     */
    void save(ApiSaveRequest request);

    /**
     * 停启用
     *
     * @param apiId
     * @param status
     */
    void option(Long apiId, String status);
}
