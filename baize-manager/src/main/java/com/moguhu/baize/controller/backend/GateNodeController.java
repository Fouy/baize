package com.moguhu.baize.controller.backend;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.backend.GateNodeSaveRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeSearchRequest;
import com.moguhu.baize.metadata.request.backend.GateNodeUpdateRequest;
import com.moguhu.baize.metadata.response.backend.GateNodeResponse;
import com.moguhu.baize.service.backend.GateNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 网关节点管理
 *
 * Created by xuefeihu on 18/9/6.
 */
@RestController
@RequestMapping("/gatenode")
public class GateNodeController extends BaseController {

    @Autowired
    private GateNodeService gateNodeService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("gatenode/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("gatenode/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("gatenode/edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<GateNodeResponse> list(GateNodeSearchRequest request) {
        PageListDto<GateNodeResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }

            result = gateNodeService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long nodeId) {
        try {
            if (null == nodeId) {
                return AjaxResult.error("参数有误");
            }
            GateNodeResponse gateNodeResponse = gateNodeService.selectById(nodeId);
            return AjaxResult.success(gateNodeResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, nodeId = {}, e={}", nodeId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(GateNodeSaveRequest request) {
        try {
            gateNodeService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(GateNodeUpdateRequest request) {
        try {
            gateNodeService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long nodeId) {
        try {
            if (null == nodeId) {
                return AjaxResult.error("参数有误");
            }

            gateNodeService.deleteById(nodeId);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, nodeId={}, e={}", JSON.toJSONString(nodeId), e);
            return AjaxResult.error("删除信息失败");
        }
    }

}
