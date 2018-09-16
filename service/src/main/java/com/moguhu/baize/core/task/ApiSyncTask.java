package com.moguhu.baize.core.task;

import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.zookeeper.ZookeeperKey;
import com.moguhu.baize.metadata.entity.api.ApiEntity;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.service.api.ApiGroupService;
import com.moguhu.baize.service.api.ApiService;
import com.moguhu.baize.service.backend.GateServiceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * API synchronize task
 * <p>
 * Created by xuefeihu on 18/9/15.
 */
public class ApiSyncTask extends AbstractSyncTask {

    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiGroupService apiGroupService;
    @Autowired
    private GateServiceService gateServiceService;

    private Long apiId;
    private String status;

    public ApiSyncTask(Long apiId, String status) {
        this.apiId = apiId;
        this.status = status;
    }

    @Override
    public Long call() throws Exception {
        logger.info("API sync task begin ...");
        long startTime = System.currentTimeMillis();
        try {
            ApiEntity api = apiService.selectById(apiId);
            if (null == api) {
                logger.warn("未找到 API实体, 同步API 结束!");
                return -1L;
            }
            ApiGroupEntity apiGroup = apiGroupService.selectById(api.getGroupId());
            if (null == apiGroup) {
                logger.warn("未找到 API分组, 同步API 结束!");
                return -1L;
            }
            GateServiceEntity gateService = gateServiceService.selectById(apiGroup.getServiceId());
            if (null == gateService) {
                logger.warn("未找到 网关服务, 同步API 结束!");
                return -1L;
            }

            String apiPath = ZookeeperKey.BAIZE_ZUUL + "/" + gateService.getServiceCode() + "/" + ZookeeperKey.SERVICECODE_APIGROUP
                    + "/" + apiGroup.getGroupId() + "/" + api.getApiId();

            if (StatusEnum.ON.name().equals(status)) {
                this.syncApi(api, apiGroup, gateService);
            } else if (StatusEnum.OFF.name().equals(status)) {
                this.deletePath(apiPath);
            }

            logger.info("API sync task execute SUCCESSFUL，apiId={}", apiId);
        } catch (Exception e) {
            logger.error("API sync task execute FAILED，apiId={}, {}", apiId, e);
        }
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        logger.info("API sync task execute END. consume: " + usedTime / 1000 + " s");
        return usedTime;
    }

}
