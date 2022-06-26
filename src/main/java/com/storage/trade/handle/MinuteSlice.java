package com.storage.trade.handle;

import com.storage.trade.TradeType;

import log.LogUtil;

public class MinuteSlice extends AbsTimeSlice<MinuteSlice.Minute> {

	public MinuteSlice(float yesPrice) {
      this.yesEndPrice = yesPrice;
	}

	// 股价波动的最大值
	private float priceWaveMax = 0f;
	// 净流入流出的最大值
	private float netMoneyMax = 0f;
	// 昨日收盘价
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
	public MinuteSlice.Minute getNewSlice(String tradeTimeString, MinuteSlice.Minute preSliceTl) {
		return new Minute(tradeTimeString, preSliceTl);
	}

	public static class Minute extends AbsTimeSlice.Slice {
		private int tradeMinute;

		public Minute(String tradeTimeString, Minute pre) {
			super(tradeTimeString);
			if (null != pre) {
				netMoney = pre.getNetMoney();
			}
			tradeMinute = getMinuteString(tradeTimeString);
		}

		// 价格
		private float price;
		// 累计流入流出净值
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
			return tradeMinute == getMinuteString(tradeTimeString);

		}

		private int getMinuteString(String tradeTimeString) {

			int startIndex = tradeTimeString.indexOf(":") + 1;
			int endIndex = tradeTimeString.lastIndexOf(":");
			return Integer.parseInt(tradeTimeString.substring(startIndex, endIndex));
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
