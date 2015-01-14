package org.easywork.orm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rongding.framework.orm.jdbc.DBParams;
import rongding.framework.orm.jdbc.DBUtil;


/***
 * 注意从数据库取日期,比如：数据库字段中有stamp=2010-02-08 09:31:53
 * rs.getTime("stamp")只能得到09:31:53，rs.getDate("stamp")只能得到2010-02-08
 * rs.getTimestamp("stamp")，得到2010-02-08 09:31:53.0,
 * 只能通过函数rs.getTimestamp("stamp").toString().substring(0, rs.getTimestamp("stamp").toString().length()-2)
 * 得到2010-02-08 09:31:53
 * @author Administrator
 *
 */
public class DBParamTest {
	public static void main(String[] args) {	
		try {
			System.out.println(DBTest.getCon());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
//		StringBuilder sql=new StringBuilder();
//		 sql.append( "select * from t_corp t where t.corpid=? and t.corpname=? ")
//		 .append("and t.stamp between to_date(?,'yyyy-mm-dd HH24:mi:ss') and to_date(?,'yyyy-mm-dd HH24:mi:ss')");
//		
//		Connection con = null;
//		try {
//			con = DBTest.getCon();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		//为sql语句设置参数
//		DBParams params = new DBParams();
//		params.addParam(1314);
//		params.addParam("惠山分局钱桥派出所");
//		params.addParam("2010-01-01 00:00:00");
//		params.addParam("2010-04-03 03:00:00");
//
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		try {
//			pst = con.prepareStatement(sql.toString());
//			params.prepareStatement(pst);
//			rs = pst.executeQuery();
//			if(rs.next()){
//				System.out.println("remark:" + rs.getString("corpname"));
//				System.out.println("stamp:"+rs.getTimestamp("stamp").toString().substring(0, rs.getTimestamp("stamp").toString().length()-2));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			DBUtil.closeRs(rs);
//			DBUtil.closePst(pst);
//			DBUtil.closeCon(con);
//		}
	}
}
