package com.moguhu.baize.controller;


import com.moguhu.baize.controller.security.auth.AuthenticationToken;
import com.moguhu.baize.controller.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * BASE Controller
 *
 * @author xuefeihu
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 登录上下文信息
     *
     * @return
     */
    public UserContext getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AuthenticationToken) {
            AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
            return (UserContext) authenticationToken.getPrincipal();
        }
        return new UserContext();
    }

    /**
     * stream 回写
     *
     * @param response
     * @param result
     */
    protected void writeResponse(HttpServletResponse response, String result) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(result);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}
