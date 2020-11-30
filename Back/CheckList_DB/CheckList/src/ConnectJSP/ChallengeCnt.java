package ConnectJSP;

import ConnectDB.Challenge;

public class ChallengeCnt {
	
	private Challenge challengejsp = Challenge.getInstance();

	private String name;
	private String member;
	private String result;

	public ChallengeCnt(String type, String name, String member) {
		this.name = name;
		this.member = member;
		
		if(type.equals("addChallenge"))
			addChallenge(name, member);

	}
	
	public void addChallenge(String name, String member) {
		result = challengejsp.addChallenge(name, member);
	}
	
	
	public String getResult() {
		return result;
	}
	
	
}
