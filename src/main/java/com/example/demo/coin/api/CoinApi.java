package com.example.demo.coin.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.coin.client.CoinClient;
import com.example.demo.coin.model.Coin;
import com.example.demo.coin.model.CurrentPrice;
import com.example.demo.coin.service.CoinService;

/**
 * 幣別 控制器
 * 
 * Coin API
 */
@RestController
public class CoinApi {

	@Autowired
	private CoinService coinService;

	@Autowired
	private CoinClient coinClient;

	public CoinApi() {

	}

	/**
	 * 查詢幣值
	 * 
	 * @param code 幣值碼
	 * @param name 幣值名稱
	 * @param rate 幣值比率
	 * @return
	 */
	@GetMapping(value = { "/coin/find" })
	public ResponseEntity<Coin> readCoin(@RequestParam String code) {
		ResponseEntity<Coin> result = null;
		//
		try {
			Coin coin = coinService.findByCode(code);
			//
			result = ResponseEntity.ok().body(coin);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}
	
	/**
	 * 新增幣值
	 * 
	 * @param code 幣值碼
	 * @param name 幣值名稱
	 * @param rate 幣值比率
	 * @return
	 */
	@PostMapping(value = { "/coin/create" })
	public ResponseEntity<Coin> createCoin(@RequestParam String code, @RequestParam String name,
			@RequestParam double rate) {
		ResponseEntity<Coin> result = null;
		//
		try {
			Coin coin = coinService.insertCoin(code, name, rate);
			//
			result = ResponseEntity.ok().body(coin);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}
	
	/**
	 * 更新幣值
	 * 
	 * @param code 幣值碼
	 * @param name 幣值名稱
	 * @param rate 幣值比率
	 * @return
	 */
	@PutMapping(value = { "/coin/update" })
	public ResponseEntity<Coin> updateCoin(@RequestParam String code, @RequestParam String name,
			@RequestParam double rate) {
		ResponseEntity<Coin> result = null;
		//
		try {
			Coin coin = coinService.updateCoin(code, name, rate);
			//
			result = ResponseEntity.ok().body(coin);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}
	
	/**
	 * 刪除幣值
	 * 
	 * @param code 幣值碼
	 * @return
	 */
	@DeleteMapping(value = { "/coin/delete" })
	public ResponseEntity<String> deleteCoin(@RequestParam String code) {
		ResponseEntity<String> result = null;
		//
		try {
			int delete = coinService.deleteCoin(code);
			//
			result = ResponseEntity.ok().body("刪除成功");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}
	
	/**
	 * 查詢coindesk
	 * 
	 * @param code 幣值碼
	 * @param name 幣值名稱
	 * @param rate 幣值比率
	 * @return
	 */
	@GetMapping(value = { "/coin/desk" })
	public ResponseEntity<CurrentPrice> readCoinDesk() {
		ResponseEntity<CurrentPrice> result = null;
		//
		try {
			CurrentPrice currentPrice = coinClient.read_price();
			//
			result = ResponseEntity.ok().body(currentPrice);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}
	
	/**
	 * 轉換coindesk
	 * 
	 * @param code 幣值碼
	 * @param name 幣值名稱
	 * @param rate 幣值比率
	 * @return
	 */
	@PostMapping(value = { "/coin/desk/convert" })
	public ResponseEntity<Coin> convertCoinDesk() {
		ResponseEntity<Coin> result = null;
		//
		try {
			Coin coin = coinService.convert();
			//
			result = ResponseEntity.ok().body(coin);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 帳號格式錯誤
			result = ResponseEntity.notFound().build();
		}
		//
		return result;
	}

}
