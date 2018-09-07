package com.moguhu.baize.metadata.request.backend;

import com.moguhu.baize.common.constants.backend.NodeStatusEnum;
import com.moguhu.baize.metadata.request.BasePageRequest;

/**
 * 网关节点 分页查询请求
 * <p>
 * Created by xuefeihu on 18/9/7.
 */
public class GateNodeSearchRequest extends BasePageRequest {

    /**
     * 机房名称.
     */
    private String engineRoom;

    /**
     * 节点编码.
     */
    private String nodeNo;

    /**
     * 状态：已创建 CREATED 收集中 COLLECTING 运行中 RUNNING  已关闭 CLOSED.
     *
     * @see NodeStatusEnum
     */
    private String status;

    public String getEngineRoom() {
        return engineRoom;
    }

    public void setEngineRoom(String engineRoom) {
        this.engineRoom = engineRoom;
    }

    public String getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(String nodeNo) {
        this.nodeNo = nodeNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
