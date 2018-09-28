package com.moguhu.baize.component.pre

import com.moguhu.baize.client.model.ApiDto
import com.moguhu.zuul.ZuulFilter
import com.moguhu.zuul.component.ApiParamParser
import com.moguhu.zuul.context.NFRequestContext
import com.moguhu.zuul.zookeeper.ApiManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

/**
 * URL mapping 验证组件
 * <p>
 * 映射匹配 API Manager 中管理的API
 * <p>
 * Created by xuefeihu on 18/9/21.
 */
public class ApiMappingFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiMappingFilter.class);

    @Override
    public int filterOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        NFRequestContext ctx = NFRequestContext.getCurrentContext();
        try {
            HttpServletRequest request = ctx.getRequest();
            String uri = request.getRequestURI();

            // 检查URL 是否合法, 如不合法, 则直接抛出异常, 并将异常信息放入 requestContext, 然后交由 error 处理.
            ApiDto api = ApiManager.checkPermission(uri);

            // 生成后端请求参数模型, 并放入 requestContext  (这里面需要建立一套参数模型)
            Map<String, Map<String, String>> backendParams = ApiParamParser.parseBackendParam(request, api);
            ctx.setBackendParams(backendParams);

        } catch (Exception e) {
            logger.error("API Mapping check error, {}", e);
        }
        return null;
    }

}
