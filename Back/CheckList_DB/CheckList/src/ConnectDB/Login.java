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
					
					// id+todo 테이블 생성
					sql = "create table "+id+"todo ("
							+ "date varchar(10) not null,"
							+ "todo varchar(1000) not null,"
							+ "todo_check int not null )";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();	
					
					// id+friend 테이블 생성
					sql = "create table "+id+"friend ("
							+ "friend_id varchar(30) not null)";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();	
					
					// id+challenge 테이블 생성
					sql = "create table "+id+"challenge ("
							+ "challenge_code int not null)";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();	
					
					// id+challengetodo 테이블 생성
					sql = "create table "+id+"challengetodo ("
							+ "code int not null,"
							+ "num int not null,"
							+ "todo varchar(1000) not null,"
							+ "todo_check int not null)";
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
		  }
		  else
			  returns = "joinFail";
			
		return returns;
	}
	
	
	public String getProfile(String id, String password) {	
		try {
			System.out.println("id : " + id + " password : " + password);
			conn = cDB.getConn();
			sql = "select * from profile where id =? and password = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();		

			
			if(rs.next()) {
				String name = rs.getString("name");
				
				sql = "select * from "+id+"friend";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();	
				
				returns = name;
				
				/*
				 * JSONArray jary = new JSONArray();
				while(rs.next()) {
					JSONObject jobj = new JSONObject();
					
					sql = "select * from profile where id = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, rs.getString("friend_id"));
					rs_sub = pstmt.executeQuery();	
					
					if(rs_sub.next()) {
						jobj.put("friend_id",rs.getString("friend_id"));
						jobj.put("friend_name",rs_sub.getString("name"));
						jary.add(jobj);
					}
					
				}
				
				if(jary.size() > 0)
					returns = jary.toJSONString()+"닉네임"+name;
				else {
					returns = "loginSuccess "+name;
				}*/
	
			}
			else
				returns = "loginFail";
			
			
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
}