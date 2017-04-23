package com.feigebbm.utils;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class SqlHelper {
	// 定义需要的变量
	private static Connection ct = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;

	public static Connection getCt() {
		return ct;
	}

	public static PreparedStatement getPs() {
		return ps;
	}

	public static ResultSet getRs() {
		return rs;
	}

	// 连接数据库的参数
	private static String url = "";
	private static String username = "";
	private static String driver = "";
	private static String password = "";

	private static Properties pp = null;
	private static FileInputStream fis = null;

	/*
	// 加载驱动，只需要一次
	static {
		try {
			// 从dbinfo.properties文件中读取配置信息
			pp = new Properties();
			fis = new FileInputStream("dbinfo.properties");
			pp.load(fis);
			url = pp.getProperty("url");
			username = pp.getProperty("username");
			driver = pp.getProperty("driver");
			password = pp.getProperty("password");

			Class.forName(driver);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			fis = null;
		}
	}
*/
	// 得到连接
	public static Connection getConnection() {
		try {
			url = "jdbc:mysql://localhost:3306/park?useUnicode=true&characterEncoding=UTF-8";
			username = "park";
			driver = "com.mysql.jdbc.Driver";
			password = "wangfei1013";
			Class.forName(driver);
			ct = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}

	// 统一的select
	public static ResultSet executeQuery(String sql, String[] parameters) {

		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {

		}

		return rs;

	}

	// 有多个sql语句，需要考虑事务
	public static void executeUpdate(String sql[], String[][] parameters) {
		try {
			// 1.得到连接
			ct = getConnection();

			ct.setAutoCommit(false);

			for (int i = 0; i < sql.length; i++) {
				if (parameters[i] != null) {
					ps = ct.prepareStatement(sql[i]);
					for (int j = 0; j < parameters[i].length; j++) {
						ps.setString(j + 1, parameters[i][j]);
					}
					ps.executeUpdate();
				}
			}

			ct.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			close(rs, ps, ct);
		}
	}

	// 先写一个update/delete/insert
	public static void executeUpdate(String sql, String[] parameters) {
		// 1. 创建一个ps
		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			// 给?赋值
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			// 执行
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			// 抛出异常
			throw new RuntimeException(e.getMessage());
		} finally {
			// 关闭资源
			close(rs, ps, ct);
		}
	}

	// 关闭资源的函数
	public static void close(ResultSet rs, Statement ps, Connection ct) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			ps = null;
		}
		if (ct != null) {
			try {
				ct.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			ct = null;
		}
	}
}
