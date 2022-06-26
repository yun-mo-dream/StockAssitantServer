package com.storage;

import java.util.ArrayList;

import com.storage.trade.DayTradeResponse;

import log.LogUtil;
import queryinterface.QueryInterface;
public class ImpQueryData implements QueryInterface {

	@Override
	public DayTradeResponse querySingle(String code, String date) {
		LogUtil.logD("ImpQueryData", "start query code="+code+",date="+date);
		String dataPathString = FileUtil.getDataPath(code, date);
		LogUtil.logD("ImpQueryData", "dataPath="+dataPathString);
		DayTradeResponse response = FileUtil.parasDataFromCsvFile(code,date,dataPathString);
		return response;
	}

	@Override
	public ArrayList<String> querySingle(String code, String fromDate, String endDate) {

		return null;
	}

	
}


