package queryinterface;

import java.util.ArrayList;

import com.storage.trade.DayTradeResponse;

public interface QueryInterface {
	/**
	 * ��ѯ������Ʊ��������
	 * @param code ��Ʊ����
	 * @param date ����
	 * @return �ļ�·��
	 */
	DayTradeResponse querySingle(String code,String date);
  
  
  /**
	 * ��ѯ������Ʊ�ڼ�����
	 * @param code ��Ʊ����
	 * @param date ����
	 * @return �ļ�·��
	 */
ArrayList<String> querySingle(String code,String fromDate,String endDate);

}
