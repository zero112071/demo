package com.example.demo.coin.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 目前幣值
 */
@AllArgsConstructor
@Data
public class CurrentPrice {

	/** 更新時間 */
	private UpdateTimes time;

	/** 免責聲明 */
	private String disclaimer;

	/** 幣別名稱 */
	private String chartName;

	/** 幣值匯率 */
	private Map<String, CoinInfo> bpi;

	public CurrentPrice() {

	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		//
		builder.append("time", time);
		builder.append("disclaimer", disclaimer);
		builder.append("chartName", chartName);
		builder.append("bpi", bpi);
		//
		return builder.toString();
	}

}
