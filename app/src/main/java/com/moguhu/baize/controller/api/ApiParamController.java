package com.moguhu.baize.controller.api;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.api.ApiParamSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiParamSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiParamResponse;
import com.moguhu.baize.service.api.ApiParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * API Param 管理
 *
 * @author xuefeihu
 */
@RestController
@RequestMapping("/apiparam")
public class ApiParamController extends BaseController {

    @Autowired
    private ApiParamService apiParamService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("apiparam/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("apiparam/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("apiparam/edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<ApiParamResponse> list(ApiParamSearchRequest request) {
        PageListDto<ApiParamResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }
            if (null == request.getApiId()) {
                return result;
            }

            result = apiParamService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long paramId) {
        try {
            if (null == paramId) {
                return AjaxResult.error("参数有误");
            }
            ApiParamResponse apiParamResponse = apiParamService.selectById(paramId);
            return AjaxResult.success(apiParamResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, paramId = {}, e={}", paramId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(ApiParamSaveRequest request) {
        try {
            apiParamService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(ApiParamUpdateRequest request) {
        try {
            apiParamService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

}
