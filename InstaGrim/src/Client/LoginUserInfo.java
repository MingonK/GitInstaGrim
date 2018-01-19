package Client;

public class LoginUserInfo {
	
	private String clientLv;
	private String clientRecord;
	private String clientWin;
	private String clientLose;
	private String clientExp;

	public LoginUserInfo(String clientLv, String clientRecord, String clientWin, String clientLose, String clientExp) {
		this.clientLv = clientLv;
		this.clientRecord = clientRecord;
		this.clientWin = clientWin;
		this.clientLose = clientLose;
		this.clientExp = clientExp;
	}

	public String getClientLv() {
		return clientLv;
	}

	public void setClientLv(String clientLv) {
		this.clientLv = clientLv;
	}

	public String getClientRecord() {
		return clientRecord;
	}

	public void setClientRecord(String clientRecord) {
		this.clientRecord = clientRecord;
	}

	public String getClientWin() {
		return clientWin;
	}

	public void setClientWin(String clientWin) {
		this.clientWin = clientWin;
	}

	public String getClientLose() {
		return clientLose;
	}

	public void setClientLose(String clientLose) {
		this.clientLose = clientLose;
	}

	public String getClientExp() {
		return clientExp;
	}

	public void setClientExp(String clientExp) {
		this.clientExp = clientExp;
	}

}
