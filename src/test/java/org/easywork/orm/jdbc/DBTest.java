package org.easywork.orm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
	public static Connection getCon() throws SQLException{
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
//		String url = "jdbc:oracle:thin:@10.71.0.129:1521:orcl";
//		String user = "dsbike";
//		String password = "dsbike";
		
		String url = "jdbc:mysql://10.71.0.129:8000/xwxappbike";
		String user = "omnipay";
		String password = "lantoon";
		
		return DriverManager.getConnection(url, user, password);	
	}
}
