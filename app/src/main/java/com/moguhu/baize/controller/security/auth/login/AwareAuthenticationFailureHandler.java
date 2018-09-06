package com.moguhu.baize.controller.security.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moguhu.baize.common.vo.AjaxResult;
import com.moguhu.baize.controller.security.exceptions.AuthMethodNotSupportedException;
import com.moguhu.baize.controller.security.exceptions.ExpiredTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理程序
 *
 * @author xuefeihu
 */
@Component
public class AwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;

    @Autowired
    public AwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (e instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(), AjaxResult.error("Invalid username or password"));
        } else if (e instanceof ExpiredTokenException) {
            mapper.writeValue(response.getWriter(), AjaxResult.error("Token has expired"));
        } else if (e instanceof AuthMethodNotSupportedException) {
            mapper.writeValue(response.getWriter(), AjaxResult.error(e.getMessage()));
        }
        mapper.writeValue(response.getWriter(), AjaxResult.error("Authentication failed"));
    }
}
