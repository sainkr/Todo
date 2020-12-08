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
			addFriend(request_id, target_id);
		else if(type.equals("getFriend"))
			getFriend(request_id);
		else if(type.equals("deleteFriend"))
			deleteFriend(request_id, target_id);
	
	}
	
	public void addFriend(String request_id, String target_id) {
		result = requestjsp.addFriend(request_id, target_id);
	}
	
	public void getFriend(String request_id) {
		result = requestjsp.getFriend(request_id);
	}
	
	public void deleteFriend(String request_id, String target_id) {
		result = requestjsp.deleteFriend(request_id, target_id);
	}
	
	public String getResult() {
		return result;
	}
	
	
}
