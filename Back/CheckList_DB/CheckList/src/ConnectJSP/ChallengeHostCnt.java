package ConnectJSP;

import ConnectDB.ChallengeHost;

public class ChallengeHostCnt {
	
	private ChallengeHost challengejsp = ChallengeHost.getInstance();

	private String num;
	private String content;
	private String result;

	public ChallengeHostCnt(String type, String code, String num, String content) {
		this.num = num;
		this.content = content;
		
		if(type.equals("addContent"))
			addContent(code, num, content);
		else if(type.equals("updateContent"))
			updateContent(code, num, content);
		else if(type.equals("deleteContent"))
			deleteContent(code, num);

	}
	
	public void addContent(String code, String num, String content) {
		result = challengejsp.addContent(code, num, content);
	}
	
	public void updateContent(String code, String num, String content) {
		result = challengejsp.updateContent(code, num, content);
	}
	
	public void deleteContent(String code, String num) {
		result = challengejsp.deleteContent(code, num);
	}
	

	public String getResult() {
		return result;
	}
	
	
}