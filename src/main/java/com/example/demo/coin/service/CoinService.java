package com.example.demo.coin.service;

import com.example.demo.coin.model.Coin;

/**
 * 幣別服務
 */
public interface CoinService {
	
	
	/**
	 * 日期樣板 ISO
	 */
	String DATE_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+00:00";
	
	/**
	 * 日期樣板
	 */
	String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * TX
	 * 
	 * 新增幣別
	 * 
	 * @param coin
	 * @return
	 */
	Coin insert(Coin coin);
	
	/**
	 * TX
	 * 
	 * 更新幣別
	 * 
	 * @param coin
	 * @return
	 */
	Coin update(Coin coin);
	
	/**
	 * TX
	 * 
	 * 刪除幣別
	 * 
	 * @param coin
	 */
	void delete(Coin coin);
	
	/**
	 * 更新幣別
	 * 
	 * @param coin
	 * @return
	 */
	Coin findById(String id);
	
	/**
	 * 更新幣別
	 * 
	 * @param code
	 * @return
	 */
	Coin findByCode(String code);
	
	/**
	 * 更新幣別
	 * 
	 * @param code
	 * @return
	 */
	Coin findByCodeQuietly(String code);
	
	/**
	 * 新增幣別
	 * 
	 * @param code
	 * @param name
	 * @param rate
	 * @return
	 */
	Coin insertCoin(String code, String name, double rate);
	
	/**
	 * 更新幣別
	 * 
	 * @param code
	 * @param name
	 * @param rate
	 * @return
	 */
	Coin updateCoin(String code, String name, double rate);
	
	/**
	 * 刪除幣別
	 * 
	 * @param code
	 * @return
	 */
	int deleteCoin(String code);
	
	/**
	 * 轉換資料
	 * 
	 * @return
	 */
	Coin convert();

}
