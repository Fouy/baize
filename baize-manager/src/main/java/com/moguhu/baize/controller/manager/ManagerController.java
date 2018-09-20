package com.moguhu.baize.controller.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.moguhu.baize.client.model.ComponentDto;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.service.backend.ComponentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组件拉取
 *
 * @author xuefeihu
 */
@RestController
@RequestMapping("/manager")
public class ManagerController extends BaseController {

    @Autowired
    private ComponentService componentService;

    @RequestMapping("/getcomponent")
    @ResponseBody
    public AjaxResult getComponent(Long compId) {
        try {
            if (null == compId || compId <= 0) {
                return AjaxResult.error("参数有误");
            }

            ComponentDto component = componentService.getComponent(compId);
            return AjaxResult.success(component);
        } catch (Exception e) {
            logger.error("拉取组件出错, e={}", e);
            return AjaxResult.error("拉取组件出错");
        }
    }

    @RequestMapping("/getcomponents")
    @ResponseBody
    public AjaxResult getComponents(String paramStr) {
        try {
            if (StringUtils.isEmpty(paramStr)) {
                return AjaxResult.error("参数有误");
            }
            List<Long> compList = JSON.parseObject(paramStr, new TypeReference<List<Long>>() {
            });

            List<ComponentDto> components = componentService.getComponents(compList);
            return AjaxResult.success(components);
        } catch (Exception e) {
            logger.error("拉取组件出错, e={}", e);
            return AjaxResult.error("拉取组件出错");
        }
    }

}
