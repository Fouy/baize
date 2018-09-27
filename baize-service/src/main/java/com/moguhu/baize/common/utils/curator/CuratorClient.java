package com.moguhu.baize.common.utils.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * task client
 *
 * @author xuefeihu
 */
@Component
public class CuratorClient {

    public static final String CHARSET = "UTF-8";

    @Value("${baize.registry.hosts}")
    private String hosts;

    private CuratorFramework client;

    /**
     * create client
     */
    @PostConstruct
    private void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        this.client = CuratorFrameworkFactory.builder().connectString(hosts).sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy).build();
        client.start();
    }

    /**
     * create node
     *
     * @param path
     * @param data
     * @param createMode
     */
    public void createNode(String path, String data, CreateMode createMode) {
        try {
            client.create().creatingParentsIfNeeded()
                    .withMode(createMode).withACL(Ids.OPEN_ACL_UNSAFE)
                    .forPath(path, data.getBytes(CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete node
     *
     * @param path
     */
    public void deleteNode(String path) {
        try {
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read node
     *
     * @param path
     * @return
     */
    public String readNode(String path) {
        Stat stat = new Stat();
        return this.readNode(path, stat);
    }

    /**
     * read node and stat
     *
     * @param path
     * @return
     */
    public String readNode(String path, Stat stat) {
        String result = "";
        try {
            byte[] data = client.getData().storingStatIn(stat).forPath(path);
            result = new String(data, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * update node
     *
     * @param path
     * @param data
     */
    public void updateNode(String path, String data) {
        this.updateNode(path, data, -1);
    }

    /**
     * update node
     *
     * @param path
     * @param data
     * @param version
     */
    public void updateNode(String path, String data, int version) {
        try {
            client.setData().withVersion(version).forPath(path, data.getBytes(CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get children
     *
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        Stat stat = new Stat();
        return this.getChildren(path, stat);
    }

    /**
     * get children and store stat
     *
     * @param path
     * @param stat
     * @return
     */
    public List<String> getChildren(String path, Stat stat) {
        List<String> result = new ArrayList<String>();
        try {
            result = client.getChildren().storingStatIn(stat).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * check exists
     *
     * @param path
     * @return
     */
    public boolean checkExists(String path) {
        boolean result = false;
        try {
            result = client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void closeClient() {
        if (null != client)
            this.client.close();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

}
