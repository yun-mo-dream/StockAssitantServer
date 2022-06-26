package com.storage.trade.handle;

import java.util.ArrayList;

/**
 * 时间片接口
 * 
 * @author Administrator
 *
 */
public abstract class AbsTimeSlice<T extends AbsTimeSlice.Slice> implements TradeActionInterface{
	protected  ArrayList<T> sliceList = new ArrayList<T>();
	protected  T currentSliceT;
	protected  T preSliceTl;
	
	
	@Override
	public void tradeAction(String tradeTimeString, float tradePriceFloat, long tradeVolumeLong, String tradeTypeString,
			long tradeMoney) {
	     if(currentSliceT == null || !currentSliceT.isInCurrentSlice(tradeTimeString)) {
	    	 preSliceTl  = currentSliceT;
	    	 currentSliceT = getNewSlice(tradeTimeString,preSliceTl);
	    	 sliceList.add(currentSliceT);
	    	 
	     }
	     currentSliceT.tradeAction(tradeTimeString, tradePriceFloat, tradeVolumeLong, tradeTypeString, tradeMoney);
		
	}
	
	
	public abstract T getNewSlice(String tradeTimeString, T preSliceTl);
	
	
	
	public static abstract class  Slice implements TradeActionInterface{
		protected String time;
		
		public Slice(String tradeTimeString) {
			time = tradeTimeString;
		}
		/**
		 * 是否在当前时间片中
		 * 
		 * @param time
		 * @return
		 */
		public	abstract boolean isInCurrentSlice(String time);
		
		
		
		public String getTime() {
			return time;
		}
	}



	public ArrayList<T> getSliceList() {
		return sliceList;
	}

}
