package com.example.demo.coin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.coin.model.Coin;
import com.example.demo.coin.repository.CoinRepository;

/**
 * 幣別服務, 交易處理
 */
@Service(value = "coinTX")
public class CoinTX {
	
	/**
	 * 幣別儲存庫
	 */
	@Autowired
	@Qualifier("coinRepository")
	private CoinRepository coinRepository;
	
	public CoinTX() {
		
	}
	
	/**
	 * TX
	 * 
	 * 新增幣別
	 * 
	 * @param coin
	 * @return
	 */
	@Transactional
	public Coin insert(Coin coin) {
		return coinRepository.save(coin);
	}
	
	/**
	 * TX
	 * 
	 * 更新幣別
	 * 
	 * @param coin
	 * @return
	 */
	@Transactional
	public Coin update(Coin coin) {
		return coinRepository.save(coin);
	}
	
	/**
	 * TX
	 * 
	 * 刪除幣別
	 * 
	 * @param coin
	 */
	@Transactional
	public void delete(Coin coin) {
		coinRepository.delete(coin);
	}

}
