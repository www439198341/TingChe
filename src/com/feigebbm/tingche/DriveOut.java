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

public class DriveOut extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public DriveOut() {
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

		doPost(request, response);

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

		System.out.println("DriveOut is called");

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String carNumber = request.getParameter("carNumber");
		String parkingInfo = request.getParameter("parkingInfo");
		String isopengate = request.getParameter("isopengate").toLowerCase();

		System.out.println(isopengate);
		if (isopengate.equals("false")) {// 表示服务器接收到出场拍照系统发来的消息
			// 写入出场时间，并计算出场时间与入场时间差
			String sql = "update parkinfo set parkout=now() where carnumber = ? and parkingnumber = ? and iscompleted=0";
			String[] parameters = { carNumber, parkingInfo };
			SqlHelper.executeUpdate(sql, parameters);
			SqlHelper.close(SqlHelper.getPs(), SqlHelper.getCt());

			String diffTime = null;
			sql = "select timestampdiff(MINUTE,firstpay,parkout) difftime from parkinfo where carnumber = ? and parkingnumber = ? and iscompleted=0";
			ResultSet rs = SqlHelper.executeQuery(sql, parameters);
			try {
				while (rs.next()) {
					diffTime = rs.getString("difftime");
					System.out.println("diffTime------->" + diffTime);
				}
				SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getCt());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getCt());

			String resData = "";
			if (diffTime == null) {
				// 用户在出场拍照时，没有线上付款
				resData = "400";
				System.out.println("用户在出场拍照时，没有线上付款");
			} else {
				// 用户已经在线付款，计算是否超出免费时限
				if (Integer.parseInt(diffTime) > 15) {
					// 超时
					resData = "500";
					System.out.println("超时");
				} else {
					// 不超时
					resData = "200";
					System.out.println("正常退出");
				}
			}
			PrintWriter out = response.getWriter();
			out.print(resData);
			out.flush();
			out.close();
		} else { // 表示系统接收到抬杆离场发来的消息
			String sql = "update parkinfo set iscompleted=1 where carnumber = ? and parkingnumber = ? and iscompleted=0";
			String[] parameters = { carNumber, parkingInfo };
			SqlHelper.executeUpdate(sql, parameters);
			SqlHelper.close(SqlHelper.getPs(), SqlHelper.getCt());
			String resData = "200";
			PrintWriter out = response.getWriter();
			out.print(resData);
			out.flush();
			out.close();
		}

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
