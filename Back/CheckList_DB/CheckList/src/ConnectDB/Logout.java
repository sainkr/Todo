package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Logout {
	
	private static Logout instance = new Logout();

	public static Logout getInstance() {
		return instance;
	}
	
	//	DB접근
	private ConnectDB cDB = new ConnectDB();	//	DBConnector 객체생성
	private Connection conn = null;    //  connecttion:db에 접근하게 해주는 객체
	private String sql = "";
	private PreparedStatement pstmt;
	private ResultSet rs;	
	private ResultSet rs_sub;	
	private String returns;
	
	public String deleteTodo(String id) {	
		try {
			System.out.println("id : " + id);
			conn = cDB.getConn();
			
			// 일단 다 삭제하고
			sql = "delete from "+id+"todo";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();	
			
			sql = "delete from "+id+"weather";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();	

			returns = "deleteSuccess";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
			returns = "error";
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs_sub != null)
				try {
					rs_sub.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
		}
		
		System.out.println(returns);
		
	return returns;
	}
	
	
	public String setTodo(String id, String date, String content, String check) {	
		try {
			System.out.println("id : " + id+ " date : "+date+" content : "+content+" check : "+ check);
			conn = cDB.getConn();
			
			sql = "insert into "+id+"todo values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, content);
			pstmt.setInt(3, Integer.parseInt(check));
			pstmt.executeUpdate();	
			
			returns = "saveSuccess";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
			returns = "error";
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs_sub != null)
				try {
					rs_sub.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
		}
		
		System.out.println(returns);
		
	return returns;
	}
	
	public String setWeather(String id, String date, String weather) {	
		try {
			System.out.println("id : " + id+ " date : "+date+" weather : "+weather);
			conn = cDB.getConn();

			sql = "insert into "+id+"weather values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setInt(2, Integer.parseInt(weather));
			pstmt.executeUpdate();	
			
			returns = "saveSuccess";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
			returns = "error";
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
			if (rs_sub != null)
				try {
					rs_sub.close();
				} catch (SQLException ex) {
					System.err.println("Login SQLException error");
					returns = "error";
				}
		}
		
		System.out.println(returns);
		
	return returns;
	}
}