package ConnectJSP;

import ConnectDB.Challenge;

public class ChallengeCnt {
	
	private Challenge challengejsp = Challenge.getInstance();

	private String name;
	private String host_id;
	private String member;
	private String result;


	public ChallengeCnt(String type, String name, String host_id, String member) {
		this.name = name;
		this.host_id = host_id;
		this.member = member;
		
		if(type.equals("addChallenge"))
			addChallenge(name, host_id, member);

	}
	
	public void addChallenge(String name, String host_id, String member) {
		result = challengejsp.addChallenge(name, host_id, member);
	}
	
	
	public String getResult() {
		return result;
	}
	
	
}
