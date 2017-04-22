package com.feigebbm.tingche;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.inject.ResolutionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feigebbm.utils.SqlHelper;

public class Park extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Park() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String carNumber = request.getParameter("carNumber");

		String resData = "";
		// 根据carNumber查询停车时长now()-parkin，停车场编号parkingnumber
		String sql = "SELECT timestampdiff(HOUR,parkin,now()) parkh,timestampdiff(MINUTE,parkin,now()) parkm,parkingnumber FROM parkinfo WHERE carnumber = ?";
		String[] parameters = { carNumber };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);

		String parkTime = null;
		String parkh = null;
		String parkm = null;
		String parkingnumber = null;
		try {
			while (rs.next()) {
				parkh = rs.getString("parkh");
				parkm = rs.getString("parkm");
				parkm = Integer.parseInt(parkm) % 60 + "";
				parkTime = parkh + "小时" + parkm + "分钟";
				parkingnumber = rs.getString("parkingnumber");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(parkTime);
		if (parkTime == null) {
			resData = "{\"isParking\":false}";
			System.out.println(parkTime);
		} else {
			// 根据parkingnumber查询停车场信息parkingname
			sql = "select parkingname from parkinginfo where parkingnumber=?";
			parameters = new String[] { parkingnumber };
			rs = SqlHelper.executeQuery(sql, parameters);
			String parkLocation = null;
			try {
				while (rs.next()) {
					parkLocation = rs.getString("parkingname");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 根据parkingnumber以及parktime查询并计算应缴纳费用
			String fee = null;
			// TODO 计算应交费用
			fee = "10.3";
			// 关闭数据库资源
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getCt());
			resData = "{\"carNumber\":\"苏A·W526Z\",\"parkTime\":\"" + parkTime + "\",\"parkLocation\":\"" + parkLocation
					+ "\",\"fee\":" + fee + "}";
		}

		PrintWriter out = response.getWriter();
		out.println(resData);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
