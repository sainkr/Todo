package ConnectJSP;

import ConnectDB.Logout;

public class LogoutCnt {
	private Logout logoutjsp = Logout.getInstance();

	private String type;
	private String id;
	private String date;
	private String check;
	private String weather;
	private String result;
	
	public LogoutCnt(String type, String id,String date, String content, String check, String weather) {
		this.type = type;
		this.id = id;
		this.check = check;
		this.weather = weather;
	
		if(type.equals("saveTodo")) {
			setTodo(id, date, content, check);
		}
		else if(type.equals("saveWeather")) {
			setWeather(id, date, weather);
		}
		else if(type.equals("delete")) {
			deleteTodo(id);
		}

	}
	
	
	public void setTodo(String id, String date, String content, String check) {
		result = logoutjsp.setTodo(id, date, content, check);
	}
	
	public void setWeather(String id, String date, String weather) {
		result = logoutjsp.setWeather(id, date, weather);
	}
	
	public void deleteTodo(String id) {
		result = logoutjsp.deleteTodo(id);
	}
	
	public String getResult() {
		return result;
	}
	
	
}
