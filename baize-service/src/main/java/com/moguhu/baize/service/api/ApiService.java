package com.moguhu.baize.service.api;


import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiCompResponse;
import com.moguhu.baize.metadata.response.api.ApiResponse;

import java.util.List;

/**
 * API 管理
 *
 * Created by xuefeihu on 18/9/6.
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

    /**
     * 查询组件列表
     *
     * @param apiId
     * @return
     */
    ApiCompResponse complist(Long apiId);

    /**
     * 保存组件信息
     *
     * @param apiId
     * @param compIds
     */
    void savecomp(Long apiId, String compIds);

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<ApiResponse> all(ApiSearchRequest request);
}
