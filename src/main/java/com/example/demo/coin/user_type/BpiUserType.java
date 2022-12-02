package com.example.demo.coin.user_type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.example.demo.coin.model.CoinInfo;

/**
 * 幣值匯率, 物件與欄位轉換
 */
public class BpiUserType implements Serializable {

	// 黑陶
	public static final char SPADE_CHAR = '♠';

	public static final String SPADE = String.valueOf(SPADE_CHAR);// \u2660

	// 愛心
	public static final char HEART_CHAR = '♥';

	public static final String HEART = String.valueOf(HEART_CHAR);// \u2665

	// 方塊
	public static final char DIAMOND_CHAR = '♦';

	public static final String DIAMOND = String.valueOf(DIAMOND_CHAR);// \u2666

	// 梅花
	public static final char CLUB_CHAR = '♣';

	public static final String CLUB = String.valueOf(CLUB_CHAR);// \u2663

	// 版本號
	public static final int volType = 1;

	public BpiUserType() {

	}

	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	public Class<?> returnedClass() {
		return Map.class;
	}

	// --------------------------------------------------
	// assemble
	// --------------------------------------------------
	/**
	 * 由物件組成欄位
	 */
	@SuppressWarnings("unchecked")
	public <R, T> R marshal(T value, SharedSessionContractImplementor session) {
		R result = null;
		if (!(value instanceof Map)) {
			return result;
		}
		//
		StringBuilder dest = new StringBuilder();
		Map<String, CoinInfo> src = (Map<String, CoinInfo>) value;
		// vol 寫入 ♥版號♠
		dest.append(HEART);
		dest.append(volType);
		dest.append(SPADE);
		// 寫入物件 v1
		dest.append(assembleBy_1(src));
		//
		result = (R) dest.toString();
		//
		return result;
	}

	/**
	 * v1 由物件組成欄位
	 * 
	 * @param src
	 * @return
	 */
	public String assembleBy_1(Map<String, CoinInfo> src) {
		StringBuilder result = new StringBuilder();
		// 寫入物件 size
		result.append(src != null ? src.size() : 0); // 0
		//
		for (Map.Entry<String, CoinInfo> entry : src.entrySet()) {
			// 取得幣別資訊
			CoinInfo value = entry.getValue();
			//
			result.append(SPADE);
			// 幣值碼
			result.append(value.getCode()); // e0
			result.append(DIAMOND);
			// 幣值符號
			result.append(value.getSymbol()); // e1
			result.append(DIAMOND);
			// 幣值比率
			result.append(value.getRate()); // e2
			result.append(DIAMOND);
			// 幣值描述
			result.append(value.getDescription()); // e3
			result.append(DIAMOND);
			// 幣值比率
			result.append(value.getRate_float()); // e4
		}
		//
		return result.toString();
	}

	// --------------------------------------------------
	// disassemble
	// --------------------------------------------------
	/**
	 * 由欄位反組成物件
	 */
	@SuppressWarnings("unchecked")
	public <R, T, O> R unmarshal(T value, SharedSessionContractImplementor session, O owner) {
		// 預設傳回空物件,非null
		Map<String, CoinInfo> result = null;
		//
		if (!(value instanceof String)) {
			return (R) result;
		}
		//
		StringBuilder src = new StringBuilder((String) value);
		// 取得版本號 並移除src的版本號
		int vol = disassembleVol(src);
		//
		if (vol < 1) {
			return (R) result;
		}
		// v1
		if (vol >= 1) {
			result = disassembleBy_1(src);
		}
		// v2,有新增的欄位,則繼續塞
		if (vol >= 2) {

		}
		//
		return (R) result;
	}

	/**
	 * v1 由欄位反組成物件
	 * 
	 * @param src
	 * @return
	 */
	public Map<String, CoinInfo> disassembleBy_1(StringBuilder src) {
		Map<String, CoinInfo> result = new LinkedHashMap<String, CoinInfo>();
		// 判斷欄位是否為null
		if (src == null) {
			return result;
		}
		// 分割src為字串陣列
		String[] values = src.toString().split(SPADE);
		//
		if (values == null || values.length == 0) {
			return result;
		}
		// 設定序號
		int idx = 0;
		// 取得size
		int size = Integer.parseInt(values[idx++]); // 0 取得size後 idx = 1
		//
		for (int i = 0; i < size; i++) {
			// 設定序號
			int index = 0;
			// 分割欄位
			String[] vals = values[idx++].split(DIAMOND);
			// 設定新物件
			CoinInfo info = new CoinInfo();
			// 幣值碼
			info.setCode(vals[index++]); // e0
			// 幣值符號
			info.setSymbol(vals[index++]); // e1
			// 幣值比率
			info.setRate(vals[index++]); // e2
			// 幣值描述
			info.setDescription(vals[index++]); // e3
			// 幣值比率
			info.setRate_float(Double.parseDouble(vals[index++])); // e4
			// 寫入result
			result.put(info.getCode(), info);
		}
		//
		return result;
	}

	// -----------------------------------------------------------------------------

	/**
	 * 取得vol,並將已處理過的部分移除,傳回剩下尚未處理的部分
	 * 
	 * 原:♥1♠sys♠1331780303919♠♠
	 * 
	 * 取vol=1,之後會變為新的string
	 * 
	 * 新:sys♠1331780303919♠♠
	 * 
	 * @param value
	 * @return
	 */
	protected int disassembleVol(StringBuilder value) {
		int result = 0;
		//
		int volLength = HEART.length();
		if (value != null && value.length() > volLength && value.substring(0, volLength).equals(HEART)) {
			int pos = value.indexOf(SPADE);
			if (pos > 0) {
				String vol = value.substring(volLength, pos);
				// value.delete(0, pos + SPLITTER.length());
				value.replace(0, pos + SPADE.length(), "");
				result = Integer.parseInt(vol);
			}
		}
		return result;
	}

}
