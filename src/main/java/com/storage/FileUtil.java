package com.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.storage.trade.DayTradeResponse;

import log.LogUtil;

public class FileUtil {

	public static boolean isFileExist(String path) {
		if (TextUtil.isNullOrEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return file.exists();
	}

	public static DayTradeResponse parasDataFromCsvFile(String code, String date, String csvFile) {
		String line = "";
		String cvsSplitBy = ",";
		DayTradeResponse response = new DayTradeResponse(code,date);
		if (!isFileExist(csvFile)) {
			LogUtil.logD("FileUtil", "parasDataFromCsvFile is not exist");
			return response;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				String[] item = line.split(cvsSplitBy);
				if (TextUtil.isNullOrEmpty(item)) {
					continue;
				}
				if (item[0].equals("TranID")) {
					continue;
				}
				// 成交时间
				String tradeTimeString = item[1];
				// 成交价格
				float tradePriceFloat = Float.parseFloat(item[2]);
				// 成交股数
				long tradeVolumeLong = Long.parseLong(item[3]);
				// 成交方向
				String tradeTypeString = item[6];
				// 成交金额
				long tradeMoney = (long) (tradePriceFloat * tradeVolumeLong);
				//卖出的序号
				long  sealOrder =    Long.parseLong(item[7]);
				//卖出挂的量
				long  sealVolume =Long.parseLong(item[4]);
				//卖出挂的价格
				float  sealPrice =    Float.parseFloat(item[8]);
				//买入的序号
				long  buyOrder =    Long.parseLong(item[9]);
				//买入挂的量
				long  buyVolume =Long.parseLong(item[5]);
				//买入挂的价格
				float  buyPrice =    Float.parseFloat(item[10]);
			  
				response.tradeAction(tradeTimeString, tradePriceFloat, tradeVolumeLong, tradeTypeString, tradeMoney
						,sealOrder,sealVolume,sealPrice,buyOrder,buyVolume,buyPrice);
			}

		} catch (IOException e) {
			LogUtil.logD("FileUtil", "IOException=" + e.getMessage());
		}
		response.tradeFinish();
		return response;
	}

	
	public static String getDataPath(String code, String date) {
		String dateDirString = TradeDaysHelper.getDataDir(date);
		if (TextUtil.isNullOrEmpty(dateDirString)) {
			return null;
		}
		String dataPathString = getFileFullPath(code, dateDirString);
		if (FileUtil.isFileExist(dataPathString)) {
			return dataPathString;
		}
		return null;
	}
	
	public static String getFileFullPath(String code,String dir) {
		return  dir+"\\"+code+".csv";
	}
	
	public static String getYesDataPath(String code, String date) {
		String yesDirString = TradeDaysHelper.getYesDataFileDir(date);
		if (TextUtil.isNullOrEmpty(yesDirString)) {
			return null;
		}
		String yesPathString = getFileFullPath(code, yesDirString);
		if (FileUtil.isFileExist(yesPathString)) {
			return yesPathString;
		}
		return null;
	}
	
	public static float getYesPrice(String code,String date) {
		String yesPathString = getYesDataPath(code,date);
		return FileUtil.getEndPrice(yesPathString);
	}

	public static float getEndPrice(String csvFile) {
		String lastLine = "";
		String line = "";
		String cvsSplitBy = ",";
		if (!isFileExist(csvFile)) {
			return 0f;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				lastLine = line;
			}

			String[] item = lastLine.split(cvsSplitBy);
			if (TextUtil.isNullOrEmpty(item)) {
				return 0f;
			}
			// 成交价格
			float tradePriceFloat = Float.parseFloat(item[2]);
			return tradePriceFloat;

		} catch (IOException e) {
			LogUtil.logD("FileUtil", "IOException=" + e.getMessage());
		}
		return 0f;

	}
	
	
	
}
