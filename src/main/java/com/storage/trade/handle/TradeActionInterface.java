package com.storage.trade.handle;

public interface TradeActionInterface {
	void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney);
	
	default void  tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney,long sealOrder,long  sealVolume,float  sealPrice,long  buyOrder,long  buyVolume,float  buyPrice) {

	}
	default void tradeFinish() {}

}
