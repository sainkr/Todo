package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChallengeHost {
	
	private static ChallengeHost instance = new ChallengeHost();

	public static ChallengeHost getInstance() {
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
				
				sql = "insert into challenge"+ code +"todo (content) values (?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, content);
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				returns = "addChallengeTodoSuccess";
				
				// 고유 번호 불러오기
				sql = "select * from challenge"+code+"todo where content = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, content);
				rs = pstmt.executeQuery();	
				
				int todo_num = 0;
				
				if(rs.next()) {
					todo_num = rs.getInt("code");
				}
				 
				
				// 각 멤버별 id + challenge table에 (모임코드, todo번호, 내용, 0) 추가
				sql = "select * from challenge"+code;
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();	
				
				while(rs.next()) {
					sql = "insert into "+rs.getString("member_id")+"challengetodo values (?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(code));
					pstmt.setInt(2, todo_num);
					pstmt.setString(3, content);
					pstmt.setInt(4, 0);
					pstmt.executeUpdate();
				}
				
				// challenge+code 테이블에 체크 여부 0으로
				sql = "update challenge"+code+" set success = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, 0);
				pstmt.executeUpdate();
	
				
				
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
			   
			   conn = cDB.getConn();
			   
				sql = "update challenge"+ code +"todo set content = ? where code = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, content);
				pstmt.setInt(2, Integer.parseInt(num));
				pstmt.executeUpdate();	//	db에 쿼리문 입력
					
				// 각 멤버별 id + challenge table에  content 내용 수정
				sql = "select * from challenge"+code;
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();	
				
				while(rs.next()) {
					sql = "update "+rs.getString("member_id")+"challengetodo set todo = ? where code = ? and num = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, content);
					pstmt.setInt(2, Integer.parseInt(code));
					pstmt.setInt(3, Integer.parseInt(num));
					pstmt.executeUpdate();
				}
				
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
			   
			   conn = cDB.getConn();
			   
				sql = "delete from challenge"+ code +"todo where code = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(num));
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				// 각 멤버별 id + challenge table에  삭제
				sql = "select * from challenge"+code;
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();	
				
				while(rs.next()) {
					System.out.println("삭제 ");
					sql = "delete from "+rs.getString("member_id")+"challengetodo where code = ? and num = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(code));
					pstmt.setInt(2, Integer.parseInt(num));
					pstmt.executeUpdate();
				}
				
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