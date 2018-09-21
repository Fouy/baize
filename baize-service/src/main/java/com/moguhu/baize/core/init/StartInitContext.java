package com.moguhu.baize.core.init;

import com.moguhu.baize.client.constants.ZookeeperKey;
import com.moguhu.baize.common.utils.curator.CuratorClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * init context
 * <p>
 * Created by xuefeihu on 18/9/8.
 */
@Component
public class StartInitContext {

    private static final Logger logger = LoggerFactory.getLogger(StartInitContext.class);

    @Autowired
    private CuratorClient client;

    private String ip;

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public Long init() throws Exception {
        logger.info("init context begin ...");
        long startTime = System.currentTimeMillis();
        try {
            init0();

            if (!client.checkExists(ZookeeperKey.BAIZE)) {
                try {
                    // 初始化根目录
                    client.createNode(ZookeeperKey.BAIZE, "", CreateMode.PERSISTENT);

                    List<String> children = client.getChildren(ZookeeperKey.BAIZE);
                    if (CollectionUtils.isEmpty(children)) {
                        client.createNode(ZookeeperKey.BAIZE_ZUUL, "", CreateMode.PERSISTENT);
                        client.createNode(ZookeeperKey.BAIZE_MANAGER, "", CreateMode.PERSISTENT);
                    }
                    logger.info("Zookeeper 根目录已创建.");
                } catch (Exception e) {
                    logger.info(" /baize node has exists. ");
                    // do nothing
                }
            }

            // 注册manager
            if (!client.checkExists(ZookeeperKey.BAIZE_MANAGER_NODES)) {
                try {
                    client.createNode(ZookeeperKey.BAIZE_MANAGER_NODES, "", CreateMode.PERSISTENT);
                    logger.info(" /baize/manager/nodes 目录已创建.");
                } catch (Exception e) {
                    logger.info(" /baize/manager/nodes has exists. ");
                }
            }

            try {
                client.createNode(ZookeeperKey.BAIZE_MANAGER_NODES + "/" + ip + ":" + port, "", CreateMode.EPHEMERAL);
                logger.info(" Manager: ip:{}, port:{}, 注册成功.", ip, port);
            } catch (Exception e) {
                logger.error("Manager 注册ZK 失败, 停止启动, e={}", e);
                System.exit(-1);
            }

            logger.info("init context execute SUCCESSFUL.");
        } catch (Exception e) {
            logger.error("init context execute FAILED, {}", e);
        }
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        logger.info("init context execute END. consume: " + usedTime / 1000 + " s");
        return usedTime;
    }

    /**
     * init ip and port
     */
    private void init0() {
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
        }
        ip = localHost.getHostAddress();
    }

}
