/**
 * APIConnector.java
 */
package com.moguhu.baize.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author Yate
 * @date Oct 17, 2014
 * @description Api连接者
 * @version 1.0
 */
public final class ApiConnector
{
	private static final Logger logger = LoggerFactory.getLogger(ApiConnector.class);

	private static PoolingHttpClientConnectionManager connManager = null;

	private static CloseableHttpClient httpclient = null;

	private static Header DEFAULT_HEADER = new BasicHeader(HttpHeaders.USER_AGENT, "multipay");

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;

	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 60000;

	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 400;

	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 30000;

	private static SSLConnectionSocketFactory socketFactory = null;

	private static X509TrustManager xtm = new X509TrustManager() {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{

		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{

		}

		public X509Certificate[] getAcceptedIssuers()
		{

			return null;
		}
	};

	static
	{
		SSLContext ctx;
		try
		{
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { xtm }, null);
			socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
		}
		catch (Exception e)
		{
			logger.error("创建SSLConnection失败：", e);
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry;
		if (socketFactory == null)
		{
			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).build();
		}
		else
		{
			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
		}

		connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

		connManager.setDefaultSocketConfig(socketConfig);
		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
				.setMaxLineLength(2000).build();
		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		httpclient = HttpClients.custom().setConnectionManager(connManager)
				.setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
				.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).build();
	}

	public static String post(String url, List<NameValuePair> pairs, String encoding)
	{
		HttpPost post = new HttpPost(url.trim());
		String responseString = "";

		try
		{
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.addHeader(DEFAULT_HEADER);

			if (pairs != null && pairs.size() > 0)
			{
				post.setEntity(new UrlEncodedFormEntity(pairs, encoding));
			}

			logger.info("[HttpUtils Post] begin invoke url:{} , params:{}", url, pairs);
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis() - s1;
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils Post] error, url : {}  , params : {},  status :{}", url, pairs,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, encoding);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}

			logger.info("[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}", url,
					pairs, responseString, s2);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils Post] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}

	public static String post(String url, List<NameValuePair> pairs)
	{
		HttpPost post = new HttpPost(url.trim());
		String responseString = "";

		try
		{
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.addHeader(DEFAULT_HEADER);

			if (pairs != null && pairs.size() > 0)
			{
				post.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
			}
			
			logger.info("[HttpUtils Post] begin invoke url:{} , params:{}", url, pairs);
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils Post] error, url : {}  , params : {},  status :{}", url, pairs,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, Consts.UTF_8);

					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}

			logger.info("[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}", url,
					pairs, responseString, s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils Post] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}

	/**
	 * 包含异常的post方法，该方法会抛出超时异常
	 * 
	 * @param url
	 * @param pairs
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static String postContainException(String url, List<NameValuePair> pairs) throws SocketTimeoutException
	{
		HttpPost post = new HttpPost(url.trim());
		String responseString = "";

		try
		{
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.addHeader(DEFAULT_HEADER);

			if (pairs != null && pairs.size() > 0)
			{
				post.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
			}

			logger.info("[HttpUtils Post] begin invoke url:{} , params:{}", url, pairs);
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils Post] error, url : {}  , params : {},  status :{}", url, pairs,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}

			logger.info("[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}", url,
					pairs, responseString, s2 - s1);
		}
		catch (SocketTimeoutException e)
		{
			throw e;

		}
		catch (Exception e)
		{
			logger.error("[HttpUtils Post] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}

	@SuppressWarnings("deprecation")
	public static String get(String url, List<NameValuePair> pairs, String encode)
	{
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0)
		{
			for (NameValuePair entry : pairs)
			{
				if (i == 0 && !url.contains("?"))
				{
					sb.append("?");
				}
				else
				{
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try
				{
					sb.append(URLEncoder.encode(value, "UTF-8"));
				}
				catch (UnsupportedEncodingException e)
				{
					logger.warn("encode http get params error, value is {}", value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		logger.info("[HttpUtils Get] begin invoke:" + url.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);
		get.addHeader(DEFAULT_HEADER);

		try
		{
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(get);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					get.abort();
					logger.error("[HttpUtils Get] error, url : {}  , params : {},  status :{}", url, pairs, statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, encode);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			catch (Exception e)
			{
				logger.error("[HttpUtils Get]get response error, url:{}", url.toString(), e);
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("[HttpUtils Get]Debug url:{} , response string :{},time={}", url.toString(), responseString,
					s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils Get] error, url : {}  , params : {}, response string : {} ,error : {}", url, pairs,
					"", e.getMessage(), e);
		}
		finally
		{
			get.releaseConnection();
		}
		return responseString;
	}

	public static String postJson(String url, JSON data, String encode)
	{
		String responseString = null;

		logger.info("[HttpUtils PostJson] begin invoke:{},param:{}", url.toString(), data);
		HttpPost post = new HttpPost(url.trim());
		post.setEntity(new StringEntity(data.toJSONString(), ContentType.APPLICATION_JSON));

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
		post.setConfig(requestConfig);
		post.addHeader(DEFAULT_HEADER);

		try
		{
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils PostJson] error, url : {}  , params : {},  status :{}", url, data,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, encode);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			catch (Exception e)
			{
				logger.error("[HttpUtils PostJson]post response error, url:{}", url.toString(), e);
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("[HttpUtils PostJson]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
					url, data, responseString, s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					data, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}

	public static String postJson(String url, JSON data)
	{
		String responseString = null;

		logger.info("[HttpUtils PostJson] begin invoke:{},param:{}", url.toString(), data);
		HttpPost post = new HttpPost(url.trim());
		post.setEntity(new StringEntity(data.toJSONString(), ContentType.APPLICATION_JSON));

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
		post.setConfig(requestConfig);
		post.addHeader(DEFAULT_HEADER);

		try
		{
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils PostJson] error, url : {}  , params : {},  status :{}", url, data,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			catch (Exception e)
			{
				logger.error("[HttpUtils PostJson]post response error, url:{}", url.toString(), e);
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("[HttpUtils PostJson]Debug url:{} , response string :{},time={}", url.toString(),
					responseString, s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					data, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}

	@SuppressWarnings("deprecation")
	public static String get(String url, List<NameValuePair> pairs)
	{
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0)
		{
			for (NameValuePair entry : pairs)
			{
				if (i == 0 && !url.contains("?"))
				{
					sb.append("?");
				}
				else
				{
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try
				{
					sb.append(URLEncoder.encode(value, Consts.UTF_8.name()));
				}
				catch (UnsupportedEncodingException e)
				{
					logger.warn("encode http get params error, value is {}", value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		logger.info("[HttpUtils Get] begin invoke:{}", url.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);
		get.addHeader(DEFAULT_HEADER);

		try
		{
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(get);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					get.abort();
					logger.error("[HttpUtils Get] error, url : {}  , params : {},  status :{}", url, pairs, statusCode);
					return "";
				}
				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			catch (Exception e)
			{
				logger.error("[HttpUtils Get]get response error, url:{}", url.toString(), e);
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("[HttpUtils Get]Debug url:{} , response string :{},time={}", url.toString(), responseString,
					s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		}
		finally
		{
			get.releaseConnection();
		}
		return responseString;
	}

	/**
	 * HTTPS请求，默认超时为5S
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String connectPostHttps(String url, Map<String, String> params)
	{
		String responseContent = null;

		HttpPost httpsPost = new HttpPost(url);
		try
		{
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			httpsPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			httpsPost.setConfig(requestConfig);
			httpsPost.addHeader(DEFAULT_HEADER);

			// 绑定到请求 Entry
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			CloseableHttpResponse response = httpclient.execute(httpsPost);
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					httpsPost.abort();
					logger.error("[HttpUtils Security] error, url : {}  , params : {},  status :{}", url, params,
							statusCode);
					return "";
				}
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try
				{
					if (null != entity)
					{
						responseContent = EntityUtils.toString(entity, Consts.UTF_8);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("requestURI : " + httpsPost.getURI() + ", responseContent: " + responseContent);
		}
		catch (ClientProtocolException e)
		{
			logger.error("ClientProtocolException", e);
		}
		catch (IOException e)
		{
			logger.error("IOException", e);
		}
		finally
		{
			httpsPost.releaseConnection();
		}
		return responseContent;
	}

	public static String postString(String url, String data)
	{
		String responseString = null;

		logger.info("[HttpUtils PostJson] begin invoke:{},param:{}", url.toString(), data);
		HttpPost post = new HttpPost(url.trim());
		post.setEntity(new StringEntity(data, ContentType.create("application/xml", Consts.UTF_8)));

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
		post.setConfig(requestConfig);
		post.addHeader(DEFAULT_HEADER);

		try
		{
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(post);
			long s2 = System.currentTimeMillis();
			try
			{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					post.abort();
					logger.error("[HttpUtils PostJson] error, url : {}  , params : {},  status :{}", url, data,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try
				{
					if (entity != null)
					{
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				}
				finally
				{
					if (entity != null)
					{
						entity.getContent().close();
					}
				}
			}
			catch (Exception e)
			{
				logger.error("[HttpUtils PostJson]post response error, url:{}", url.toString(), e);
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
			logger.info("[HttpUtils PostJson]Debug url:{} , response string :{},time={}", url.toString(),
					responseString, s2 - s1);
		}
		catch (Exception e)
		{
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					data, "", e.getMessage(), e);
		}
		finally
		{
			post.releaseConnection();
		}
		return responseString;
	}
}
