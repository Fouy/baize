package com.moguhu.baize.core.task;

import com.moguhu.baize.client.constants.ZookeeperKey;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.backend.GateServiceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * single GateService synchronize task
 * <p>
 * Created by xuefeihu on 18/9/8.
 */
public class SingleServiceSyncTask extends AbstractSyncTask {

    @Autowired
    private GateServiceService gateServiceService;

    private Long serviceId;
    private String status;

    public SingleServiceSyncTask(Long serviceId, String status) {
        this.serviceId = serviceId;
        this.status = status;
    }

    @Override
    public Long call() throws Exception {
        logger.info("single gate service sync task begin ...");
        long startTime = System.currentTimeMillis();
        try {
            GateServiceResponse gateService = gateServiceService.selectById(serviceId);
            String servicePath = ZookeeperKey.BAIZE_ZUUL + "/" + gateService.getServiceCode();

            if (StatusEnum.ON.name().equals(status)) {
                this.syncService(gateService);
            } else if (StatusEnum.OFF.name().equals(status)) {
                this.deletePath(servicePath);
            }

            logger.info("single GateService sync task execute SUCCESSFUL，serviceId={}", serviceId);
        } catch (Exception e) {
            logger.error("single GateService sync task execute FAILED，serviceId={}, {}", serviceId, e);
        }
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        logger.info("single GateService sync task execute END. consume: " + usedTime / 1000 + " s");
        return usedTime;
    }

}
