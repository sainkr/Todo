package ConnectJSP;

import ConnectDB.ChallengeTodo;

public class ChallengeTodoCnt {
	
	private ChallengeTodo challengejsp = ChallengeTodo.getInstance();

	private String host_id;
	private String num;
	private String result;


	public ChallengeTodoCnt(String type, String host_id, String code, String num) {
		this.host_id = host_id;
		this.num = num;
		
		if(type.equals("getChallengetodo"))
			getChallengetodo(host_id, code);
		else if(type.equals("setChallengetodo"))
			setChallengetodo(host_id, code, num);
	}
	
	public void getChallengetodo(String host_id, String code) {
		result = challengejsp.getChallengetodo(host_id, code);
	}
	
	public void setChallengetodo(String host_id, String code, String num) {
		result = challengejsp.setChallengetodo(host_id, code, num);
	}
	
	
	public String getResult() {
		return result;
	}
	
	
}
