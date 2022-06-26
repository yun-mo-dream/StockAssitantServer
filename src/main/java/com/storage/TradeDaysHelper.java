package com.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TradeDaysHelper {

	static final String D_Stroage_1 = "D:\\stock-data\\full-detail\\data_1";
	static final String D_Stroage_2 = "D:\\stock-data\\full-detail\\data_3";
	static final String E_Stroage_1 = "E:\\stock-data\\full-detail\\data_2";

	public static HashMap<String, String> sdatePathMap = null;
	public static ArrayList<String> tradeDaysList = new ArrayList<String>();

	/**
	 * 获取文件路径
	 * 
	 * @param date
	 * @return
	 */
	public static String getDataDir(String date) {
		if (null == sdatePathMap) {
			geneateDataPathMap();
		}
		return sdatePathMap.get(date);
	}

	/**
	 * 遍历各个盘寻找数据，并且存入map中
	 */
	private static void geneateDataPathMap() {
		sdatePathMap = new HashMap<String, String>();
		geneateDataPathMap(D_Stroage_1);
		geneateDataPathMap(E_Stroage_1);
		geneateDataPathMap(D_Stroage_2);
		System.out.println("sdatePathMap size--" + sdatePathMap.size());

	}

	/**
	 * 遍历指定寻找数据，并且存入map中
	 */
	private static void geneateDataPathMap(String root) {

		File rootDirFile = new File(root);
		if (!rootDirFile.isDirectory()) {
			return;
		}
		File[] childFiles = rootDirFile.listFiles();
		if (childFiles != null && childFiles.length > 0) {
			for (int i = 0; i < childFiles.length; i++) {
				File child = childFiles[i];
				if (!child.isDirectory()) {
					continue;
				}
				File[] dateDirs = child.listFiles();

				if (dateDirs != null && dateDirs.length > 0) {
					for (int j = 0; j < dateDirs.length; j++) {
						File dateDirFile = dateDirs[j];
						if (!dateDirFile.isDirectory()) {
							continue;
						}
						String keyString = dateDirFile.getName().replace("-", "");
						String valueString = dateDirFile.getPath();
						tradeDaysList.add(keyString);
						sdatePathMap.put(keyString, valueString);
					}

				}
			}
		}

	}

	public static String getYesDataFileDir(String date) {
		int index = tradeDaysList.indexOf(date);
		if (index == 0) {
			return null;
		}
		String yeString = sdatePathMap.get(tradeDaysList.get(index - 1));
		return yeString;
	}
}
