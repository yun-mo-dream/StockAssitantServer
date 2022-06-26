package com.storage.trade;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.storage.trade.handle.SecondThreeSlice;
import com.storage.trade.handle.TradeActionInterface;

import log.LogUtil;

import com.storage.FileUtil;
import com.storage.trade.handle.MinuteSlice;

public class DayTradeResponse implements TradeActionInterface {
	// 总共交易金额
	private long tradeMoney = 0l;
	// 总共买入金额
	private long buyMoney = 0l;
	// 总共卖出金额
	private long sealMoney = 0l;
	// 股票代码
	private String code;
	// 日期
	private String date;
	// 分钟数据详情
	private MinuteSlice minuteSlice;
	// 三秒切片数据详情
	private SecondThreeSlice secondThreeSlice = new SecondThreeSlice();
	private float yesPrice;
	public DayTradeResponse(String code, String date) {
		this.code = code;
		this.date = date;
		yesPrice = FileUtil.getYesPrice(code, date);
		minuteSlice = new MinuteSlice(yesPrice);
	}

	@Override
	public void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney, long sealOrder, long sealVolume, float sealPrice, long buyOrder, long buyVolume,
			float buyPrice) {
	
		handleData(tradeTimeString, tradePriceFloat, tradeVolumeLong, tradeTypeString, tradeMoney);
		minuteSlice.tradeAction(tradeTimeString, tradePriceFloat, tradeVolumeLong, tradeTypeString, tradeMoney);
		// secondThreeSlice.tradeAction(tradeTimeString, tradePriceFloat,
		// tradeVolumeLong, tradeTypeString, tradeMoney);
		checkAddMap(buyList, buyOrder, buyVolume, tradeVolumeLong);
		checkAddMap(sealList, sealOrder, sealVolume, tradeVolumeLong);
	}
	private long netFlow = 0L;
    /**
     * 全天整体的数据概况
     * @param tradeTimeString
     * @param tradePriceFloat
     * @param tradeVolumeLong
     * @param tradeTypeString
     * @param tradeMoney
     */
	private void handleData(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney) {
		if (begainPrice == 0) {
			begainPrice = tradePriceFloat;
		}
		endPrice = tradePriceFloat;
		this.tradeMoney += tradeMoney;
		if (TradeType.B.name().equals(tradeTypeString)) {
			this.buyMoney += tradeMoney;
			netFlow  += tradeMoney;
		} else {
			this.sealMoney += tradeMoney;
			netFlow  -= tradeMoney;
		}

	}

	/**
	 * 买入列表
	 */
	ArrayList<TradeWay> buyList = new ArrayList<TradeWay>();
	/**
	 * 卖出列表
	 */
	ArrayList<TradeWay> sealList = new ArrayList<TradeWay>();
	private float begainPrice = 0f;
	private float endPrice = 0f;
	@Override
	public void tradeFinish() {
		LogUtil.logD("test--", "昨日收盘："+yesPrice+" ,今日开盘："+begainPrice+" ,今日收盘："+endPrice+",涨幅 = "+(endPrice - yesPrice)*100/yesPrice);
	   LogUtil.logD("test--", "成交总额："+tradeMoney/10000+"w ,买入总额："+buyMoney/10000+"w ,卖出总额："+buyMoney/10000+"w ,主动交易买卖差 = "+netFlow/10000+"w");
       LogUtil.logD("test--", "买入笔数："+buyList.size() +  "卖出笔数："+sealList.size()+" ,买卖笔数差 = "+(buyList.size()-sealList.size())+",未完成买入笔数="+unFinishBuyArrayList.size()+" ,未完成卖出笔数="+unFinishSealArrayList.size());
   
	}
	
	private ArrayList<TradeWay> unFinishBuyArrayList  = new ArrayList<TradeWay>();
	private ArrayList<TradeWay> unFinishSealArrayList  = new ArrayList<TradeWay>();
	/**
	 * 买入卖出成交的挂单整合
	 * 
	 * @param list
	 * @param order
	 * @param volume
	 * @param tradeVolume
	 */
	private void checkAddMap(ArrayList<TradeWay> list, long order, long volume, long tradeVolume) {
		if (getLastOrder(list) < order) {
			TradeWay way = new TradeWay(order, volume);
			way.tradeVolume = tradeVolume;
			list.add(way);
			if (volume > tradeVolume) {
				if (list == buyList) {
					unFinishBuyArrayList.add(way);
				}else {
					unFinishSealArrayList.add(way);
				}
			}
		} else {
			for (int i = list.size() - 1; i >= 0; i--) {
				TradeWay tradeWay = list.get(i);
				if (tradeWay.order == order) {
					tradeWay.tradeVolume += tradeVolume;
				
					if (tradeWay.tradeVolume == tradeWay.volume) {
						if (list == buyList) {
							unFinishBuyArrayList.remove(tradeWay);
						}else {
							unFinishSealArrayList.remove(tradeWay);
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * 获取列表的最后一个交易order
	 * 
	 * @param list
	 * @return
	 */
	private long getLastOrder(ArrayList<TradeWay> list) {
		if (list.size() == 0) {
			return -1;
		}
		return list.get(list.size() - 1).order;
	}

	public String generateResponseJsonString() {
		return JSON.toJSONString(DayTradeResponse.this);

	}

	public long getTradeMoney() {
		return tradeMoney;
	}

	public long getBuyMoney() {
		return buyMoney;
	}

	public long getSealMoney() {
		return sealMoney;
	}

	public MinuteSlice getMinuteSlice() {
		return minuteSlice;
	}

	public String getCode() {
		return code;
	}

	public String getDate() {
		return date;
	}

	@Override
	public void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney) {
		// TODO Auto-generated method stub
		
	}

//	public SecondThreeSlice getSecondThreeSlice() {
//		return secondThreeSlice;
//	}

}
