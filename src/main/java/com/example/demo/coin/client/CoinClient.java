package com.example.demo.coin.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.coin.model.CurrentPrice;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * Coin Client
 */
@Service(value = "coinClient")
@Slf4j
public class CoinClient {

	/** 幣值url */
	public static final String PRICE_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";

	@Autowired
	private RestTemplate restTemplate;

	/** gson */
	private static final Gson gson = new Gson();

	public CurrentPrice read_price() {
		CurrentPrice result = null;
		CloseableHttpClient httpClient = null;
		HttpResponse response = null;
		//
		try {
			httpClient = HttpClients.createDefault();
			//
			HttpGet httpGet = new HttpGet(PRICE_URL);
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

			response = httpClient.execute(httpGet);
			//
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			//
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				if (StringUtils.isEmpty(body)) {
					throw new Exception();
				}
				result = gson.fromJson(body, CurrentPrice.class);
			} else {
				log.info("body: " + body);
				log.error("statusCode" + response.getStatusLine().getStatusCode());
				log.error("statusLine: " + response.getStatusLine());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

}
