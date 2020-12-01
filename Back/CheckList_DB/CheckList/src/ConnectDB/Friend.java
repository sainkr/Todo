package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Friend {
	
	private static Friend instance = new Friend();

	public static Friend getInstance() {
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
	
	
	public String addRequest(String request_id, String target_id) {
		
		  if(getProfile(target_id).equals("loginSuccess")) {
			  try {
					conn = cDB.getConn();
					
					sql = "insert into request_friend (request_id, target_id) values (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, request_id);
					pstmt.setString(2, target_id);
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					// 친구 추가
					sql = "select * from profile where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, request_id);
					rs = pstmt.executeQuery();	//	db에 쿼리문 입력
					if(rs.next()) {
						String friend_id = rs.getString("friend_id");
						if(friend_id == null) {
							friend_id = target_id;
						}
						else {
							friend_id += " "+ target_id;
						}
						
						sql = "update profile set friend_id = ? where id = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, friend_id);
						pstmt.setString(2, request_id);
						pstmt.executeUpdate();	//	db에 쿼리문 입력
						
					}
					
					// 친구 추가
					sql = "select * from profile where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, target_id);
					rs = pstmt.executeQuery();	//	db에 쿼리문 입력
					if(rs.next()) {
						String friend_id = rs.getString("friend_id");
						if(friend_id == null) {
							friend_id = request_id;
						}
						else {
							friend_id += " "+ request_id;
						}
						
						sql = "update profile set friend_id = ? where id = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, friend_id);
						pstmt.setString(2, target_id);
						pstmt.executeUpdate();	//	db에 쿼리문 입력
						
					}
					
					
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
	
	public String getRequest(String request_id) {
		System.out.println("내 아이디"+ request_id);
		
	  try {
			conn = cDB.getConn();
			
			sql = "select * from request_friend where target_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, request_id);
			rs = pstmt.executeQuery();			
			
			JSONArray jary = new JSONArray();
			while(rs.next()) {
				sql = "select * from profile where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, rs.getString("request_id"));
				rs_sub = pstmt.executeQuery();	
			
				while(rs_sub.next()) {
					JSONObject jobj = new JSONObject();
					jobj.put("id",rs.getString("request_id"));
					jobj.put("name",rs_sub.getString("name"));
					jary.add(jobj);
				}
				
				sql = "delete from request_friend where num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, rs.getString("num"));
				pstmt.executeUpdate();
			}
			
			if(jary.size() == 0)
				returns = "requstNoting";
			else
				returns = jary.toJSONString();
			
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