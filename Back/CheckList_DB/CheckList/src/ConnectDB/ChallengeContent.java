package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChallengeContent {
	
	private static ChallengeContent instance = new ChallengeContent();

	public static ChallengeContent getInstance() {
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

	
	
	public String addContent(String code, String num, String content) {
		
		  try {
			   System.out.println(code+" "+num +" " + content);
			   
				conn = cDB.getConn();
				
				sql = "insert into challenge"+ code +" values (?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, num);
				pstmt.setString(2, content);
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				returns = "addChallengeTodoSuccess";
				
				
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
	
	public String updateContent(String code, String num, String content) {
		
		  try {
			   System.out.println(code+" "+num +" " + content);
			   
				sql = "update challenge"+ code +" set content = ? where num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, content);
				pstmt.setInt(2, Integer.parseInt(num));
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				returns = "updateChallengeTodoSuccess";
				
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
	
	
	public String deleteContent(String code, String num) {
		
		  try {
			   System.out.println(code+" "+num);
			   
				sql = "delete from challenge"+ code +"where num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(num));
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				returns = "deleteChallengeTodoSuccess";
				
				
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