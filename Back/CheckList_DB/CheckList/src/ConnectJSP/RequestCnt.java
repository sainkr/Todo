package ConnectJSP;

import ConnectDB.Request;

public class RequestCnt {
	
	private Request requestjsp = Request.getInstance();

	private String request_id;
	private String target_id;
	private String result;

	public RequestCnt(String request_id, String target_id) {
		this.request_id = request_id;
		this.target_id = target_id;
		addRequest(request_id, target_id);
	}
	
	public void addRequest(String request_id, String target_id) {
		result = requestjsp.addRequest(request_id, target_id);
	}
	
	public String getResult() {
		return result;
	}
	
	
}
