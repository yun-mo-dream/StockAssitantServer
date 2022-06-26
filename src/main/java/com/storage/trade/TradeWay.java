package com.storage.trade;

public class TradeWay {
	long order;
	long volume;
	long tradeVolume;
	public TradeWay(long order,long volume) {
		this.order = order;
		this.volume = volume;
	}
	
	public void addTrade(long tradeVolume) {
		this.tradeVolume += tradeVolume;
	}
}
