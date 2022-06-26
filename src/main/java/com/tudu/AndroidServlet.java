package com.tudu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.storage.ImpQueryData;
import com.storage.TextUtil;
import com.storage.trade.DayTradeResponse;

/**
 * http://127.0.0.1:8080/StockAssitantServer/query_single?code=300649&from_date=20211201&end_date=20211201
 * 
 * @author Administrator sheng_xiong
 * @date 20220503
 * @description
 */

public class AndroidServlet extends HttpServlet {

	public AndroidServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fromDate = request.getParameter("from_date");
		String endDate = request.getParameter("end_date");
		String code = request.getParameter("code");

		if (TextUtil.isNullOrEmpty(endDate)) {
			ImpQueryData impQueryData = new ImpQueryData();
			DayTradeResponse tradeResponse = impQueryData.querySingle(code, fromDate);
			response.getOutputStream().write(tradeResponse.generateResponseJsonString().getBytes("UTF-8"));
		} else {
			response.getOutputStream().write("Login success".getBytes("UTF-8"));
		}

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);

	}
}
