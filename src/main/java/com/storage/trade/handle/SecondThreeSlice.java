package com.storage.trade.handle;

import com.storage.trade.TradeType;

/**
 * ����ʱ��Ƭ
 * 
 * @author Administrator
 *
 */
public class SecondThreeSlice extends AbsTimeSlice<SecondThreeSlice.ThreeSlice> {
	// �ɼ۲��������ֵ
	private float priceWaveMax = 0f;
	// ���������������ֵ
	private float netMoneyMax = 0f;
	// �������̼�
	private float yesEndPrice = 0f;

	@Override
	public void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney) {
		super.tradeAction(tradeTimeString, tradePriceFloat, tradeVolumeLong, tradeTypeString, tradeMoney);
		float nowNetMoney = currentSliceT.getNetMoney();
		netMoneyMax = Math.max(netMoneyMax, Math.abs(nowNetMoney));
		priceWaveMax = Math.max(priceWaveMax, Math.abs(tradePriceFloat - yesEndPrice));
	}

	@Override
	public SecondThreeSlice.ThreeSlice getNewSlice(String tradeTimeString, SecondThreeSlice.ThreeSlice preSliceTl) {
		return new ThreeSlice(tradeTimeString, preSliceTl);
	}

	public static class ThreeSlice extends AbsTimeSlice.Slice {
		private int tradeSecond;

		public ThreeSlice(String tradeTimeString, ThreeSlice pre) {
			super(tradeTimeString);
			if (null != pre) {
				netMoney = pre.getNetMoney();
			}
			tradeSecond = getSecondFromTime(tradeTimeString);
		}

		// �۸�
		private float price;
		// �ۼ�����������ֵ
		private long netMoney = 0l;

		
		
		@Override
		public void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong,
				String tradeTypeString, long tradeMoney) {
			price = tradePriceFloat;
			if (TradeType.B.name().equals(tradeTypeString)) {
				netMoney += tradeMoney;
			} else {
				netMoney -= tradeMoney;
			}

		}

		@Override
		public boolean isInCurrentSlice(String tradeTimeString) {
			// ���̾��ۺ��������̾���
			if (time.startsWith("9:25")||time.startsWith("09:25")) {
				return time.equals(tradeTimeString);
			} else if (time.startsWith("15:00")) {
				return tradeTimeString.startsWith("15:00");
			} else {
				int second = getSecondFromTime(tradeTimeString);
				return second / 3 == tradeSecond / 3;
			}

		}

		private int getSecondFromTime(String tradeTimeString) {
			int secondIndex = tradeTimeString.lastIndexOf(":")+1;
			return Integer.parseInt(tradeTimeString.substring(secondIndex, tradeTimeString.length()));
		}

		public float getPrice() {
			return price;
		}

		public long getNetMoney() {
			return netMoney;
		}

	}

	public float getPriceWaveMax() {
		return priceWaveMax;
	}

	public float getNetMoneyMax() {
		return netMoneyMax;
	}

	public float getYesEndPrice() {
		return yesEndPrice;
	}
	
	
	
	

}
