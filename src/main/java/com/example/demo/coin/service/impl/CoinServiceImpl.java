package com.example.demo.coin.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.coin.client.CoinClient;
import com.example.demo.coin.model.Coin;
import com.example.demo.coin.model.CoinInfo;
import com.example.demo.coin.model.CurrentPrice;
import com.example.demo.coin.repository.CoinRepository;
import com.example.demo.coin.service.CoinService;
import com.example.demo.coin.user_type.BpiUserType;

/**
 * 幣別服務
 */
@Service
public class CoinServiceImpl implements CoinService {

	/**
	 * 幣別儲存庫
	 */
	@Autowired
	@Qualifier("coinRepository")
	private CoinRepository coinRepository;

	/**
	 * 幣別服務, 交易處理
	 */
	@Autowired
	@Qualifier("coinTX")
	private CoinTX coinTX;

	@Autowired
	private CoinClient coinClient;

	BpiUserType userType = new BpiUserType();

	/**
	 * TX
	 * 
	 * 新增幣別
	 * 
	 * @param coin
	 * @return
	 */
	@Override
	public Coin insert(Coin coin) {
		return coinTX.insert(coin);
	}

	/**
	 * TX
	 * 
	 * 更新幣別
	 * 
	 * @param coin
	 * @return
	 */
	@Override
	public Coin update(Coin coin) {
		return coinTX.update(coin);
	}

	/**
	 * TX
	 * 
	 * 刪除幣別
	 * 
	 * @param coin
	 */
	@Override
	public void delete(Coin coin) {
		coinTX.delete(coin);
	}

	/**
	 * 更新幣別
	 * 
	 * @param coin
	 * @return
	 */
	@Override
	public Coin findById(String id) {
		Coin result = null;
		//
		try {
			result = coinRepository.findById(id).get();
			//
			if (result == null) {
				throw new Exception("Not Exit");
			}
			
			if (result != null) {
				String bpiString = result.getBpiString();
				Map<String, CoinInfo> bpi = userType.unmarshal(bpiString, null, null);
				result.setBpi(bpi);
				
				// 設置 service 所需時間文字格式
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
				// 取得回傳格式
				String updated = sdf.format(new Date(result.getUpdatedTime()));
				// 寫入result
				result.setUpdated(updated);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 更新幣別
	 * 
	 * @param code
	 * @return
	 */
	@Override
	public Coin findByCode(String code) {
		Coin result = null;
		//
		try {
			result = coinRepository.findByCode(code);
			//
			if (result == null) {
				throw new Exception("Not Exit");
			}

			if (result != null) {
				String bpiString = result.getBpiString();
				Map<String, CoinInfo> bpi = userType.unmarshal(bpiString, null, null);
				result.setBpi(bpi);
				
				// 設置 service 所需時間文字格式
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
				// 取得回傳格式
				String updated = sdf.format(new Date(result.getUpdatedTime()));
				// 寫入result
				result.setUpdated(updated);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 新增幣別
	 * 
	 * @param code
	 * @param name
	 * @param rate
	 * @return
	 */
	@Override
	public Coin insertCoin(String code, String name, double rate) {
		Coin result = null;
		//
		try {
			// 取得本地時間
			long now = System.currentTimeMillis();
			// 取得偏移時間
			Calendar calendar = Calendar.getInstance();
			int offset = calendar.getTimeZone().getRawOffset();
			// 取得UTC時間
			long utc = now - offset;

			// 設定物件
			Coin coin = new Coin();
			coin.setCode(code);
			coin.setName(name);
			coin.setRate(rate);
			coin.setUpdatedTime(utc);

			// 寫入資料庫
			result = insert(coin);

			// 回傳
			if (result != null) {
				// 設置 service 所需時間文字格式
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
				// 取得回傳格式
				String updated = sdf.format(new Date(utc));
				// 寫入result
				result.setUpdated(updated);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 更新幣別
	 * 
	 * @param code
	 * @param name
	 * @param rate
	 * @return
	 */
	@Override
	public Coin updateCoin(String code, String name, double rate) {
		Coin result = null;
		//
		try {
			// 取得本地時間
			long now = System.currentTimeMillis();
			// 取得偏移時間
			Calendar calendar = Calendar.getInstance();
			int offset = calendar.getTimeZone().getRawOffset();
			// 取得UTC時間
			long utc = now - offset;

			// 取得資料
			Coin coin = findByCode(code);
			coin.setName(name);
			coin.setRate(rate);
			coin.setUpdatedTime(utc);

			// 更新資料庫
			result = update(coin);
			// 回傳
			if (result != null) {
				// 設置 service 所需時間文字格式
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
				// 取得回傳格式
				String updated = sdf.format(new Date(utc));
				// 寫入result
				result.setUpdated(updated);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 刪除幣別
	 * 
	 * @param code
	 * @return
	 */
	@Override
	public int deleteCoin(String code) {
		int result = 0;
		//
		try {
			// 取得資料
			Coin coin = findByCode(code);

			// 刪除資料
			delete(coin);

			// 回傳
			result = 1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 轉換資料
	 * 
	 * @return
	 */
	@Override
	public Coin convert() {
		Coin result = null;
		//
		try {
			CurrentPrice currentPrice = coinClient.read_price();
			String bpiString = userType.marshal(currentPrice.getBpi(), null);

			// 設置 service 所需時間文字格式
			SimpleDateFormat sdf_iso = new SimpleDateFormat(DATE_ISO_PATTERN);
			// 轉換時間
			long utc = (sdf_iso.parse(currentPrice.getTime().getUpdatedISO())).getTime();

			// 建立新物件
			Coin coin = new Coin();
			coin.setCode(currentPrice.getChartName());
			coin.setName(currentPrice.getChartName());
			coin.setUpdatedTime(utc);
			coin.setBpiString(bpiString);

			// 寫入資料庫
			result = insert(coin);

			// 回傳
			if (result != null) {
				// 設置 service 所需時間文字格式
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
				// 取得回傳格式
				String updated = sdf.format(new Date(utc));
				// 寫入result
				result.setUpdated(updated);
				result.setBpi(currentPrice.getBpi());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return result;

	}

}
