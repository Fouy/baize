package com.moguhu.baize.service.api.impl;

import com.moguhu.baize.common.constants.backend.ComponentTypeEnum;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组件转换器
 * <p>
 * Created by xuefeihu on 18/9/15.
 */
public class ComponentConvert {

    /**
     * 组件转Map
     *
     * @param componentResponses
     * @return
     */
    public static Map<String, List<ComponentResponse>> convert2Map(List<ComponentResponse> componentResponses) {
        Map<String, List<ComponentResponse>> map = new HashMap<>();
        ComponentTypeEnum[] types = ComponentTypeEnum.values();
        for (int i = 0; i < types.length; i++) {
            map.put(types[i].name(), new ArrayList<>());
        }

        componentResponses.forEach(componentResponse -> {
            List<ComponentResponse> tempList = map.get(componentResponse.getType());
            tempList.add(componentResponse);
        });
        return map;
    }

}
