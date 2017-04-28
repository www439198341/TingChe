package com.wangfei.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.feigebbm.utils.SqlHelper;

public class tt {

	public static void main(String[] args) {
		tt tt = new tt();
		tt.testUpdate();
	}
	
	public void testUpdate() {
		// String sql = "update userinfo set nickname = ? where account = ?";
		// String[] parameters = { "xiaoge", "kuaidi" };
		// SqlHelper.executeUpdate(sql, parameters);
		String sql = "insert into parkinfo values(?, now() ,?,null,null,null)";
		System.out.println(sql);
		String[] parameters = { "abc", "123456" };
		SqlHelper.executeUpdate(sql, parameters);

	}

	public void testquery() throws SQLException {

//		String sql = "select * from userinfo";
//		ResultSet rs = SqlHelper.executeQuery(sql, null);
//		while (rs.next()) {
//			System.out.println(rs.getString("nickname"));
//		}
//		SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getCt());
	}

}
