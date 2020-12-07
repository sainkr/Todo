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
	
	
}