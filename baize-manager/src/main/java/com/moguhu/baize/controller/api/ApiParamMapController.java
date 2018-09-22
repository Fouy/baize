package com.moguhu.baize.controller.api;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.client.constants.BooleanEnum;
import com.moguhu.baize.client.constants.ParamMapTypeEnum;
import com.moguhu.baize.common.constants.api.ParamTypeEnum;
import com.moguhu.baize.common.constants.api.PositionEnum;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.api.ApiParamMapSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiParamMapUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiParamMapResponse;
import com.moguhu.baize.service.api.ApiParamMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * API Param 映射管理
 *
 * Created by xuefeihu on 18/9/11.
 */
@RestController
@RequestMapping("/apiparammap")
public class ApiParamMapController extends BaseController {

    @Autowired
    private ApiParamMapService apiParamMapService;

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("apiparammap/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("apiparammap/edit");
        return mav;
    }

    @RequestMapping("/constantadd")
    public ModelAndView constantadd() {
        ModelAndView mav = new ModelAndView("apiparammap/constant-add");
        return mav;
    }

    @RequestMapping("/constantedit")
    public ModelAndView constantedit() {
        ModelAndView mav = new ModelAndView("apiparammap/constant-edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<ApiParamMapResponse> list(ApiParamMapSearchRequest request) {
        PageListDto<ApiParamMapResponse> result = new PageListDto<>();
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

            result = apiParamMapService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long mapId) {
        try {
            if (null == mapId) {
                return AjaxResult.error("参数有误");
            }
            ApiParamMapResponse apiParamMapResponse = apiParamMapService.selectById(mapId);
            return AjaxResult.success(apiParamMapResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, mapId = {}, e={}", mapId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(ApiParamMapSaveRequest request) {
        try {
            if (null == request.getApiId() || PositionEnum.resolve(request.getPosition()) == null
                    || StringUtils.isEmpty(request.getName()) || ParamMapTypeEnum.resolve(request.getMapType()) == null) {
                return AjaxResult.error("参数有误");
            }
            // 1 映射参数 type need 以原参数为准; 2 常量需要非空; 3 自定暂不支持
            ParamMapTypeEnum mapTypeEnum = ParamMapTypeEnum.resolve(request.getMapType());
            if (mapTypeEnum.matches(ParamMapTypeEnum.MAP.name()) && request.getParamId() == null) {
                return AjaxResult.error("参数有误");
            }
            if (mapTypeEnum.matches(ParamMapTypeEnum.CONSTANT.name())
                    && (ParamTypeEnum.resolve(request.getType()) == null || BooleanEnum.resolve(request.getNeed()) == null
                    || StringUtils.isEmpty(request.getDefaultValue()))) {
                return AjaxResult.error("参数有误");
            }
            if (mapTypeEnum.matches(ParamMapTypeEnum.CUSTOM.name())) {
                return AjaxResult.error("暂不支持自定义类型");
            }

            apiParamMapService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(ApiParamMapUpdateRequest request) {
        try {
            if (request.getMapId() == null) {
                return AjaxResult.error("参数有误");
            }

            apiParamMapService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long mapId) {
        try {
            if (null == mapId) {
                return AjaxResult.error("参数有误");
            }
            // 检查是否存在
            ApiParamMapResponse apiParamMapResponse = apiParamMapService.selectById(mapId);
            if (null == apiParamMapResponse) {
                return AjaxResult.error("记录不存在或状态不合法");
            }

            apiParamMapService.deleteById(mapId);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, mapId={}, e={}", mapId, e);
            return AjaxResult.error("删除信息失败");
        }
    }

}
