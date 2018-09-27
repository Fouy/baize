package com.moguhu.baize.client.utils;

import com.moguhu.baize.client.constants.ZookeeperKey;

/**
 * Zookeeper Path Builder
 * <p>
 * Created by xuefeihu on 18/9/27.
 */
public class ZookeeperPathBuilder {

    /**
     * build Service path
     *
     * @param serviceCode
     * @return
     */
    public static String buildServicePath(String serviceCode) {
        if (isEmpty(serviceCode)) {
            throw new RuntimeException("serviceCode is empty");
        }
        return ZookeeperKey.BAIZE_ZUUL + "/" + serviceCode;
    }

    /**
     * build group path
     *
     * @param serviceCode
     * @param groupId
     * @return
     */
    public static String buildGroupPath(String serviceCode, Long groupId) {
        if (isEmpty(serviceCode) || null == groupId) {
            throw new RuntimeException("parameter is empty");
        }
        return buildServicePath(serviceCode) + "/" + ZookeeperKey.SERVICECODE_APIGROUP + "/" + groupId;
    }

    /**
     * build backhosts path
     *
     * @param serviceCode
     * @param hosts
     * @return
     */
    public static String buildBackhostsPath(String serviceCode, String hosts) {
        if (isEmpty(serviceCode) || isEmpty(hosts)) {
            throw new RuntimeException("parameter is empty");
        }
        return buildServicePath(serviceCode) + "/" + ZookeeperKey.SERVICECODE_BACKHOSTS + "/" + hosts;
    }

    private static boolean isEmpty(String str) {
        if (null == str || "".equals(str.replaceAll(" ", ""))) {
            return true;
        }
        return false;
    }

}
