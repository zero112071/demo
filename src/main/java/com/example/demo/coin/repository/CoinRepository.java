package com.example.demo.coin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.coin.model.Coin;

/**
 * 幣別儲存庫
 */
@Repository(value ="coinRepository")
public interface CoinRepository extends JpaRepository<Coin, String>{
	
	/**
	 * 從code, 搜尋幣別
	 * 
	 * @param code
	 * @return
	 */
	Coin findByCode(String code);

}
