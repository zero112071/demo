package com.example.demo.coin.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 幣值資訊
 */
@AllArgsConstructor
@Data
public class CoinInfo implements Serializable {
	
	/** 幣值碼 */
	private String code;
	
	/** 幣值符號 */
	private String symbol;
	
	/** 幣值比率 */
	private String rate;
	
	/** 幣值描述 */
	private String description;
	
	/** 幣值比率 */
	private double rate_float;
	
	public CoinInfo() {
		
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		//
		builder.append("code", code);
		builder.append("symbol", symbol);
		builder.append("rate", rate);
		builder.append("description", description);
		builder.append("rate_float", rate_float);
		//
		return builder.toString();
	}

}
