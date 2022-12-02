package com.example.demo.coin.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.example.demo.coin.user_type.BpiUserType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 幣別
 */
@Entity
@Table(name = "coin", indexes = { //
		@Index(name = "IDX_coin_code", columnList = "code") })
//@TypeDefs({ @TypeDef(name = "BpiUserType", typeClass = BpiUserType.class, defaultForType = Map.class) })
@AllArgsConstructor
@Data
public class Coin implements Serializable {

	/** 系统uuid, 唯一码, 不可变 */
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(length = 32)
	@Id
	private String id;

	/** 更新時間 UTC */
	@Column(columnDefinition = "bigint default 0")
	private long updatedTime;

	/** 幣別 */
	@Column(length = 50)
	private String code;

	/** 名稱 */
	@Column(length = 50)
	private String name;

	/** 幣值匯率 */
	@Column(columnDefinition = "double default 0")
	private double rate;

	/** 幣值匯率 */
	@Column(length = 1024)
	private String bpiString;
	
	/** 幣值匯率 */
	@Transient
	private Map<String, CoinInfo> bpi;
	
	/** 更新時間 */
	@Transient
	private String updated;

	public Coin() {

	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		//
		builder.append("id", id);
		builder.append("updatedTime", updatedTime);
		builder.append("code", code);
		builder.append("name", name);
		builder.append("rate", rate);
		builder.append("bpi", bpi);
		//
		return builder.toString();
	}

}
