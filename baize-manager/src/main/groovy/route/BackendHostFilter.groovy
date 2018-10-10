package com.moguhu.zuul.component.route

import com.google.common.collect.Lists
import com.moguhu.baize.client.constants.PositionEnum
import com.moguhu.baize.client.model.ApiDto
import com.moguhu.baize.client.model.ApiGroupDto
import com.moguhu.baize.client.utils.WebClient
import com.moguhu.zuul.ZuulFilter
import com.moguhu.zuul.component.ApiParamParser
import com.moguhu.zuul.context.NFRequestContext
import com.moguhu.zuul.exception.ZuulException
import com.moguhu.zuul.util.HostsUtil
import org.apache.commons.lang.StringUtils
import org.apache.http.HttpStatus
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletResponse

/**
 * 后端基于HOST路由组件
 * <p>
 * Created by xuefeihu on 18/9/22.
 */
public class BackendHostFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(BackendHostFilter.class);

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        NFRequestContext ctx = NFRequestContext.getCurrentContext();
        try {
            ApiGroupDto group = ctx.getBackendGroup();
            String hostsStr = group.getHosts();
            String host = "";
            if (StringUtils.isNotEmpty(hostsStr)) {
                List<String> hosts = Arrays.asList(hostsStr.split(","));
                host = HostsUtil.getRandomNode(hosts);
            }
            if (StringUtils.isEmpty(host)) {
                throw new ZuulException("Host was empty", HttpStatus.SC_OK, "");
            }

            ApiDto api = ctx.getBackendApi();
            Map<String, Map<String, String>> backendParams = ctx.getBackendParams();
            // 后端请求URL
            String backendUrl = api.getProtocol() + "://" + host + api.getPath();

            // 后端参数组装
            String getUrlTail = "";
            List<NameValuePair> requestHeaders = Lists.newArrayList();
            List<NameValuePair> requestEntity = Lists.newArrayList();
            Iterator<Map.Entry<String, Map<String, String>>> iterator = backendParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, String>> next = iterator.next();
                String paramName = next.getKey();
                Map<String, String> valueMap = next.getValue();
                String position = valueMap.get(ApiParamParser.MAPPING_METHOD_KEY);
                String value = valueMap.get(ApiParamParser.MAPPING_VALUE_KEY);
                if (PositionEnum.HEAD.name().equals(position)) {
                    requestHeaders.add(new BasicNameValuePair(paramName, value));
                } else if (PositionEnum.POST.name().equals(position)) {
                    requestEntity.add(new BasicNameValuePair(paramName, value));
                } else if (PositionEnum.GET.name().equals(position)) {
                    if (StringUtils.isEmpty(getUrlTail)) {
                        getUrlTail = paramName + "=" + value;
                    } else {
                        getUrlTail = getUrlTail + "&" + paramName + "=" + value;
                    }
                } else {
                    logger.warn("not supported parameter type of {}, it has been ignored.", position);
                }
            }
            backendUrl = backendUrl + getUrlTail;

            // TODO 需要重写, 所有的响应头都要返回, 不可以吃掉上游服务器返回的数据
            String result = new WebClient().doPost4Html(backendUrl, "", requestHeaders, requestEntity);
            HttpServletResponse response = ctx.getResponse();
            response.getWriter().write(result);

        } catch (Exception e) {
            logger.error("API Mapping check error, {}", e);
            throw new RuntimeException(e);
        }
        return null;
    }


}
