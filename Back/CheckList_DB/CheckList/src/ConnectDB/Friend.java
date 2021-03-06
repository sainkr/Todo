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
	
	
	public String addFriend(String request_id, String target_id) {
		
		  if(getProfile(target_id).equals("loginSuccess")) {
			  try {
					conn = cDB.getConn();
					
					sql = "insert into "+request_id+"friend values (?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, target_id);
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					sql = "insert into "+target_id+"friend values (?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, request_id);
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					sql = "select * from profile where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, rs.getString("request_id"));
					rs = pstmt.executeQuery();	
				
					if (rs.next()) 
						returns = rs.getString("name");
				
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
	
	public String getFriend(String request_id) {
		System.out.println("내 아이디"+ request_id);
		
	  try {
			conn = cDB.getConn();
			
			sql = "select * from "+request_id+"friend";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();			
			
			JSONArray jary = new JSONArray();
			while(rs.next()) {
				sql = "select * from profile where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, rs.getString("friend_id"));
				rs_sub = pstmt.executeQuery();	
			
				while(rs_sub.next()) {
					JSONObject jobj = new JSONObject();
					jobj.put("id",rs.getString("friend_id"));
					jobj.put("name",rs_sub.getString("name"));
					jary.add(jobj);
				}
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
	
	public String deleteFriend(String request_id, String target_id) {
		System.out.println("내 아이디"+ request_id);
		
	  try {
			conn = cDB.getConn();
			
			sql = "delete * from "+request_id+"friend where friend_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, target_id);
			rs = pstmt.executeQuery();			
			
			sql = "delete * from "+target_id+"friend where friend_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, request_id);
			rs = pstmt.executeQuery();		
			
			returns = "deleteFriendSuccess";
			
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