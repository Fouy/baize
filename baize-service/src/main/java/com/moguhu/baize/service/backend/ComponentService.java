package com.moguhu.baize.service.backend;


import com.moguhu.baize.client.model.ComponentDto;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.metadata.request.backend.ComponentSaveRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.request.backend.ComponentUpdateRequest;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;

import java.util.List;

/**
 * 组件 管理
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
public interface ComponentService {

    /**
     * 分页列表查询
     *
     * @param request
     * @return
     */
    PageListDto<ComponentResponse> pageList(ComponentSearchRequest request);

    /**
     * 根据ID查询
     *
     * @param compId
     * @return
     */
    ComponentResponse selectById(Long compId);

    /**
     * 更新
     *
     * @param request
     */
    void updateById(ComponentUpdateRequest request);

    /**
     * 删除
     *
     * @param compId
     */
    void deleteById(Long compId);

    /**
     * 新增
     *
     * @param request
     */
    void save(ComponentSaveRequest request);

    /**
     * 停启用
     *
     * @param compId
     * @param status
     */
    void option(Long compId, String status);

    /**
     * 查询可用服务列表
     *
     * @return
     */
    List<ComponentResponse> all();

    /**
     * 根据 groupId 查询组件
     *
     * @param groupId
     * @return
     */
    List<Long> queryByApiGroup(Long groupId);

    /**
     * 根据 apiId 查询组件
     *
     * @param apiId
     * @return
     */
    List<Long> queryByApi(Long apiId);

    /**
     * Gateway 拉取组件
     *
     * @param compId
     * @return
     */
    ComponentDto getComponent(Long compId);

    /**
     * GateWay 拉取组件列表
     *
     * @param compList
     * @return
     */
    List<ComponentDto> getComponents(List<Long> compList);
}
