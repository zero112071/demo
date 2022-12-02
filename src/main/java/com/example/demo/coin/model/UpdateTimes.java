package com.example.demo.coin.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 更新時間
 */
@AllArgsConstructor
@Data
public class UpdateTimes {
	
	/** UTC時間 */
	private String updated;
	
	/** ISO時間 */
	private String updatedISO;
	
	/** GMT時間 */
	private String updateduk;
	
	public UpdateTimes() {
		
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		//
		builder.append("updated", updated);
		builder.append("updatedISO", updatedISO);
		builder.append("updateduk", updateduk);
		//
		return builder.toString();
	}

}
