package com.moguhu.baize.core.task;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.client.constants.ZookeeperKey;
import com.moguhu.baize.client.model.ApiDto;
import com.moguhu.baize.client.model.ApiGroupDto;
import com.moguhu.baize.client.utils.ZookeeperPathBuilder;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.utils.curator.CuratorClient;
import com.moguhu.baize.core.ZookeeperModelConvert;
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
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private ZookeeperModelConvert modelConvert;

    /**
     * synchronize Gate Service, it will delete oldService if needed.
     */
    protected void syncService(GateServiceEntity gateService, String oldService) {
        // 删除原服务
        if (StringUtils.isNoneEmpty(oldService)) {
            String oldServicePath = ZookeeperPathBuilder.buildServicePath(oldService);
            if (client.checkExists(oldServicePath)) {
                client.deleteNode(oldServicePath);
            }
        }

        String servicePath = ZookeeperPathBuilder.buildServicePath(gateService.getServiceCode());

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
                        String backHostsPath = ZookeeperPathBuilder.buildBackhostsPath(gateService.getServiceCode(), gateService.getHosts());
                        if (!client.checkExists(backHostsPath)) {
                            client.createNode(backHostsPath, "", CreateMode.PERSISTENT);
                        }
                    } catch (Exception e) {
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

        String apiGroupPath = ZookeeperPathBuilder.buildGroupPath(gateService.getServiceCode(), apiGroup.getGroupId());
        String apiPath = apiGroupPath + "/" + api.getApiId();

        if (StatusEnum.ON.name().equals(api.getStatus())) {
            // 准备 ZK 参数
            List<Long> compIds = componentService.queryByApi(api.getApiId());
            ApiParamSearchRequest param = new ApiParamSearchRequest();
            param.setApiId(api.getApiId());
            List<ApiParamResponse> apiParamList = apiParamService.all(param);
            ApiParamMapSearchRequest param1 = new ApiParamMapSearchRequest();
            param1.setApiId(api.getApiId());
            List<ApiParamMapResponse> apiParamMapList = apiParamMapService.all(param1);

            ApiDto apiDto = new ApiDto();
            apiDto.setCompIds(compIds);
            apiDto.setParams(modelConvert.convertParams(apiParamList));
            apiDto.setMappings(modelConvert.convertMappings(apiParamMapList));
            modelConvert.convertApi(apiDto, api);
            String apiStr = JSON.toJSONString(apiDto);

            if (!client.checkExists(apiPath)) {
                try {
                    client.createNode(apiPath, apiStr, CreateMode.PERSISTENT);
                } catch (Exception e) {
                    logger.info(" /baize/zuul/${serviceCode}/apigroup/${group}/${api} node has exists. ");
                    // do nothing
                }
            } else {
                client.updateNode(apiPath, apiStr);
            }
        } else if (StatusEnum.OFF.name().equals(api.getStatus()) && client.checkExists(apiPath)) {
            client.deleteNode(apiPath);
        }
    }

    /**
     * synchronize Group info only
     *
     * @param gateService
     * @param apiGroup
     * @return apiGroupPath
     */
    protected void syncGroupOnly(GateServiceEntity gateService, ApiGroupEntity apiGroup) {
        String apiGroupPath = ZookeeperPathBuilder.buildGroupPath(gateService.getServiceCode(), apiGroup.getGroupId());
        // 准备Group信息
        List<Long> compIds = componentService.queryByApiGroup(apiGroup.getGroupId());
        ApiGroupDto apiGroupDto = modelConvert.convertApiGroup(apiGroup, compIds);
        String apiGroupStr = JSON.toJSONString(apiGroupDto);

        if (!client.checkExists(apiGroupPath)) {
            try {
                client.createNode(apiGroupPath, apiGroupStr, CreateMode.PERSISTENT);
            } catch (Exception e) {
                logger.warn(" /baize/zuul/${serviceCode}/apigroup/${group} node has exists. ");
                // do nothing
            }
        } else {
            client.updateNode(apiGroupPath, apiGroupStr);
        }
    }


}
