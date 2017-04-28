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

public class Login extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public Login() {
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
		
		// 得到请求参数
		String openid = request.getParameter("openid");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String gender = request.getParameter("gender");
		String language = request.getParameter("language");
		String nickname = request.getParameter("nickname");
		String province = request.getParameter("province");
		
		String resData = null;
		String defaultCarNumber = null;
		
		// 客户端调用该方法，会返回一个值，根据这个返回值的不同，客户端采取相应的动作
		// 100:必须绑定车牌号
		// 200:有车牌号，但是没在停车场（没有未支付的停车信息）
		// 300:有车牌号，并且在停车场（有位支付的停车信息），但是还没有首次支付
		// 400:有车牌号，在停车场，并且已经首次支付
		// 查询数据库，看是否存在该openid的用户。若不存在，则注册。
		// 若存在该用户，则查询该用户的默认车牌，若存在，获得默认车牌。
		String sql = "select * from userinfo where openid = ?";
		String[] parameters = {openid};
		ResultSet rs1 = SqlHelper.executeQuery(sql, parameters);
		try {
			if(!rs1.next()){// 用户不存在，注册。此时返回必须绑定车牌号的信息。
				sql = "insert into userinfo values(?,?,?,?,?,?,?,null,null,null,null,null)";
				parameters=new String[]{openid,city,country,gender,language,nickname,province};
				SqlHelper.executeUpdate(sql, parameters);
				resData="100";
			}else{// 用户存在，查询是否存在默认车牌号
				sql = "select carnumber1 from userinfo where openid=?";
				parameters = new String[]{openid};
				ResultSet rs2 = SqlHelper.executeQuery(sql, parameters);
				if(rs2.next()){// 获得默认车牌号
					defaultCarNumber=rs2.getString(1);
					sql="select * from parkinfo where carnumber=? and iscompleted=0";
					parameters=new String[]{defaultCarNumber};
					ResultSet rs3 = SqlHelper.executeQuery(sql, parameters);
					if(rs3.next()){// 在停车场，有未完成的停车信息
						//查询是否已经首次支付
						sql = "select firstpay from parkinfo where carnumber=(select carnumber1 from userinfo where openid = ?)";
						parameters = new String[]{openid};
						ResultSet rs4 = SqlHelper.executeQuery(sql, parameters);
						if(rs4.next()){// 查询获取firstpay的值
							String firstpay = rs4.getString(1);
							if(firstpay==null){ // 未支付
								resData="300";
							}else{ // 已支付
								resData="400";
							}
						}
						SqlHelper.close(rs4, SqlHelper.getPs(), SqlHelper.getCt());
					}else{ // 无停车信息，返回200
						resData="200";
					}
					SqlHelper.close(rs3, SqlHelper.getPs(), SqlHelper.getCt());
				}else{// 用户存在，但是没有车牌号
					resData="100";
				}
				SqlHelper.close(rs2, SqlHelper.getPs(), SqlHelper.getCt());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SqlHelper.close(rs1, SqlHelper.getPs(), SqlHelper.getCt());
		
		
		PrintWriter out = response.getWriter();
		out.println(resData);
		System.out.println("resData from Login "+resData);
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
