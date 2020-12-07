package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChallengeAdd {
	
	private static ChallengeAdd instance = new ChallengeAdd();

	public static ChallengeAdd getInstance() {
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
	
	
	public String addChallenge(String name, String host_id, String member) {
		
	  try {
		  System.out.println(name +" "+ host_id+ " "+ member);
		 
			conn = cDB.getConn();
			sql = "insert into challenge(host_id, challenge_name) values (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, host_id);
			pstmt.setString(2, name);
			pstmt.executeUpdate();	//	db에 쿼리문 입력
			
			// 고유 번호 불러오기
			sql = "select * from challenge where host_id = ? and challenge_name = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, host_id);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {
				
				int code = rs.getInt("code");
				
				// challenge+code 테이블 생성
				sql = "create table challenge"+code+"("
						+ "member_id varchar(30) not null, success int not null)";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				
				// id+challengetodo 테이블 생성
				sql = "create table challenge"+code+"todo("
						+ "code int auto_increment primary key, content varchar(1000) not null)";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				
				// 멤버 추가
				String[] arr = member.split(" ");
				for(int i =0; i<arr.length; i++) {
					sql = "insert into challenge"+code+" values(?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, arr[i]);
					pstmt.setInt(2, 0);
					pstmt.executeUpdate();
				}
				
				// 각 member+challenge 테이블에 모임코드 추가
				for(int i =0; i<arr.length; i++) {
					sql = "insert into "+arr[i]+"challenge values(?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, code);
					pstmt.executeUpdate();
				}	
				
				 returns = String.valueOf(code);
			}

						
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
	
	
	public String setChallenge(String name, String host_id, String code, String add_member, String delete_member) {
		
		  try {
			  System.out.println(name +" "+ host_id+ " " + code +" 추가 "+ add_member+" 삭제 "+delete_member);
			 
				conn = cDB.getConn();
				
				// 이름 변경
				sql = "update challenge set challenge_name = ? where code = ? and host_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setInt(2,  Integer.parseInt(code));
				pstmt.setString(3, host_id);
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				// 삭제 멤버
				if(!delete_member.equals("")) {
					String[] deleteArr = delete_member.split(" ");
					
					for(int i =0; i < deleteArr.length; i++) {
						// challenge+code 테이블에서 지움
						sql = "delete from challenge"+code+" where member_id = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, deleteArr[i]);
						pstmt.executeUpdate();	//	db에 쿼리문 입력
						
						// id+challenge 테이블에서 지움
						sql = "delete from "+deleteArr[i]+"challenge where challenge_code = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt(code) );
						pstmt.executeUpdate();	//	db에 쿼리문 입력
						
						// id+challengetodo 테이블에서 지움
						sql = "delete from "+deleteArr[i]+"challengetodo where code = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt(code) );
						pstmt.executeUpdate();	//	db에 쿼리문 입력
					}
				}
				
				
				// 추가 멤버
				if(!add_member.equals("")) {
					String[] addArr = add_member.split(" ");
					
					for(int i =0; i<addArr.length; i++) {
						sql = "insert into challenge"+code+" values(?,?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, addArr[i]);
						pstmt.setInt(2, 0);
						pstmt.executeUpdate();
					
						sql = "insert into "+addArr[i]+"challenge values(?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt(code));
						pstmt.executeUpdate();
						
						// 고유 번호 불러오기
						sql = "select * from challenge"+code+"todo";
						pstmt = conn.prepareStatement(sql);
						rs = pstmt.executeQuery();		
						
						while(rs.next()) {
							sql = "insert into "+addArr[i]+"challengetodo values(?,?,?,?)";
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, Integer.parseInt(code));
							pstmt.setInt(2, rs.getInt("code"));
							pstmt.setString(3, rs.getString("content"));
							pstmt.setInt(4, 0);
							pstmt.executeUpdate();
						}
					}
					
				}
				returns = "updateChallengeSuccess";
							
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
	
	
	public String deleteChallenge(String host_id, String code) {
		
		  try {
			  System.out.println(host_id+ " " + code);
			 
				conn = cDB.getConn();
				
				// 고유 번호 불러오기
				sql = "select * from challenge"+code;
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();		
				
				while(rs.next()) {
					// challenge+code 테이블에서 지움
					sql = "delete from challenge"+code+" where member_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, rs.getString("member_id"));
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					// id+challenge 테이블에서 지움
					sql = "delete from "+rs.getString("member_id")+"challenge where challenge_code = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(code) );
					pstmt.executeUpdate();	//	db에 쿼리문 입력
					
					// id+challengetodo 테이블에서 지움
					sql = "delete from "+rs.getString("member_id")+"challengetodo where code = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(code) );
					pstmt.executeUpdate();	//	db에 쿼리문 입력
				}

				sql = "drop table challenge"+code;
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();	//	db에 쿼리문 입력

				sql = "drop table challenge"+code+"todo";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				sql = "delete from challenge where code = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(code));
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				returns = "deleteChallengeSuccess";
							
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