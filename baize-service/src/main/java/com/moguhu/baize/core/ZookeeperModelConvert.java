package com.moguhu.baize.core;

import com.moguhu.baize.client.model.ApiParamDto;
import com.moguhu.baize.client.model.ApiParamMapDto;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Zookeeper Model Convert
 * <p>
 * Created by xuefeihu on 18/9/20.
 */
@Component
public class ZookeeperModelConvert {


    public List<ApiParamDto> convertParams(List<ApiParamResponse> apiParamList) {
        return null;
    }

    public List<ApiParamMapDto> convertMappings(List<ApiParamMapResponse> apiParamMapList) {
        return null;
    }
}
