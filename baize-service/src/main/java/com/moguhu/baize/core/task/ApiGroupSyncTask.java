package com.moguhu.baize.core.task;

import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import com.moguhu.baize.service.api.ApiService;
import com.moguhu.baize.service.backend.GateServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * API Group synchronize task
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
public class ApiGroupSyncTask extends AbstractSyncTask {

    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiGroupService apiGroupService;
    @Autowired
    private GateServiceService gateServiceService;

    private Long groupId;

    public ApiGroupSyncTask(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public Long call() throws Exception {
        logger.info("API Group sync task begin ...");
        long startTime = System.currentTimeMillis();
        try {
            ApiGroupEntity apiGroup = apiGroupService.selectById(groupId);
            if (null == apiGroup) {
                logger.warn("未找到 API分组, 同步API Group 结束!");
                return -1L;
            }
            GateServiceEntity gateService = gateServiceService.selectById(apiGroup.getServiceId());
            if (null == gateService) {
                logger.warn("未找到 网关服务, 同步API Group 结束!");
                return -1L;
            }

            ApiSearchRequest param = new ApiSearchRequest();
            param.setGroupId(groupId);
            List<ApiResponse> apiList = apiService.all(param);
            if (!CollectionUtils.isEmpty(apiList)) {
                apiList.forEach(api -> this.syncApi(api, apiGroup, gateService));
            }

            logger.info("API Group sync task execute SUCCESSFUL，groupId={}", groupId);
        } catch (Exception e) {
            logger.error("API Group sync task execute FAILED，groupId={}, {}", groupId, e);
        }
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        logger.info("API Group sync task execute END. consume: " + usedTime / 1000 + " s");
        return usedTime;
    }

}
