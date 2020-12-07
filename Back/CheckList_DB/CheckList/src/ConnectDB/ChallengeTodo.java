package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChallengeTodo {
	
	private static  ChallengeTodo instance = new  ChallengeTodo();

	public static  ChallengeTodo getInstance() {
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
	
	
	public String getMember(String code) {
		try {
			    System.out.println(code);
				conn = cDB.getConn();
				sql = "select * from challenge"+code;
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();	//	db에 쿼리문 입력
				
				JSONArray jary = new JSONArray();
				
				while(rs.next()) {
					JSONObject jobj = new JSONObject();
					
					sql = "select * from profile where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,rs.getString("member_id"));
					rs_sub = pstmt.executeQuery();	//	db에 쿼리문 입력
					
					if(rs_sub.next()) {
						jobj.put("member_id",rs.getString("member_id"));
						jobj.put("name",rs_sub.getString("name"));
						jobj.put("success",rs.getInt("success"));
						jary.add(jobj);
					}
					
				}
				
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
				
				if(rs_sub != null)
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
	
	
	public String getChallengetodo(String host_id, String code) {
		
	  try {
		   System.out.println(host_id+ " "+ code);
			conn = cDB.getConn();
			sql = "select * from "+host_id+"challengetodo where code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(code));
			rs = pstmt.executeQuery();	//	db에 쿼리문 입력
			
			JSONArray jary = new JSONArray();
			
			while(rs.next()) {
				JSONObject jobj = new JSONObject();
				jobj.put("num",rs.getInt("num"));
				jobj.put("content",rs.getString("todo"));
				jobj.put("check",rs.getString("todo_check"));
				jary.add(jobj);
			}
			
			if(jary.size() == 0)
				returns = "challengeNoting";
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
		}
		
		System.out.println(returns);
	  
	
		return returns;
	}
	
	
	public String setChallengetodo(String host_id, String code, String num) {
		
		  try {
			   System.out.println(host_id+ " "+ code+" "+ num);
				conn = cDB.getConn();
				sql = "select * from "+host_id+"challengetodo where code = ? and num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(code));
				pstmt.setInt(2, Integer.parseInt(num));
				rs = pstmt.executeQuery();	//	db에 쿼리문 입력
				
				while(rs.next()) {
					int c = 0;
					if(rs.getInt("todo_check")== 1) {
						sql = "update "+host_id+"challengetodo set todo_check = ? where code = ? and num = ?";
					}
					else {
						sql = "update "+host_id+"challengetodo set todo_check = ? where code = ? and num = ?";
						c = 1;
					}
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, c);
					pstmt.setInt(2, Integer.parseInt(code));
					pstmt.setInt(3, Integer.parseInt(num));
					pstmt.executeUpdate();
				}
				
				sql = "select * from "+host_id+"challengetodo where code = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(code));
				rs = pstmt.executeQuery();	//	db에 쿼리문 입력
				
				int count = 0;
				int cnum = 0;
				while(rs.next()) {
					count++;
					if(rs.getInt("todo_check")== 1) {
						cnum++;
					}
				}
				
				if(cnum == count) {
					sql = "update challenge"+code+" set success = ? where member_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, 1);
					pstmt.setString(2, host_id);
					pstmt.executeUpdate();
				}
				else {
					sql = "update challenge"+code+" set success = ? where member_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, 0);
					pstmt.setString(2, host_id);
					pstmt.executeUpdate();
				}
				
				returns = "setCheckSuccess";
							
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
			}
			
			System.out.println(returns);
		  
		
			return returns;
		}
	
}