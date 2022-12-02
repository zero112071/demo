package com.example.demo.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestTemplateConfig {

	@Value("${http.maxTotal}")
	private Integer maxTotal;

	@Value("${http.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;

	@Value("${http.connectTimeout}")
	private Integer connectTimeout;

	@Value("${http.connectionRequestTimeout}")
	private Integer connectionRequestTimeout;

	@Value("${http.socketTimeout}")
	private Integer socketTimeout;

	@Value("${http.staleConnectionCheckEnabled}")
	private boolean staleConnectionCheckEnabled;

	@Value("${http.validateAfterInactivity}")
	private Integer validateAfterInactivity;

	@Value("${http.retryCount}")
	private Integer retryCount;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
		// 可以添加消息轉換
		// restTemplate.setMessageConverters(messageConverters);
		// 可以增加攔截器
		// restTemplate.setInterceptors(interceptors);
		restTemplate.getMessageConverters().add(new TextHttpMessageConverter());
		return restTemplate;
	}

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	@Bean
	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		// 最大連接數
		connectionManager.setMaxTotal(maxTotal);
		// 單個路由最大連接數
		connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		// 最大空間時間
		connectionManager.setValidateAfterInactivity(validateAfterInactivity);
		// 伺服器返回資料(response)的時間，超過拋出read timeout
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout) // 連接上伺服器(握手成功)的時間，超出拋出connect timeout
				// 提交前檢測是否可用
				// .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled)
				// 從連接池中獲取連接的超時時間，超時間未拿到可用連接，會拋出org.apache.http.conn.ConnectionPoolTimeoutException:
				// Timeout waiting for connection from pool
				.setConnectionRequestTimeout(connectionRequestTimeout).build();

		// 重試次數
		DefaultHttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(retryCount, true,
				new ArrayList<>()) {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				log.info("Retry request, execution count: {}, exception: {}", executionCount, exception);
				return super.retryRequest(exception, executionCount, context);
			}
		};
		return HttpClientBuilder.create() //
				.setDefaultRequestConfig(requestConfig) //
				.setConnectionManager(connectionManager) //
				.setRetryHandler(requestRetryHandler) //
				.build();
	}

	/**
	 * 2020/12/04 支援 text/plain
	 */
	public class TextHttpMessageConverter extends MappingJackson2HttpMessageConverter {
		public TextHttpMessageConverter() {
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(MediaType.TEXT_HTML);
			setSupportedMediaTypes(mediaTypes);
		}
	}

}
