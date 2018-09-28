package com.moguhu.baize.controller.backend;

import com.alibaba.fastjson.JSON;
import com.moguhu.baize.common.constants.StatusEnum;
import com.moguhu.baize.common.constants.backend.ComponentTypeEnum;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.entity.api.ApiCompRelaEntity;
import com.moguhu.baize.metadata.entity.api.GroupCompRelaEntity;
import com.moguhu.baize.metadata.entity.common.RichContentEntity;
import com.moguhu.baize.metadata.request.backend.ComponentSaveRequest;
import com.moguhu.baize.metadata.request.backend.ComponentSearchRequest;
import com.moguhu.baize.metadata.request.backend.ComponentUpdateRequest;
import com.moguhu.baize.metadata.response.backend.ComponentResponse;
import com.moguhu.baize.service.api.ApiCompRelaService;
import com.moguhu.baize.service.api.GroupCompRelaService;
import com.moguhu.baize.service.backend.ComponentService;
import com.moguhu.baize.service.common.RichContentService;
import com.moguhu.baize.zuul.common.FilterInfo;
import com.moguhu.baize.zuul.filter.FilterVerifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 组件管理
 * <p>
 * Created by xuefeihu on 18/9/12.
 */
@RestController
@RequestMapping("/component")
public class ComponentController extends BaseController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private RichContentService richContentService;

    @Autowired
    private ApiCompRelaService apiCompRelaService;

    @Autowired
    private GroupCompRelaService groupCompRelaService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("component/main");
        return mav;
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("component/add");
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("component/edit");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<ComponentResponse> list(ComponentSearchRequest request) {
        PageListDto<ComponentResponse> result = new PageListDto<>();
        try {
            if (null == request.getPageNumber()) {
                request.setPageNumber(1);
            }
            if (null == request.getPageSize()) {
                request.setPageSize(10);
            }

            result = componentService.pageList(request);
        } catch (Exception e) {
            logger.error("分页查询出错, e={}", e);
        }

        return result;
    }

    @RequestMapping("/downfile")
    @ResponseBody
    public void downfile(Long compId, HttpServletResponse response) {
        try {
            // 查询文件
            ComponentResponse componentResponse = componentService.selectById(compId);
            if (null == componentResponse) {
                return;
            }
            RichContentEntity richContentEntity = richContentService.selectById(componentResponse.getContentId());
            String content = richContentEntity.getContent();
            FilterInfo filterInfo = FilterVerifier.getInstance().verifyFilter(content);

            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setContentLength(content.getBytes(Charset.forName("UTF-8")).length);

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", filterInfo.getGroovyFileName() + ".groovy");
            response.setHeader(headerKey, headerValue);
            response.setContentType("application/octet-stream;charset=UTF-8");

            this.writeResponse(response, content);
        } catch (Exception e) {
            logger.error("下载文件出错, e={}", e);
        }
    }

    @RequestMapping("/all")
    @ResponseBody
    public AjaxResult all() {
        try {
            List<ComponentResponse> list = componentService.all();
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("查询全部列表, e={}", e);
            return AjaxResult.error("查询全部列表");
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long compId) {
        try {
            if (null == compId) {
                return AjaxResult.error("参数有误");
            }
            ComponentResponse componentResponse = componentService.selectById(compId);
            return AjaxResult.success(componentResponse);
        } catch (Exception e) {
            logger.error("查询详情失败, compId = {}, e={}", compId, e);
            return AjaxResult.error("查询详情失败");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(ComponentSaveRequest request, MultipartFile file) {
        try {
            if (StringUtils.isEmpty(request.getName()) || ComponentTypeEnum.resolve(request.getType()) == null
                    || StringUtils.isEmpty(request.getVersion())) {
                return AjaxResult.error("参数有误");
            }
            if (file.isEmpty()) {
                return AjaxResult.error("文件内容为空");
            }
            String fileContent = new String(file.getBytes(), "UTF-8");
            FilterInfo filterInfo = FilterVerifier.getInstance().verifyFilter(fileContent);

            request.setCompCode(filterInfo.getCompCode());
            request.setExecPosition(filterInfo.getExecPosition());
            request.setPriority(filterInfo.getPriority());
            request.setGroovyCode(filterInfo.getGroovyCode());

            componentService.save(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("新增信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("新增信息失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult update(ComponentUpdateRequest request, MultipartFile file) {
        try {
            if (null == request.getCompId()) {
                return AjaxResult.error("参数有误");
            }
            ComponentResponse componentEntity = componentService.selectById(request.getCompId());
            if (null == componentEntity) {
                return AjaxResult.error("组件不存在");
            }
            if (!file.isEmpty()) {
                String fileContent = new String(file.getBytes(), "UTF-8");
                FilterInfo filterInfo = FilterVerifier.getInstance().verifyFilter(fileContent);

                request.setCompCode(filterInfo.getCompCode());
                request.setExecPosition(filterInfo.getExecPosition());
                request.setPriority(filterInfo.getPriority());
                request.setGroovyCode(filterInfo.getGroovyCode());
            }

            componentService.updateById(request);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("更新信息失败, request={}, e={}", JSON.toJSONString(request), e);
            return AjaxResult.error("更新信息失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long compId) {
        try {
            if (null == compId) {
                return AjaxResult.error("参数有误");
            }
            ComponentResponse componentEntity = componentService.selectById(compId);
            if (null == componentEntity) {
                return AjaxResult.error("组件不存在");
            }
            // 检查是否有API依赖
            if (this.checkUsed(compId)) {
                return AjaxResult.error("组件被使用中, 不可停用.");
            }

            componentService.deleteById(compId);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("删除信息失败, compId={}, e={}", compId, e);
            return AjaxResult.error("删除信息失败");
        }
    }

    @RequestMapping("/option")
    @ResponseBody
    public AjaxResult option(Long compId, String status) {
        try {
            if (StatusEnum.resolve(status) == null || null == compId) {
                return AjaxResult.error("参数有误");
            }
            ComponentResponse componentEntity = componentService.selectById(compId);
            if (null == componentEntity) {
                return AjaxResult.error("组件不存在");
            }
            // 检查是否有API依赖
            if (StatusEnum.OFF.name().equals(status) && this.checkUsed(compId)) {
                return AjaxResult.error("组件被使用中, 不可停用.");
            }

            componentService.option(compId, status);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("停启用失败, compId={}, e={}", compId, e);
            return AjaxResult.error("停启用失败");
        }
    }

    private boolean checkUsed(Long compId) {
        ApiCompRelaEntity param = new ApiCompRelaEntity();
        param.setCompId(compId);
        List<ApiCompRelaEntity> apiRelas = apiCompRelaService.all(param);
        if (!CollectionUtils.isEmpty(apiRelas)) {
            return true;
        }

        GroupCompRelaEntity param1 = new GroupCompRelaEntity();
        param1.setCompId(compId);
        List<GroupCompRelaEntity> groupRelas = groupCompRelaService.all(param1);
        if (!CollectionUtils.isEmpty(groupRelas)) {
            return true;
        }
        return false;
    }

}
