package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Login {
	
	private static Login instance = new Login();

	public static Login getInstance() {
		return instance;
	}
	
	//	DB접근
	private ConnectDB cDB = new ConnectDB();	//	DBConnector 객체생성
	private Connection conn = null;    //  connecttion:db에 접근하게 해주는 객체
	private String sql = "";
	private PreparedStatement pstmt;
	private ResultSet rs;	
	private String returns;
	
	
	public String addProfile(String id, String password, String name) {
		
		  if(getProfile(id,password).equals("loginFail")) {
			  try {
					conn = cDB.getConn();
					
					sql = "insert into login (id, password, name) values (?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					pstmt.setString(2, password);
					pstmt.setString(3, name);
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					returns = "todoAddSuccess";
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
			  returns = "joinFail";
			
		return returns;
	}	
	
	
	public String getProfile(String id, String password) {	
		try {
			
			conn = cDB.getConn();
			sql = "select * from login where id =? and password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.executeUpdate();	//	db에 쿼리문 입력
			
			if(pstmt == null)
				returns = "loginFail";
			else {
				returns = "loginSuccess";
			}
			
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