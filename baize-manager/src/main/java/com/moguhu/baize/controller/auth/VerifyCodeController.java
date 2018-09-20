package com.moguhu.baize.controller.auth;

import com.moguhu.baize.common.utils.auth.VerifyCodeUtils;
import com.moguhu.baize.service.auth.CodeCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取验证码
 * <p>
 * Created by xuefeihu on 18/8/29.
 */
@RestController
@RequestMapping("/api/verifycode")
public class VerifyCodeController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeController.class);

    @Resource
    private CodeCacheService codeCacheService;

    @RequestMapping("/generate")
    public void generate(String codeid, HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");

            if (StringUtils.isEmpty(codeid)) {
                logger.warn("无效的证码获取");
                return;
            }

            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            // 存入redis
            codeCacheService.put(codeid, verifyCode.toLowerCase());
            // 生成图片
            int w = 130, h = 30;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (Exception e) {
            logger.error("获取验证码异常：{}", e);
        }
    }

}
