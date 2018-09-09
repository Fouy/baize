package com.moguhu.baize.controller.backend;

import com.moguhu.baize.common.vo.PageListDto;
import com.moguhu.baize.controller.BaseController;
import com.moguhu.baize.metadata.response.backend.GateInstanceResponse;
import com.moguhu.baize.service.backend.GateInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 网关实例
 *
 * Created by xuefeihu on 18/9/9.
 */
@RestController
@RequestMapping("/gateinstance")
public class GateInstanceController extends BaseController {

    @Autowired
    private GateInstanceService gateInstanceService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("gateinstance/main");
        return mav;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageListDto<GateInstanceResponse> list(Long serviceId) {
        PageListDto<GateInstanceResponse> result = new PageListDto<>();
        try {
            result = gateInstanceService.pageList(serviceId);
        } catch (Exception e) {
            logger.error("网关实例查询出错, e={}", e);
        }

        return result;
    }

}
