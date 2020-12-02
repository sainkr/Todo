package ConnectJSP;

import ConnectDB.ChallengeContent;

public class ChallengeContentCnt {
	
	private ChallengeContent challengejsp = ChallengeContent.getInstance();

	private String num;
	private String content;
	private String result;

	public ChallengeContentCnt(String type, String code, String num, String content) {
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