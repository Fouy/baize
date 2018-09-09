package com.moguhu.baize.core.task;

import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.zookeeper.ZookeeperKey;
import com.moguhu.baize.common.utils.curator.CuratorClient;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.backend.GateServiceService;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * single gate service 同步任务
 * <p>
 * Created by xuefeihu on 18/9/8.
 */
public class SingleServiceSyncTask implements Callable<Long> {

    private static final Logger logger = LoggerFactory.getLogger(SingleServiceSyncTask.class);

    private static final Set<String> serviceChildSet = new HashSet<>();

    static {
        serviceChildSet.add(ZookeeperKey.SERVICECODE_NODES);
        serviceChildSet.add(ZookeeperKey.SERVICECODE_APIGROUP);
        serviceChildSet.add(ZookeeperKey.SERVICECODE_COMPONENT);
        serviceChildSet.add(ZookeeperKey.SERVICECODE_REFRESH);
    }

    @Autowired
    private CuratorClient client;

    @Autowired
    private GateServiceService gateServiceService;

    private Long serviceId;
    private String status;

    /**
     * service zk path
     */
    private String servicePath;

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
            servicePath = ZookeeperKey.BAIZE_ZUUL + "/" + gateService.getServiceCode();

            if (StatusEnum.ON.name().equals(status)) {
                this.push2Zk();
            } else if (StatusEnum.OFF.name().equals(status)) {
                this.deletePath(servicePath);
            }

            logger.info("single gate service sync task execute SUCCESSFUL，serviceId={}", serviceId);
        } catch (Exception e) {
            logger.error("single gate service sync task execute FAILED，serviceId={}, {}", serviceId, e);
        }
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        logger.info("single gate service sync task execute END. consume: " + usedTime / 1000 + " s");
        return usedTime;
    }

    /**
     * 从ZK删除
     *
     * @param path
     */
    private void deletePath(String path) {
        if (client.checkExists(path)) {
            List<String> children = client.getChildren(path);
            if (CollectionUtils.isEmpty(children)) {
                return;
            }
            children.forEach(child -> {
                deletePath(path + "/" + child);
            });
            client.deleteNode(path);
        }
    }

    /**
     * 推送至ZK
     */
    private void push2Zk() {
        if (!client.checkExists(servicePath)) {
            try {
                client.createNode(servicePath, "", CreateMode.PERSISTENT);
            } catch (Exception e) {
                logger.info(" /baize node has exists. ");
                // do nothing
            }
        }
        // 创建 /baize/zuul/${serviceCode} 下的 nodes apigroup component refresh
        List<String> serviceChild = client.getChildren(servicePath);
        if (CollectionUtils.isEmpty(serviceChild)) {
            serviceChildSet.forEach(childNode -> {
                if (!serviceChild.contains(childNode)) {
                    client.createNode(servicePath + "/" + childNode, "", CreateMode.PERSISTENT);
                }
            });
        }

    }

}
