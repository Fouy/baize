package com.moguhu.baize.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class WebClient {
	
	private DefaultHttpClient httpClient = null;
	private static final Integer TIMEOUT = 60;//
	private CookieStore cookieStore = null;
	private int lastStatusCode = 0;
	/**
	 * 301 OR 302
	 */
	private String LocationUrl = "";
	private static final String DefaultContentType = "application/x-www-form-urlencoded";
	private String contentType = DefaultContentType;
	private static final String DefaultUserAgent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	private String userAgent = DefaultUserAgent;
	private String acceptCharset = "";
	private String accept = "";
	private String responseCharset = HTTP.DEFAULT_CONTENT_CHARSET;
	private String responseMimetype = HTTP.DEFAULT_CONTENT_TYPE;
	private String requestWith = "";
	private String host = "";

	public String getRequestWith() {
		return requestWith;
	}

	public void setRequestWith(String requestWith) {
		this.requestWith = requestWith;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public WebClient() {
		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager();
		mgr.setMaxTotal(200);
		mgr.setDefaultMaxPerRoute(20);
		this.httpClient = new DefaultHttpClient(mgr);
		this.cookieStore = new BasicCookieStore();

		AbstractHttpClient abstractHttpClient = (AbstractHttpClient) httpClient;
		if (abstractHttpClient != null) {
			abstractHttpClient.setCookieStore(this.cookieStore);
		}

		HttpParams httpParams = this.httpClient.getParams();
		if (httpParams != null) {
			Integer con_timeout = TIMEOUT * 1000;
			Integer so_timeout = TIMEOUT * 1000;
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
					con_timeout);
			httpParams
					.setParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout);

		}
		CompressHandle();
	}

	private void parseEntity(HttpEntity entity) {
		Header header = entity.getContentType();
		if (header != null) {
			HeaderElement[] elements = header.getElements();
			if (elements.length > 0) {
				HeaderElement elem = elements[0];
				responseMimetype = elem.getName();
				NameValuePair val = elem.getParameterByName("charset");
				if (null != val) {
					responseCharset = val.getValue();
				}
			}
		}
	}

	public void disableRedirectStrategy() {
		this.httpClient.setRedirectStrategy(new DisableRedirectStrategy());
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	class DisableRedirectStrategy implements RedirectStrategy {

		public HttpUriRequest getRedirect(HttpRequest arg0, HttpResponse arg1,
				HttpContext arg2) throws ProtocolException {
			return null;
		}

		public boolean isRedirected(HttpRequest arg0, HttpResponse arg1,
				HttpContext arg2) throws ProtocolException {
			return false;
		}
	}


	/**
	 * 
	 * @param base
	 * @return
	 */
	public DefaultHttpClient wrapClient(DefaultHttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			DefaultHttpClient newHttpClient = new DefaultHttpClient(ccm,
					base.getParams());
			newHttpClient.setCookieStore(base.getCookieStore());
			return newHttpClient;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public void addCookie(String name, String value, String domain, String path) {
		if (name == null || name.isEmpty())
			return;
		for (Cookie cookie : this.cookieStore.getCookies()) {
			if (name.equals(cookie.getName())) {
				return;
			}
		}
		BasicClientCookie newCookie = new BasicClientCookie(name, value);
		newCookie.setDomain(domain);
		newCookie.setPath(path);
		newCookie.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60
				* 60 * 1000));
		this.cookieStore.addCookie(newCookie);
	}


	public String getCookie(String name) {
		List<Cookie> cookies = this.cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName() != null
					&& cookie.getName().toLowerCase().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}


	public int getLastStatusCode() {
		return this.lastStatusCode;
	}


	public String getLocationUrl() {
		return this.LocationUrl;
	}


	private void CompressHandle() {
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(HttpResponse arg0, HttpContext arg1)
					throws HttpException, IOException {
				HttpResponse httpResponse = arg0;
				@SuppressWarnings("unused")
				HttpContext httpContext = arg1;
				decompressingEntity(httpResponse);
			}
		});
	}


	private void decompressingEntity(HttpResponse httpResponse) {
		HttpEntity entity = httpResponse.getEntity();
		Header header = entity.getContentEncoding();
		if (header != null) {
			String compressStr = header.getValue();
			if (compressStr != null) {
				compressStr = compressStr.toLowerCase();
				if (compressStr.contains("gzip")) {
					httpResponse.setEntity(new GzipDecompressingEntity(
							httpResponse.getEntity()));
				} else if (compressStr.contains("deflate")) {
					httpResponse.setEntity(new DeflateDecompressingEntity(
							httpResponse.getEntity()));
				}
			}
		}
	}


	public void finalize() {
		if (this.httpClient != null) {
			this.httpClient.getConnectionManager().shutdown();
			this.httpClient = null;
		}
	}


	private void initRequestHeader(HttpRequestBase httpRequestBase) {
		httpRequestBase.setHeader("Accept",
				"text/html, application/xhtml+xml, */*");
		httpRequestBase.setHeader("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		if (null != userAgent) {
			httpRequestBase.setHeader("User-Agent", userAgent);
		} else {
			httpRequestBase.setHeader("User-Agent", DefaultUserAgent);
		}
		httpRequestBase.setHeader("Accept-Encoding", "gzip, deflate");
		
		if (httpRequestBase instanceof HttpPost) {
			httpRequestBase.setHeader("Content-Type", contentType);
			contentType = DefaultContentType;
		}
		if (!"".equals(acceptCharset)) {
			httpRequestBase.setHeader("Accept-Charset", acceptCharset);
			acceptCharset = "";
		}
		if (!"".equals(accept)) {
			httpRequestBase.setHeader("Accept", accept);
			accept = "";
		}
		if (!"".equals(requestWith)) {
			httpRequestBase.setHeader("x-requested-with", requestWith);
		}
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public HttpResponse doGet(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ClientProtocolException, IOException {
		
		if (requestEntity != null && requestEntity.size() > 0) {
			if (targetUrl.indexOf("?") < 0)
				targetUrl += "?";
			for (NameValuePair item : requestEntity) {
				if (item.getName() != null && !item.getName().isEmpty()
						&& item.getValue() != null) {
					if (!targetUrl.endsWith("?") && !targetUrl.endsWith("&"))
						targetUrl += "&";
					targetUrl += String.format("%s=%s", item.getName(),
							java.net.URLEncoder.encode(item.getValue(),
									requestEntityCharset));
				}
			}
		}

		HttpGet httpGet = new HttpGet(targetUrl);
		this.initRequestHeader(httpGet);
		if (referUrl != null && referUrl.length() > 0) {
			httpGet.addHeader("Referer", referUrl);
		}
		
		if (requestHeaders != null && requestHeaders.size() > 0) {
			for (NameValuePair item : requestHeaders) {
				if (item.getName() != null && !item.getName().isEmpty()
						&& item.getValue() != null) {
					if (!httpGet.containsHeader(item.getName())) {
						httpGet.addHeader(item.getName(), item.getValue());
					} else {
						httpGet.setHeader(item.getName(), item.getValue());
					}
				}
			}
		}
		
		HttpResponse httpResponse = null;
        if (targetUrl.toLowerCase().startsWith("https")) {
			DefaultHttpClient newHttpClient = wrapClient(httpClient);
			httpResponse = newHttpClient.execute(httpGet);
			httpClient.setCookieStore(newHttpClient.getCookieStore());
		} else {
			
			httpResponse = httpClient.execute(httpGet);
            System.out.println(httpResponse);
        }
		this.lastStatusCode = httpResponse.getStatusLine().getStatusCode();
		if (this.lastStatusCode == 301 || this.lastStatusCode == 302) {
			Header header = httpResponse.getFirstHeader("Location");
			if (header != null) {
				this.LocationUrl = header.getValue();
			}
		}
		this.decompressingEntity(httpResponse);
		return httpResponse;
	}

	public String do302Get4Html(String targetUrl, String referUrl)
			throws ClientProtocolException, IOException {
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(targetUrl);
		this.initRequestHeader(httpGet);
		if (referUrl != null && referUrl.length() > 0) {
			httpGet.addHeader("Referer", referUrl);
		}
	    
		HttpResponse httpResponse = null;
		httpResponse = httpClient.execute(httpGet, localContext);
		this.setHost(((HttpHost) localContext
				.getAttribute(ExecutionContext.HTTP_TARGET_HOST)).getHostName());
		this.decompressingEntity(httpResponse);
		HttpEntity httpEntity = httpResponse.getEntity();
		parseEntity(httpEntity);
		return EntityUtils.toString(httpEntity);
	}

	public String doGet4Html(String targetUrl) throws ParseException,
			IOException {
		return this.doGet4Html(targetUrl, null);
	}

	public String doGet4Html(String targetUrl, String referUrl)
			throws ParseException, IOException {
		return this.doGet4Html(targetUrl, referUrl, null);
	}

	public String doGet4Html(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws ParseException,
			IOException {
		return this.doGet4Html(targetUrl, referUrl, requestHeaders, null, null);
	}

	public String doGet4Html(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ParseException, IOException {
		HttpResponse httpResponse = this.doGet(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		parseEntity(httpEntity);
		return EntityUtils.toString(httpEntity);
	}
	
	public String doGet4HtmlEncoding(String targetUrl, String referUrl)
			throws ParseException, IOException {
		HttpResponse httpResponse = this.doGet(targetUrl, referUrl, null, null,
				null);
		HttpEntity httpEntity = httpResponse.getEntity();
		parseEntity(httpEntity);
		return EntityUtils.toString(httpEntity);
	}

	public String getResponseCharset() {
		return responseCharset;
	}

	public String getResponseMimetype() {
		return responseMimetype;
	}


	public InputStream doGet4Stream(String targetUrl)
			throws ClientProtocolException, IOException {
		return this.doGet4Stream(targetUrl, null);
	}
	
	public InputStream doGet4Stream(String targetUrl, String referUrl)
			throws ClientProtocolException, IOException {
		return this.doGet4Stream(targetUrl, referUrl, null);
	}


	public InputStream doGet4Stream(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws ClientProtocolException,
			IOException {
		return this.doGet4Stream(targetUrl, referUrl, requestHeaders, null,
				null);
	}


	public InputStream doGet4Stream(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ClientProtocolException, IOException {
		HttpResponse httpResponse = this.doGet(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return httpEntity.getContent();
	}


	public byte[] doGet4Bytes(String targetUrl) throws IOException {
		return this.doGet4Bytes(targetUrl, null);
	}


	public byte[] doGet4Bytes(String targetUrl, String referUrl)
			throws IOException {
		return this.doGet4Bytes(targetUrl, referUrl, null);
	}

	public byte[] doGet4Bytes(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws IOException {
		return this
				.doGet4Bytes(targetUrl, referUrl, requestHeaders, null, null);
	}


	public byte[] doGet4Bytes(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws IOException {
		HttpResponse httpResponse = this.doGet(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toByteArray(httpEntity);
	}


	public HttpResponse doPost(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(targetUrl);
		this.initRequestHeader(httpPost);
		if (referUrl != null && referUrl.length() > 0) {
			httpPost.addHeader("Referer", referUrl);
		}
		
		if (requestHeaders != null && requestHeaders.size() > 0) {
			for (NameValuePair item : requestHeaders) {
				if (item.getName() != null && !item.getName().isEmpty()
						&& item.getValue() != null) {
					httpPost.addHeader(item.getName(), item.getValue());
				}
			}
		}
		
		if (requestEntity != null && requestEntity.size() > 0) {
			UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(
					requestEntity, requestEntityCharset);
			httpPost.setEntity(urlEntity);
		}
		
		HttpResponse httpResponse = null;
		if (targetUrl.toLowerCase().startsWith("https")) {
			DefaultHttpClient newHttpClient = wrapClient(httpClient);
			httpResponse = newHttpClient.execute(httpPost);
			httpClient.setCookieStore(newHttpClient.getCookieStore());
		} else {
		
			httpResponse = httpClient.execute(httpPost);
		}
		this.lastStatusCode = httpResponse.getStatusLine().getStatusCode();
		if (this.lastStatusCode == 301 || this.lastStatusCode == 302) {
			Header header = httpResponse.getFirstHeader("Location");
			if (header != null) {
				this.LocationUrl = header.getValue();
			}
		}
		this.decompressingEntity(httpResponse);
		return httpResponse;
	}

	public HttpResponse doPost(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders, byte[] requestBytes,
			String requestEntityCharset) throws ClientProtocolException,
			IOException {
		HttpPost httpPost = new HttpPost(targetUrl);
		this.initRequestHeader(httpPost);
		if (referUrl != null && referUrl.length() > 0) {
			httpPost.addHeader("Referer", referUrl);
		}
		if (requestHeaders != null && requestHeaders.size() > 0) {
			for (NameValuePair item : requestHeaders) {
				if (item.getName() != null && !item.getName().isEmpty()
						&& item.getValue() != null) {
					httpPost.addHeader(item.getName(), item.getValue());
				}
			}
		}
		if (requestBytes != null) {
			ByteArrayEntity byteArr = new ByteArrayEntity(requestBytes);
			httpPost.setEntity(byteArr);
		}
		HttpResponse httpResponse = null;
		if (targetUrl.toLowerCase().startsWith("https")) {
			DefaultHttpClient newHttpClient = wrapClient(httpClient);
			httpResponse = newHttpClient.execute(httpPost);
			httpClient.setCookieStore(newHttpClient.getCookieStore());
		} else {
			httpResponse = httpClient.execute(httpPost);
		}
		this.lastStatusCode = httpResponse.getStatusLine().getStatusCode();
		if (this.lastStatusCode == 301 || this.lastStatusCode == 302) {
			Header header = httpResponse.getFirstHeader("Location");
			if (header != null) {
				this.LocationUrl = header.getValue();
			}
		}
		this.decompressingEntity(httpResponse);
		return httpResponse;
	}


	public String doPost4Html(String targetUrl) throws ParseException,
			IOException {
		return this.doPost4Html(targetUrl, null);
	}

		public String doPost4Html(String targetUrl, String referUrl)
			throws ParseException, IOException {
		return this.doPost4Html(targetUrl, referUrl, null);
	}

	
	public String doPost4Html(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws ParseException,
			IOException {
		return this.doPost4Html(targetUrl, referUrl, requestHeaders, "", null);
	}

    public String doPost4Html(String targetUrl, String referUrl,
                              List<NameValuePair> requestHeaders,
                              List<NameValuePair> requestEntity) throws IOException {
        /**HttpResponse response = this.doPost(targetUrl, targetUrl, requestHeaders, requestEntity, "UTF-8");
        Header headCode = response.getFirstHeader("code");
        if (headCode == null) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("code", ExceptionConst.HTTP_TIMEOUT_ERR.code);
            m.put("message", ExceptionConst.HTTP_TIMEOUT_ERR.message);
            return JsonUtils.toJSONString(m);
        }
        String codeValue = headCode.getValue();
        if (codeValue == null || !codeValue.toString().equals("0")) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("code", ExceptionConst.HTTP_TIMEOUT_ERR.code);
            m.put("message", ExceptionConst.HTTP_TIMEOUT_ERR.message);
            return JsonUtils.toJSONString(m);
        } else {
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            if (result != null && !result.equals("")) {
                return result;
            } else {
                return "{}";
            }
        }*/
        return this.doPost4Html(targetUrl, referUrl, requestHeaders, requestEntity, "UTF-8");
    }

    public String doPost4Html(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ParseException, IOException {
		HttpResponse httpResponse = this.doPost(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity);
	}

	
	public String doPost4Html(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders, String requestContent,
			String requestEntityCharset) throws ParseException, IOException {
		byte[] postData = null;
		if (requestContent != null && !requestContent.isEmpty()
				&& requestEntityCharset != null) {
			postData = requestContent.getBytes(requestEntityCharset);
		}
		HttpResponse httpResponse = this.doPost(targetUrl, referUrl,
				requestHeaders, postData, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity);
	}

	
	public InputStream doPost4Stream(String targetUrl)
			throws ClientProtocolException, IOException {
		return this.doPost4Stream(targetUrl, null);
	}

	
	public InputStream doPost4Stream(String targetUrl, String referUrl)
			throws ClientProtocolException, IOException {
		return this.doPost4Stream(targetUrl, referUrl, null);
	}

	
	public InputStream doPost4Stream(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws ClientProtocolException,
			IOException {
		return this.doPost4Stream(targetUrl, referUrl, requestHeaders, null,null);
	}

	
	public InputStream doPost4Stream(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws ClientProtocolException, IOException {
		HttpResponse httpResponse = this.doPost(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return httpEntity.getContent();
	}

	
	public byte[] doPost4Bytes(String targetUrl) throws IOException {
		return this.doPost4Bytes(targetUrl, null);
	}

	
	public byte[] doPost4Bytes(String targetUrl, String referUrl)
			throws IOException {
		return this.doPost4Bytes(targetUrl, referUrl, null);
	}

	
	public byte[] doPost4Bytes(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders) throws IOException {
		return this.doPost4Bytes(targetUrl, referUrl, requestHeaders, null,null);
	}

	
	public byte[] doPost4Bytes(String targetUrl, String referUrl,
			List<NameValuePair> requestHeaders,
			List<NameValuePair> requestEntity, String requestEntityCharset)
			throws IOException {
		HttpResponse httpResponse = this.doPost(targetUrl, referUrl,
				requestHeaders, requestEntity, requestEntityCharset);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toByteArray(httpEntity);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public JSONObject httpRequest(String requestUrl, String requestMethod) {
	    JSONObject jsonObject = null;
	    StringBuffer buffer = new StringBuffer();
	    try {
	 
	      URL url = new URL(requestUrl);
	      // http协议传输
	      HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	 
	      httpUrlConn.setDoOutput(true);
	      httpUrlConn.setDoInput(true);
	      httpUrlConn.setUseCaches(false);
	      // 设置请求方式（GET/POST）
	      httpUrlConn.setRequestMethod(requestMethod);
	 
	      if ("GET".equalsIgnoreCase(requestMethod))
	        httpUrlConn.connect();
	      // 将返回的输入流转换成字符串
	      InputStream inputStream = httpUrlConn.getInputStream();
	      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	 
	      String str = null;
	      while ((str = bufferedReader.readLine()) != null) {
	        buffer.append(str);
	      }
	      bufferedReader.close();
	      inputStreamReader.close();
	      // 释放资源
	      inputStream.close();
	      inputStream = null;
	      httpUrlConn.disconnect();
	      jsonObject = JSONObject.parseObject(buffer.toString());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return jsonObject;
	  }
	
	public static void main(String[] args) throws Exception{
		/*WebClient client = new WebClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("entry", "weibo"));
		params.add(new BasicNameValuePair("gateway", "1"));
		params.add(new BasicNameValuePair("from", ""));
		params.add(new BasicNameValuePair("savestate", "7"));
		params.add(new BasicNameValuePair("useticket", "1"));
		params.add(new BasicNameValuePair("paperefer", ""));
		params.add(new BasicNameValuePair("vsnf", "1"));
		String page = client.doPost4Html("http://127.0.0.1:9012/myHttpService?a=1", null, null, params, "UTF-8");*/
	}
}
