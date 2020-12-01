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
	private ResultSet rs_sub;	
	private String returns;
	
	
	public String addProfile(String id, String password, String name) {
		
		  if(getProfile(id,password).equals("loginFail")) {
			  try {
					conn = cDB.getConn();
					
					sql = "insert into profile (id, password, name) values (?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					pstmt.setString(2, password);
					pstmt.setString(3, name);
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					returns = "joinSuccess";
				
					
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
			System.out.println("id : " + id + " password : " + password);
			conn = cDB.getConn();
			sql = "select * from profile where id =? and password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();		

			if(rs.next()) {
				JSONArray jary = new JSONArray();
				String friend_id = rs.getString("friend_id");
				String[] arr = friend_id.split(" ");
				
				for(int i =0; i<arr.length; i++) {
					
					sql = "select * from profile where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, arr[i]);
					rs_sub = pstmt.executeQuery();	
					
					JSONObject jobj = new JSONObject();
					jobj.put("name",rs.getString("name"));
					jobj.put("id",arr[i]);
					jobj.put("name",rs_sub.getString("name"));
					jary.add(jobj);
				}
				
				if(jary.size() == 0)
					returns = rs.getString("name");
				else
					returns = jary.toJSONString();
	
			}
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