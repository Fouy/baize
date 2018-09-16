package com.moguhu.baize.core.task;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.constants.zookeeper.ZookeeperKey;
import com.moguhu.baize.common.utils.curator.CuratorClient;
import com.moguhu.baize.core.zookeeper.model.ApiZkStorage;
import com.moguhu.baize.metadata.entity.api.ApiEntity;
import com.moguhu.baize.metadata.entity.api.ApiGroupEntity;
import com.moguhu.baize.metadata.entity.backend.GateServiceEntity;
import com.moguhu.baize.metadata.request.api.ApiParamMapSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamSearchRequest;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import com.moguhu.baize.service.api.ApiParamMapService;
import com.moguhu.baize.service.api.ApiParamService;
import com.moguhu.baize.service.backend.ComponentService;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Abstract Synchronize Task
 * <p>
 * Created by xuefeihu on 18/9/16.
 */
public abstract class AbstractSyncTask implements Callable<Long> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final Set<String> serviceChildSet = new HashSet<>();

    static {
        serviceChildSet.add(ZookeeperKey.SERVICECODE_NODES);
        serviceChildSet.add(ZookeeperKey.SERVICECODE_APIGROUP);
        serviceChildSet.add(ZookeeperKey.SERVICECODE_BACKHOSTS);
    }

    @Autowired
    protected CuratorClient client;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ApiParamService apiParamService;
    @Autowired
    private ApiParamMapService apiParamMapService;

    /**
     * Gate service zookeeper path
     */
    private String servicePath;
    /**
     * Gate service entity
     */
    private GateServiceEntity gateService;
    /**
     * API zookeeper path
     */
    private String apiPath;
    /**
     * API entity
     */
    private ApiEntity api;
    /**
     * API Group zookeeper path
     */
    private String apiGroupPath;
    /**
     * API Group entity
     */
    private ApiGroupEntity apiGroup;

    /**
     * delete path from zookeeper (contains children paths)
     *
     * @param path
     */
    protected void deletePath(String path) {
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
     * synchronize Gate Service
     */
    protected void syncService(GateServiceEntity gateService) {
        this.gateService = gateService;
        this.servicePath = ZookeeperKey.BAIZE_ZUUL + "/" + gateService.getServiceCode();

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
                // backhosts 写入后端服务HOSTS
                if (ZookeeperKey.SERVICECODE_BACKHOSTS.equals(childNode)) {
                    try {
                        String backHostsPath = servicePath + "/" + childNode + "/" + URLEncoder.encode(gateService.getHosts(), "UTF-8");
                        if (!client.checkExists(backHostsPath)) {
                            client.createNode(backHostsPath, "", CreateMode.PERSISTENT);
                        }
                    } catch (UnsupportedEncodingException e) {
                        logger.error("创建 backhosts 失败, e={}", e);
                        throw new RuntimeException("create backhosts failed", e);
                    }
                }
            });
        }
    }

    /**
     * synchronize API
     * <p>
     * this method will synchronize GateService first, and then synchronize Group info (group only).
     */
    protected void syncApi(ApiEntity api, ApiGroupEntity apiGroup, GateServiceEntity gateService) {
        this.api = api;
        this.apiGroup = apiGroup;
        this.gateService = gateService;

        // first synchronize GateService info
        this.syncService(gateService);

        // then synchronize only Group info
        apiGroupPath = this.syncGroupOnly(apiGroup);
        apiPath = apiGroupPath + "/" + api.getApiId();

        if (!client.checkExists(apiPath)) {
            try {
                List<Long> compIds = componentService.queryByApi(api.getApiId());
                ApiParamSearchRequest param = new ApiParamSearchRequest();
                param.setApiId(api.getApiId());
                List<ApiParamResponse> apiParamList = apiParamService.all(param);
                ApiParamMapSearchRequest param1 = new ApiParamMapSearchRequest();
                param1.setApiId(api.getApiId());
                List<ApiParamMapResponse> apiParamMapList = apiParamMapService.all(param1);

                ApiZkStorage apiZkStorage = new ApiZkStorage();
                apiZkStorage.setCompIds(compIds);
                apiZkStorage.setParams(apiParamList);
                apiZkStorage.setMappings(apiParamMapList);
                String storageStr = URLEncoder.encode(JSON.toJSONString(apiZkStorage), "UTF-8");

                client.createNode(servicePath, storageStr, CreateMode.PERSISTENT);
            } catch (Exception e) {
                logger.info(" /baize/zuul/${serviceCode}/apigroup/${group}/${api} node has exists. ");
                // do nothing
            }
        }
    }

    /**
     * synchronize Group info only
     *
     * @param apiGroup
     * @return apiGroupPath
     */
    private String syncGroupOnly(ApiGroupEntity apiGroup) {
        this.apiGroup = apiGroup;

        apiGroupPath = ZookeeperKey.BAIZE_ZUUL + "/" + gateService.getServiceCode() + "/" + ZookeeperKey.SERVICECODE_APIGROUP + "/" + apiGroup.getGroupId();
        if (!client.checkExists(apiGroupPath)) {
            try {
                // 存入 Group 组件信息
                List<Long> compIds = componentService.queryByApiGroup(apiGroup.getGroupId());
                String compIdsStr = URLEncoder.encode(JSON.toJSONString(compIds), "UTF-8");
                client.createNode(apiGroupPath, compIdsStr, CreateMode.PERSISTENT);
            } catch (Exception e) {
                logger.warn(" /baize/zuul/${serviceCode}/apigroup/${group} node has exists. ");
                // do nothing
            }
        }
        return apiGroupPath;
    }


}
