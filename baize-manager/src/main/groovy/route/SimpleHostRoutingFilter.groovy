package com.moguhu.zuul.component.route;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.moguhu.baize.client.constants.PositionEnum;
import com.moguhu.baize.client.model.ApiDto;
import com.moguhu.baize.client.model.ApiGroupDto;
import com.moguhu.baize.client.model.ComponentDto;
import com.moguhu.zuul.ZuulFilter;
import com.moguhu.zuul.component.ApiParamParser;
import com.moguhu.zuul.component.ProxyRequestHelper;
import com.moguhu.zuul.component.ZuulProperties;
import com.moguhu.zuul.component.http.ApacheHttpClientConnectionManagerFactory;
import com.moguhu.zuul.component.http.ApacheHttpClientFactory;
import com.moguhu.zuul.component.http.DefaultApacheHttpClientConnectionManagerFactory;
import com.moguhu.zuul.component.http.DefaultApacheHttpClientFactory;
import com.moguhu.zuul.context.NFRequestContext;
import com.moguhu.zuul.context.RequestContext;
import com.moguhu.zuul.exception.ZuulException;
import com.moguhu.zuul.exception.ZuulRuntimeException;
import com.moguhu.zuul.util.HostsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.moguhu.zuul.constants.FilterConstants.ROUTE_TYPE;
import static com.moguhu.zuul.constants.FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER;

/**
 * 后端基于HOST路由组件
 * <p>
 * 1. 需要baize后台配合使用
 */
public class SimpleHostRoutingFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHostRoutingFilter.class);

    private final Timer connectionManagerTimer = new Timer("SimpleHostRoutingFilter.connectionManagerTimer", true);

    private boolean sslHostnameValidationEnabled;

    private ProxyRequestHelper helper;
    private ZuulProperties.Host hostProperties;
    private ApacheHttpClientConnectionManagerFactory connectionManagerFactory;
    private ApacheHttpClientFactory httpClientFactory;
    private HttpClientConnectionManager connectionManager;
    private CloseableHttpClient httpClient;

    public SimpleHostRoutingFilter() {
        ZuulProperties properties = new ZuulProperties();
        this.helper = new ProxyRequestHelper();
        this.hostProperties = properties.getHost();
        this.sslHostnameValidationEnabled = properties.isSslHostnameValidationEnabled();
        this.connectionManagerFactory = new DefaultApacheHttpClientConnectionManagerFactory();
        this.httpClientFactory = new DefaultApacheHttpClientFactory();
        initialize();
    }

    private void initialize() {
        this.connectionManager = connectionManagerFactory.newConnectionManager(!this.sslHostnameValidationEnabled,
                this.hostProperties.getMaxTotalConnections(), this.hostProperties.getMaxPerRouteConnections(),
                this.hostProperties.getTimeToLive(), this.hostProperties.getTimeUnit(), null);
        this.httpClient = newClient();
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (SimpleHostRoutingFilter.this.connectionManager == null) {
                    return;
                }
                SimpleHostRoutingFilter.this.connectionManager.closeExpiredConnections();
            }
        }, 30000, 5000);
    }

    public void stop() {
        this.connectionManagerTimer.cancel();
    }

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SIMPLE_HOST_ROUTING_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        ApiDto api = NFRequestContext.getCurrentContext().getBackendApi();
        if (CollectionUtils.isNotEmpty(api.getComponentList())) {
            for (ComponentDto componentDto : api.getComponentList()) {
                if (componentDto != null && componentDto.getCompCode().equalsIgnoreCase(componentName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object run() {
        NFRequestContext ctx = NFRequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            ApiGroupDto group = ctx.getBackendGroup();
            String hostsStr = group.getHosts();
            String host = "";
            if (org.apache.commons.lang.StringUtils.isNotEmpty(hostsStr)) {
                List<String> hosts = Arrays.asList(hostsStr.split(","));
                host = HostsUtil.getRandomNode(hosts);
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(host)) {
                throw new ZuulException("Host was empty", HttpStatus.SC_OK, "");
            }

            ApiDto api = ctx.getBackendApi();
            Map<String, Map<String, String>> backendParams = ctx.getBackendParams();
            // 后端请求URL
//            String backendUrl = api.getProtocol() + "://" + host + api.getPath();
            String[] hostAndport = host.split(":");
            host = hostAndport[0];
            int port = 80;
            if (hostAndport.length > 1) {
                port = Integer.parseInt(hostAndport[1]);
            }
            RequestContext.getCurrentContext().setRouteHost(new URL(api.getProtocol(), host, port, ""));

            // 后端参数组装
            String getUrlTail = "";
            Map<String, String> headers = Maps.newHashMap();
            Map<String, String> params = Maps.newHashMap();
            Iterator<Map.Entry<String, Map<String, String>>> iterator = backendParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, String>> next = iterator.next();
                String paramName = next.getKey();
                Map<String, String> valueMap = next.getValue();
                String position = valueMap.get(ApiParamParser.MAPPING_METHOD_KEY);
                String value = valueMap.get(ApiParamParser.MAPPING_VALUE_KEY);
                if (PositionEnum.HEAD.name().equals(position)) {
                    headers.put(paramName, value);
                } else if (PositionEnum.POST.name().equals(position)) {
                    params.put(paramName, value);
                } else if (PositionEnum.GET.name().equals(position)) {
                    if (org.apache.commons.lang.StringUtils.isEmpty(getUrlTail)) {
                        getUrlTail = paramName + "=" + value;
                    } else {
                        getUrlTail = getUrlTail + "&" + paramName + "=" + value;
                    }
                } else {
                    logger.warn("not supported parameter type of {}, it has been ignored.", position);
                }
            }
            String uri = api.getPath() + getUrlTail;

            // 所有的响应头都要返回, 不可以吃掉上游服务器返回的数据
            String verb = Splitter.on(",").splitToList(api.getMethods()).get(0); // 多种Method 取第一种
            InputStream requestEntity = getRequestBody(request);
            if (request.getContentLength() < 0) {
                ctx.setChunkedRequestBody();
            }

            CloseableHttpResponse response = forward(this.httpClient, verb, uri, request, headers, params, requestEntity);
            setResponse(response);

        } catch (Exception e) {
            logger.error("API Mapping check error, {}", e);
            throw new ZuulRuntimeException(e);
        }

        return null;
    }

    protected HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    protected CloseableHttpClient newClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(this.hostProperties.getSocketTimeoutMillis())
                .setConnectTimeout(this.hostProperties.getConnectTimeoutMillis())
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        return httpClientFactory.createBuilder().setDefaultRequestConfig(requestConfig).
                setConnectionManager(this.connectionManager).disableRedirectHandling().build();
    }

    private CloseableHttpResponse forward(CloseableHttpClient httpclient, String verb,
                                          String uri, HttpServletRequest request, Map<String, String> headers,
                                          Map<String, String> params, InputStream requestEntity) throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params, requestEntity);
        URL host = RequestContext.getCurrentContext().getRouteHost();
        HttpHost httpHost = getHttpHost(host);
        uri = StringUtils.cleanPath((host.getPath() + uri).replaceAll("/{2,}", "/"));
        int contentLength = request.getContentLength();

        ContentType contentType = null;
        if (request.getContentType() != null) {
            contentType = ContentType.parse(request.getContentType());
        }

        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength, contentType);
        HttpRequest httpRequest = buildHttpRequest(verb, uri, entity, headers);
        try {
            logger.debug(httpHost.getHostName() + " " + httpHost.getPort() + " " + httpHost.getSchemeName());
            CloseableHttpResponse zuulResponse = forwardRequest(httpclient, httpHost, httpRequest);
            this.helper.appendDebug(info, zuulResponse.getStatusLine().getStatusCode(), revertHeaders(zuulResponse.getAllHeaders()));
            return zuulResponse;
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure immediate deallocation of all system resources
            // httpclient.getConnectionManager().shutdown();
        }
    }

    protected HttpRequest buildHttpRequest(String verb, String uri, InputStreamEntity entity, Map<String, String> headers) {
        HttpRequest httpRequest;
        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            case "DELETE":
                BasicHttpEntityEnclosingRequest entityRequest = new BasicHttpEntityEnclosingRequest(verb, uri);
                httpRequest = entityRequest;
                entityRequest.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);
                logger.debug(uri);
        }

        httpRequest.setHeaders(convertHeaders(headers));
        return httpRequest;
    }

    private Map<String, String> revertHeaders(Header[] headers) {
        Map<String, String> map = Maps.newHashMap();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, header.getValue());
            }
        }
        return map;
    }

    private Header[] convertHeaders(Map<String, String> headers) {
        List<Header> list = Lists.newArrayList();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            list.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return list.toArray(new BasicHeader[0]);
    }

    private CloseableHttpResponse forwardRequest(CloseableHttpClient httpclient,
                                                 HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(), host.getProtocol());
        return httpHost;
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        } catch (IOException ex) {
            // no requestBody is ok.
        }
        return requestEntity;
    }

    private void setResponse(HttpResponse response) throws IOException {
        RequestContext.getCurrentContext().set("zuulResponse", response);
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    /**
     * Add header names to exclude from proxied response in the current request.
     *
     * @param names
     */
    protected void addIgnoredHeaders(String... names) {
        this.helper.addIgnoredHeaders(names);
    }

    /**
     * Determines whether the filter enables the validation for ssl hostnames.
     *
     * @return true if enabled
     */
    boolean isSslHostnameValidationEnabled() {
        return this.sslHostnameValidationEnabled;
    }
}
