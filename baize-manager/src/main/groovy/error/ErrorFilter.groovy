package com.moguhu.zuul.component.error

import com.alibaba.fastjson.JSON
import com.google.common.collect.Maps
import com.moguhu.zuul.ZuulFilter
import com.moguhu.zuul.context.NFRequestContext
import com.moguhu.zuul.exception.ZuulException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Generate a error response while there is an error.
 */
public class ErrorFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        NFRequestContext currentContext = NFRequestContext.getCurrentContext();
        return currentContext.getThrowable() != null;
    }

    @Override
    public Object run() {
        NFRequestContext ctx = NFRequestContext.getCurrentContext();
        Throwable ex = ctx.getThrowable();
        try {
            String errorCause = "Zuul Gateway Unknow Error.";
            int responseStatusCode;

            if (ex instanceof ZuulException) {
                ZuulException zex = (ZuulException) ex;
                String cause = zex.errorCause;
                if (cause != null) {
                    errorCause = cause;
                }
                responseStatusCode = zex.nStatusCode;

                Enumeration<String> headerIt = ctx.getRequest().getHeaderNames();
                StringBuilder sb = new StringBuilder(ctx.getRequest().getRequestURI() + ":" + errorCause);
                while (headerIt.hasMoreElements()) {
                    String name = headerIt.nextElement();
                    String value = ctx.getRequest().getHeader(name);
                    sb.append("REQUEST:: > " + name + ":" + value + "\n");
                }
                logger.error(sb.toString());
            } else {
                responseStatusCode = 500;
            }
            ctx.setResponseStatusCode(responseStatusCode);

            ctx.addZuulResponseHeader("Content-Type", "application/json; charset=utf-8");
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody(generateJson(ex));
        } finally {
            ctx.setErrorHandled(true); //ErrorFilter was handled
            return null;
        }
    }

    private String generateJson(Throwable ex) {
        Map<String, String> map = Maps.newHashMap();
        map.put("msg", ex.getMessage());
        return JSON.toJSONString(map);
    }

}