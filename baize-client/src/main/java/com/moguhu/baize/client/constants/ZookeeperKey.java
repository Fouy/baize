package com.moguhu.baize.client.constants;

/**
 * ZooKeeper key
 *
 * Created by xuefeihu on 18/9/8.
 *
 */
public class ZookeeperKey {

    public static final String BAIZE = "/baize";

    public static final String BAIZE_ZUUL = BAIZE + "/zuul";


    // /baize/zuul/${serviceCode} 下节点
    public static final String SERVICECODE_NODES = "nodes";
    public static final String SERVICECODE_APIGROUP = "apigroup";
    public static final String SERVICECODE_BACKHOSTS = "backhosts";


    public static final String BAIZE_MANAGER = BAIZE + "/manager";

    public static final String BAIZE_MANAGER_NODES = BAIZE_MANAGER + "/nodes";



}
