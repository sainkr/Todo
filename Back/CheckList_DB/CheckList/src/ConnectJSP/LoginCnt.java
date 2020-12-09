package ConnectJSP;

import ConnectDB.Login;

public class LoginCnt {
	private Login loginjsp = Login.getInstance();

	private String type;
	private String id;
	private String password;
	private String name;
	private String result;
	
	public LoginCnt(String type, String id, String password, String name) {
		this.type = type;
		this.id = id;
		this.password = password;
		this.name = name;
	
		if(type.equals("login")) {
			getProfile(id,password);
		}
		else if(type.equals("join")) {
			addProfile(id, password, name);
		}
		else if(type.equals("getTodo")) {
			getTodo(id);
		}
		else if(type.equals("getWeather")) {
			getWeather(id);
		}
	}
	
	
	
	public void getProfile(String id, String password) {
		result = loginjsp.getProfile(id,password);
	}
	
	public void addProfile(String id, String password,String name) {
		result = loginjsp.addProfile(id,password,name);
	}
	
	public void getTodo(String id) {
		result = loginjsp.getTodo(id);
	}
	
	public void getWeather(String id) {
		result = loginjsp.getWeather(id);
	}
	
	public String getResult() {
		return result;
	}
	
	
}
