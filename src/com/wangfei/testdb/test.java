package com.wangfei.testdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class test {
	public static void main(String[] args) {
		Connection ct = null;
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//1.加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.得到连接
			ct=DriverManager.getConnection("jdbc:mysql://192.168.1.64:3306/park","park","wangfei1013");
			ps=ct.prepareStatement("select * from userinfo");
			rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("nickname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				rs=null;
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				ps=null;
			}
			if(ct!=null){
				try {
					ct.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				ct=null;
			}
		}
	}
}
