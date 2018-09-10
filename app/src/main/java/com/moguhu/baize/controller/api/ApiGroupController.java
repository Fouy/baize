package com.moguhu.baize.controller.api;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.request.api.ApiGroupSaveRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupSearchRequest;
import com.moguhu.baize.metadata.request.api.ApiGroupUpdateRequest;
import com.moguhu.baize.metadata.response.api.ApiGroupResponse;
import com.moguhu.baize.service.api.ApiGroupService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * API分组 管理
 *
 * @author xuefeihu
 */
@RestController
@RequestMapping("/apigroup")
public class ApiGroupController extends BaseController {

    @Autowired
    private ApiGroupService apiGroupService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("apigroup/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("apigroup/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("apigroup/edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<ApiGroupResponse> list(ApiGroupSearchRequest request) {
        PageListDto<ApiGroupResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }

            result = apiGroupService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/all")
    @ResponseBody
    public AjaxResult all() {
        try {
            List<ApiGroupResponse> apiGroupResponse = apiGroupService.all();
            return AjaxResult.success(apiGroupResponse);
        } catch (Exception e) {
            logger.error("查询所有列表失败, e={}", e);
            return AjaxResult.error("查询所有列表失败");
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long groupId) {
        try {
            if (null == groupId) {
                return AjaxResult.error("参数有误");
            }
            ApiGroupResponse apiGroupResponse = apiGroupService.selectById(groupId);
            return AjaxResult.success(apiGroupResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, groupId = {}, e={}", groupId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(ApiGroupSaveRequest request) {
        try {
            if (StringUtils.isEmpty(request.getName()) || StringUtils.isEmpty(request.getType())
                    || null == request.getServiceId()) {
                return AjaxResult.error("参数有误");
            }

            apiGroupService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(ApiGroupUpdateRequest request) {
        try {
            if (null == request.getGroupId()) {
                return AjaxResult.error("参数有误");
            }
            // 检查分组是否存在
            ApiGroupResponse apiGroupResponse = apiGroupService.selectById(request.getGroupId());
            if (null == apiGroupResponse || StatusEnum.ON.name().equals(apiGroupResponse.getStatus()) ) {
                return AjaxResult.error("记录不存在或状态不合法");
            }

            apiGroupService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long groupId) {
        try {
            if (null == groupId) {
                return AjaxResult.error("参数有误");
            }
            // 检查分组是否存在
            ApiGroupResponse apiGroupResponse = apiGroupService.selectById(groupId);
            if (null == apiGroupResponse || StatusEnum.ON.name().equals(apiGroupResponse.getStatus()) ) {
                return AjaxResult.error("记录不存在或状态不合法");
            }

            apiGroupService.deleteById(groupId);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, groupId={}, e={}", JSON.toJSONString(groupId), e);
            return AjaxResult.error("删除信息失败");
        }
    }

    @RequestMapping("/option")
    @ResponseBody
    public AjaxResult option(Long groupId, String status) {
        try {
            if (null == groupId || StatusEnum.resolve(status) == null) {
                return AjaxResult.error("参数有误");
            }
            // 检查分组是否存在
            ApiGroupResponse apiGroupResponse = apiGroupService.selectById(groupId);
            if (null == apiGroupResponse) {
                return AjaxResult.error("记录不存在");
            }

            apiGroupService.option(groupId, status);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, groupId={}, e={}", JSON.toJSONString(groupId), e);
            return AjaxResult.error("删除信息失败");
        }
    }

}
