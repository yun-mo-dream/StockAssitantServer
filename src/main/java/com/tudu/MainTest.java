package com.tudu;

import com.storage.ImpQueryData;
import com.storage.trade.DayTradeResponse;


public class MainTest {
	public static void main(String[] args) {

		ImpQueryData impQueryData = new ImpQueryData();
		DayTradeResponse  response = impQueryData.querySingle("000001","20220128");
		System.out.println("respnse=" + response.generateResponseJsonString());
	}
}
