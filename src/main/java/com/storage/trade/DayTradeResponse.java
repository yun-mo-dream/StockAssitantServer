package com.storage.trade;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.storage.trade.handle.SecondThreeSlice;
import com.storage.trade.handle.TradeActionInterface;

import log.LogUtil;

import com.storage.FileUtil;
import com.storage.trade.handle.MinuteSlice;

public class DayTradeResponse implements TradeActionInterface {
	// �ܹ����׽��
	private long tradeMoney = 0l;
	// �ܹ�������
	private long buyMoney = 0l;
	// �ܹ��������
	private long sealMoney = 0l;
	// ��Ʊ����
	private String code;
	// ����
	private String date;
	// ������������
	private MinuteSlice minuteSlice;
	// ������Ƭ��������
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
     * ȫ����������ݸſ�
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
	 * �����б�
	 */
	ArrayList<TradeWay> buyList = new ArrayList<TradeWay>();
	/**
	 * �����б�
	 */
	ArrayList<TradeWay> sealList = new ArrayList<TradeWay>();
	private float begainPrice = 0f;
	private float endPrice = 0f;
	@Override
	public void tradeFinish() {
		LogUtil.logD("test--", "�������̣�"+yesPrice+" ,���տ��̣�"+begainPrice+" ,�������̣�"+endPrice+",�Ƿ� = "+(endPrice - yesPrice)*100/yesPrice);
	   LogUtil.logD("test--", "�ɽ��ܶ"+tradeMoney/10000+"w ,�����ܶ"+buyMoney/10000+"w ,�����ܶ"+buyMoney/10000+"w ,�������������� = "+netFlow/10000+"w");
       LogUtil.logD("test--", "���������"+buyList.size() +  "����������"+sealList.size()+" ,���������� = "+(buyList.size()-sealList.size())+",δ����������="+unFinishBuyArrayList.size()+" ,δ�����������="+unFinishSealArrayList.size());
   
	}
	
	private ArrayList<TradeWay> unFinishBuyArrayList  = new ArrayList<TradeWay>();
	private ArrayList<TradeWay> unFinishSealArrayList  = new ArrayList<TradeWay>();
	/**
	 * ���������ɽ��Ĺҵ�����
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
	 * ��ȡ�б�����һ������order
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
