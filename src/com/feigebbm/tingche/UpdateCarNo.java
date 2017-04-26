package com.feigebbm.tingche;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.feigebbm.utils.SqlHelper;

public class UpdateCarNo extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateCarNo() {
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

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String openid = request.getParameter("openid");
		String addressList = request.getParameter("addressList");
		System.out.println(openid);
		System.out.println(addressList);
		JSONArray array = null;
		String carno[]=new String[3];
		try {
			array = new JSONArray(addressList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for(int i=0;i<array.length();i++){
			try {
				JSONObject jo = array.getJSONObject(i);
				carno[i]=jo.getString("Address");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<carno.length;i++){
			System.out.println(carno[i]);
		}
		// 以这两个参数，更新数据库
		String sql = "update userinfo set carnumber1=?,carnumber2=?,carnumber3=? where openid=?";
		String[] parameters = {carno[0],carno[1],carno[2],openid};
		SqlHelper.executeUpdate(sql, parameters);
		

		PrintWriter out = response.getWriter();
		out.println("200");
		out.flush();
		out.close();
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
