package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Request {
	
	private static Request instance = new Request();

	public static Request getInstance() {
		return instance;
	}
	
	//	DB접근
	private ConnectDB cDB = new ConnectDB();	//	DBConnector 객체생성
	private Connection conn = null;    //  connecttion:db에 접근하게 해주는 객체
	private String sql = "";
	private PreparedStatement pstmt;
	private ResultSet rs;	
	private String returns;
	
	
	public String addRequest(String request_id, String target_id) {
		
		  if(getProfile(target_id).equals("loginSuccess")) {
			  try {
					conn = cDB.getConn();
					
					sql = "insert into request_friend (request_id, target_id) values (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, request_id);
					pstmt.setString(2, target_id);
					
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					returns = "requestSuccess";
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println("Login SQLException error");
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
				}
				
				System.out.println(returns);
		  }
		  else
			  returns = "requestFail";
			
		return returns;
	}
	
	public String getProfile(String id) {	
		try {

			conn = cDB.getConn();
			sql = "select * from profile where id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();		

			if(rs.next()) 
				returns = "loginSuccess";
			else
				returns = "loginFail";
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Login SQLException error");
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
		}
		
		System.out.println(returns);
		
	return returns;
	}
}