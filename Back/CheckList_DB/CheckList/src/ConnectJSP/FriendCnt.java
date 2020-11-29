package ConnectJSP;

import ConnectDB.Friend;

public class FriendCnt {
	
	private Friend requestjsp = Friend.getInstance();

	private String request_id;
	private String target_id;
	private String result;

	public FriendCnt(String type, String request_id, String target_id) {
		this.request_id = request_id;
		this.target_id = target_id;
		
		if(type.equals("addFriend"))
			addRequest(request_id, target_id);
		else
			getRequest(request_id);
	
	}
	
	public void addRequest(String request_id, String target_id) {
		result = requestjsp.addRequest(request_id, target_id);
	}
	
	public void getRequest(String request_id) {
		result = requestjsp.getRequest(request_id);
	}
	
	public String getResult() {
		return result;
	}
	
	
}
