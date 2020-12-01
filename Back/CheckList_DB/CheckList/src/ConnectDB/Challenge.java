package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Challenge {
	
	private static Challenge instance = new Challenge();

	public static Challenge getInstance() {
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
	
	
	public String addChallenge(String name, String member) {
		
	  try {
		  System.out.println(name +" "+ member);
			conn = cDB.getConn();
			sql = "insert into challenge(name,member,fin_member, nfin_member) values (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, member);
			pstmt.setString(3, "");
			pstmt.setString(4, "");
			pstmt.executeUpdate();	//	db에 쿼리문 입력
			
			
			// 고유 번호 불러오기
			sql = "select * from challenge where name = ? and member = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, member);
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {
				String[] arr = member.split(" ");
				for(int i =0; i<arr.length; i++) {
					sql = "insert into challenge_request values(?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, arr[i]);
					pstmt.setString(2, rs.getString("num"));
					pstmt.executeUpdate();
				}
				
				returns = rs.getString("num");
			}
			
			// 테이블 생성
			sql = "create table challenge"+returns+"("
					+ "num int primary key, content varchar(100) not null)";
			pstmt = conn.prepareStatement(sql);
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
	
	
	public String addContent(String name, String member) {
		
		  try {
			  System.out.println(name +" "+ member);
				conn = cDB.getConn();
				sql = "insert into challenge(name,member,fin_member, nfin_member) values (?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, member);
				pstmt.setString(3, "");
				pstmt.setString(4, "");
				pstmt.executeUpdate();	//	db에 쿼리문 입력
				
				// 고유 번호 불러오기
				sql = "select * from challenge where name = ? and member = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, member);
				rs = pstmt.executeQuery();			
				
				while(rs.next()) {
					String[] arr = member.split(" ");
					for(int i =0; i<arr.length; i++) {
						sql = "insert into challenge_request values(?,?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, arr[i]);
						pstmt.setString(2, rs.getString("num"));
						pstmt.executeUpdate();
					}
					
					returns = rs.getString("num");
				}
				
				// 테이블 생성
				sql = "create table "+returns+"challenge("
						+ "num int primary key, content varchar(100) not null)";
				pstmt = conn.prepareStatement(sql);
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
	
	
}