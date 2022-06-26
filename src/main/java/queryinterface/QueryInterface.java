package queryinterface;

import java.util.ArrayList;

import com.storage.trade.DayTradeResponse;

public interface QueryInterface {
	/**
	 * 查询单个股票单天数据
	 * @param code 股票代码
	 * @param date 日期
	 * @return 文件路径
	 */
	DayTradeResponse querySingle(String code,String date);
  
  
  /**
	 * 查询单个股票期间数据
	 * @param code 股票代码
	 * @param date 日期
	 * @return 文件路径
	 */
ArrayList<String> querySingle(String code,String fromDate,String endDate);

}
