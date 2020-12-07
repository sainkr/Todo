package ConnectJSP;

import ConnectDB.ChallengeAdd;

public class ChallengeAddCnt {
	
	private ChallengeAdd challengejsp = ChallengeAdd.getInstance();

	private String name;
	private String host_id;
	private String code;
	private String add_member;
	private String delete_member;
	private String result;


	public ChallengeAddCnt(String type, String name, String host_id, String code, String add_member, String delete_member) {
		this.name = name;
		this.host_id = host_id;
		this.code = code;
		this.add_member = add_member;
		this.delete_member = delete_member;
		
		if(type.equals("addChallenge"))
			addChallenge(name, host_id, add_member);
		else if(type.equals("setChallenge"))
			setChallenge(name, host_id, code, add_member, delete_member);
		else if(type.equals("deleteChallenge"))
			deleteChallenge(host_id, code);
	}
	
	public void addChallenge(String name, String host_id, String member) {
		result = challengejsp.addChallenge(name, host_id, member);
	}
	
	public void setChallenge(String name, String host_id, String code, String add_member, String delete_member) {
		result = challengejsp.setChallenge(name, host_id, code, add_member, delete_member);
	}
	
	public void deleteChallenge( String host_id, String code) {
		result = challengejsp.deleteChallenge(host_id, code);
	}
	
	
	public String getResult() {
		return result;
	}
	
	
}
