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

public class GetCarNo extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GetCarNo() {
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
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
		
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String openid = request.getParameter("openid");
		System.out.println(openid);
		// 以openid为依据，查询该用户绑定的车牌号信息
		String reString="";
		String sql = "select * from userinfo where openid=?";
		String[] parameters = {openid};
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		String carno1 = null,carno2 = null,carno3 = null;
		try {
			if(rs.next()){
				carno1=rs.getString(8);
				carno2 = rs.getString(9);
				carno3 = rs.getString(10);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(carno1);
		System.out.println(carno2);
		System.out.println(carno3);
		
		//[{"Address":"苏A·W526Z"},{"Address":"京A·12345"}]
		// 对查询结果进行判断
		if(carno1==null || carno1==""){
			// 没有任何车辆信息，返回""
			reString="[]";
		}else if(carno2==null || carno2==""){
			// 只有一个车牌号，返回[{"Address":"苏A·W526Z"}]
			reString = "[{\"Address\":\""+carno1+"\"}]";
		}else if(carno3==null || carno3==""){
			// 有两个车牌，返回[{"Address":"苏A·W526Z"},{"Address":"京A·12345"}]
			reString="[{\"Address\":\""+carno1+"\"},{\"Address\":\""+carno2+"\"}]";
		}else{
			// 有三个车牌，返回全部格式
			reString="[{\"Address\":\""+carno1+"\"},{\"Address\":\""+carno2+"\"},{\"Address\":\""+carno3+"\"}]";
		}
		
		
		PrintWriter out = response.getWriter();
		out.println(reString);
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
