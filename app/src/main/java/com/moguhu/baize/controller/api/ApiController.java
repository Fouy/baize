package com.moguhu.baize.controller.api;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.request.api.ApiSaveRequest;
import com.moguhu.baize.common.request.api.ApiSearchRequest;
import com.moguhu.baize.common.request.api.ApiUpdateRequest;
import com.moguhu.baize.common.response.api.ApiResponse;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.service.api.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * API管理
 *
 * @author xuefeihu
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    private ApiService apiService;

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
            apiService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/option")
    @ResponseBody
    public AjaxResult option(Long apiId, String status) {
        try {
            if (StatusEnum.resolve(status) == null || null == apiId) {
                return AjaxResult.error("参数有误");
            }

            apiService.option(apiId, status);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("停启用信息失败, apiId={}, e={}", apiId, e);
            return AjaxResult.error("停启用信息失败");
        }
    }
}
