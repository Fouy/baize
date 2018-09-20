package com.moguhu.baize.controller.backend;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateServiceUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import com.moguhu.baize.service.backend.GateServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 网关服务管理
 * <p>
 * Created by xuefeihu on 18/9/8.
 */
@RestController
@RequestMapping("/gateservice")
public class GateServiceController extends BaseController {

    @Autowired
    private GateServiceService gateServiceService;

    @Autowired
    private ApiGroupService apiGroupService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("gateservice/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("gateservice/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("gateservice/edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<GateServiceResponse> list(GateServiceSearchRequest request) {
        PageListDto<GateServiceResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }

            result = gateServiceService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/all")
    @ResponseBody
    public AjaxResult all() {
        try {
            List<GateServiceResponse> list = gateServiceService.all();
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("查询全部列表, e={}", e);
            return AjaxResult.error("查询全部列表");
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long serviceId) {
        try {
            if (null == serviceId) {
                return AjaxResult.error("参数有误");
            }
            GateServiceResponse GateServiceResponse = gateServiceService.selectById(serviceId);
            return AjaxResult.success(GateServiceResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, serviceId = {}, e={}", serviceId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(GateServiceSaveRequest request) {
        try {
            if (StringUtils.isEmpty(request.getName()) || StringUtils.isEmpty(request.getServiceCode())) {
                return AjaxResult.error("参数有误");
            }

            gateServiceService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(GateServiceUpdateRequest request) {
        try {
            if (null == request.getServiceId()) {
                return AjaxResult.error("参数有误");
            }

            gateServiceService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long serviceId) {
        try {
            if (null == serviceId) {
                return AjaxResult.error("参数有误");
            }
            GateServiceResponse gateServiceEntity = gateServiceService.selectById(serviceId);
            if (null == gateServiceEntity) {
                return AjaxResult.error("服务不存在");
            }
            if (StatusEnum.ON.name().equals(gateServiceEntity.getStatus())) {
                return AjaxResult.error("服务启用中, 不可删除");
            }
            // 关联 Group 检查
            ApiGroupSearchRequest param = new ApiGroupSearchRequest();
            param.setServiceId(serviceId);
            List<ApiGroupResponse> apiGroupList = apiGroupService.queryAll(param);
            if (!CollectionUtils.isEmpty(apiGroupList)) {
                return AjaxResult.error("有分组关联, 不可删除");
            }

            gateServiceService.deleteById(serviceId);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, serviceId={}, e={}", JSON.toJSONString(serviceId), e);
            return AjaxResult.error("删除信息失败");
        }
    }

    @RequestMapping("/option")
    @ResponseBody
    public AjaxResult option(Long serviceId, String status) {
        try {
            if (StatusEnum.resolve(status) == null || null == serviceId) {
                return AjaxResult.error("参数有误");
            }
            GateServiceResponse gateServiceEntity = gateServiceService.selectById(serviceId);
            if (null == gateServiceEntity) {
                return AjaxResult.error("服务不存在");
            }

            gateServiceService.option(serviceId, status);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("停启用失败, serviceId={}, e={}", serviceId, e);
            return AjaxResult.error("停启用失败");
        }
    }

}
