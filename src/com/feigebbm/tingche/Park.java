package com.feigebbm.tingche;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feigebbm.utils.SqlHelper;

public class Park extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

		System.out.println("Park is called");
		
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String openid = request.getParameter("openid");
		String carNumber = null;
		//System.out.println("I am servlet Park. I get openid from payment. the openid is:"+openid);
		
		String resData = "";
		// 根据openid查询停车时长now()-parkin，停车场编号parkingnumber
		String sql = "SELECT timestampdiff(HOUR,parkin,now()) parkh,timestampdiff(MINUTE,parkin,now()) parkm,parkingnumber FROM parkinfo WHERE carnumber = (select carnumber1 from userinfo where openid = ?)";
		String[] parameters = { openid };
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
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getCt());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println(parkTime);
		if (parkTime == null) {
			resData = "{\"isParking\":false}";
			//System.out.println("parkTime from park "+parkTime);
		} else {
			// 根据parkingnumber查询停车场信息parkingname
			sql = "select parkingname from parkinginfo where parkingnumber=?";
			parameters = new String[] { parkingnumber };
			ResultSet rss = SqlHelper.executeQuery(sql, parameters);
			String parkLocation = null;
			try {
				while (rss.next()) {
					parkLocation = rss.getString("parkingname");
				}
				SqlHelper.close(rss, SqlHelper.getPs(), SqlHelper.getCt());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 根据parkingnumber以及parktime查询并计算应缴纳费用
			String fee = null;
			// TODO 计算应交费用
			fee = "10.3";
			// 关闭数据库资源
			
			
			// 根据openid查询carNumber1
			sql = "select carnumber1 from userinfo where openid = ?";
			parameters = new String[]{openid};
			ResultSet rrr = SqlHelper.executeQuery(sql, parameters);
			
			try {
				while (rrr.next()) {
					carNumber=rrr.getString(1);
				}
				SqlHelper.close(rrr, SqlHelper.getPs(), SqlHelper.getCt());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SqlHelper.close(rrr, SqlHelper.getPs(), SqlHelper.getCt());
			resData = "{\"carNumber\":\""+carNumber+"\",\"parkTime\":\"" + parkTime + "\",\"parkLocation\":\"" + parkLocation
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
