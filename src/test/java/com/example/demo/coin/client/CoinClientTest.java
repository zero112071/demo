package com.example.demo.coin.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.coin.model.Coin;
import com.example.demo.coin.model.CoinInfo;
import com.example.demo.coin.model.CurrentPrice;
import com.example.demo.coin.service.CoinService;
import com.example.demo.coin.user_type.BpiUserType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class CoinClientTest {

	@Autowired
	private CoinClient coinClient;
	
	@Autowired
	private CoinService coinService;

	BpiUserType userType = new BpiUserType();

	/**
	 * 日期樣板
	 */
	String DATE_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+00:00";

	@Test
	public void test1() throws ParseException {

		CurrentPrice result = coinClient.read_price();
		String iso = result.getTime().getUpdatedISO();

		// 設置 service 所需時間文字格式
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO_PATTERN);

		Date date = sdf.parse(iso);
		
		Calendar calendar = Calendar.getInstance();
		int offset = calendar.getTimeZone().getRawOffset();

		System.out.println("----------------------------------------");
		System.out.println("offset = " + offset);
		System.out.println("----------------------------------------");

		
	}
	
	@Test
	public void test2() throws ParseException {
		
		CurrentPrice result = coinClient.read_price();
		Map<String, CoinInfo> info = result.getBpi();
		String type = userType.marshal(info, null);
		
		String value = "♥1♠3♠USD♦&#36;♦16,959.8685♦United States Dollar♦16959.8685♠GBP♦&pound;♦14,171.5304♦British Pound Sterling♦14171.5304♠EUR♦&euro;♦16,521.3880♦Euro♦16521.388";
		Map<String, CoinInfo> unmarshal = userType.unmarshal(value, null, null);

		System.out.println("----------------------------------------");
		System.out.println("type = " + type);
		System.out.println("----------------------------------------");
		System.out.println("map = " + unmarshal);
		System.out.println("----------------------------------------");
	}
	
	@Test
	public void test3() throws ParseException {
		
		Coin result = coinService.convert();
		
		System.out.println("----------------------------------------");
		System.out.println("result = " + result);
		System.out.println("----------------------------------------");
		
	}

}
