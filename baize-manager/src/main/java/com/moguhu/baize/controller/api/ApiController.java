package com.moguhu.baize.controller.api;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.client.constants.BooleanEnum;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.api.ApiSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiCompResponse;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;
import com.moguhu.baize.metadata.response.api.ApiResponse;
import com.moguhu.baize.metadata.response.backend.GateServiceResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import com.moguhu.baize.service.api.ApiService;
import com.moguhu.baize.service.backend.GateServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * API管理
 *
 * Created by xuefeihu on 18/9/6.
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiGroupService apiGroupService;

    @Autowired
    private GateServiceService gateServiceService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("api/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("api/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("api/edit");
        return mav;
    }

    @RequestMapping("/compconfig")
    public ModelAndView compconfig() {
        ModelAndView mav = new ModelAndView("api/comp-config");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<ApiResponse> list(ApiSearchRequest request) {
        PageListDto<ApiResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }

            result = apiService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long apiId) {
        try {
            if (null == apiId) {
                return AjaxResult.error("参数有误");
            }
            ApiResponse apiResponse = apiService.selectById(apiId);
            return AjaxResult.success(apiResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, apiId = {}, e={}", apiId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(ApiSaveRequest request) {
        try {
            if (StringUtils.isEmpty(request.getName()) || null == request.getGroupId() || StringUtils.isEmpty(request.getPath())
                    || StringUtils.isEmpty(request.getMethods()) || BooleanEnum.resolve(request.getCached()) == null
                    || StringUtils.isEmpty(request.getProtocol()) || StringUtils.isEmpty(request.getVersion())) {
                return AjaxResult.error("参数有误");
            }

            apiService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(ApiUpdateRequest request) {
        try {
            if (null == request.getApiId()) {
                return AjaxResult.error("参数有误");
            }

            apiService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新失败");
        }
    }

    @RequestMapping("/option")
    @ResponseBody
    public AjaxResult option(Long apiId, String status) {
        try {
            if (StatusEnum.resolve(status) == null || null == apiId) {
                return AjaxResult.error("参数有误");
            }
            ApiResponse apiResponse = apiService.selectById(apiId);
            if (null == apiResponse) {
                return AjaxResult.error("API不存在");
            }
            ApiGroupResponse apiGroup = apiGroupService.selectById(apiResponse.getGroupId());
            if (apiGroup == null) {
                return AjaxResult.error("API分组不存在");
            }
            // 检查依赖发Gate Service 是否启用
            if (StatusEnum.ON.name().equals(status)) {
                GateServiceResponse gateService = gateServiceService.selectById(apiGroup.getServiceId());
                if (gateService == null || StatusEnum.OFF.name().equals(gateService.getStatus())) {
                    return AjaxResult.error("依赖的Gate Service未上线!");
                }
            }

            apiService.option(apiId, status);
            return AjaxResult.success("操作成功", null);
        } catch (Exception e) {
            logger.error("停启用失败, apiId={}, e={}", apiId, e);
            return AjaxResult.error("停启用失败");
        }
    }

    @RequestMapping("/complist")
    @ResponseBody
    public AjaxResult complist(Long apiId) {
        try {
            if (null == apiId) {
                return AjaxResult.error("参数有误");
            }

            ApiCompResponse response = apiService.complist(apiId);
            return AjaxResult.success(response);
        } catch (Exception e) {
            logger.error("查询组件列表失败, apiId = {}, e={}", apiId, e);
            return AjaxResult.error("查询组件列表失败");
        }
    }


    @RequestMapping("/savecomp")
    @ResponseBody
    public AjaxResult savecomp(Long apiId, String compIds) {
        try {
            if (null == apiId) {
                return AjaxResult.error("参数有误");
            }

            apiService.savecomp(apiId, compIds);
            return AjaxResult.success("保存成功", null);
        } catch (Exception e) {
            logger.error("保存失败, e={}", e);
            return AjaxResult.error("保存失败");
        }
    }

}
