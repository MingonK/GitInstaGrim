package Server;

public class LoginInfo {
	private int seq;
	private String loginId;
	private String loginPw;
	private String name;
	private String emailAddress;
	private int level;
	private int record;
	private int win;
	private int lose;
	private int exp;
	private int getScore;

	public LoginInfo(String loginId, String loginPw, String name, String emailAddress, int level, int record, int win, int lose,
			int exp) {
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.name = name;
		this.emailAddress = emailAddress;
		this.level = level;
		this.record = record;
		this.win = win;
		this.lose = lose;
		this.exp = exp;
	}
	
	public LoginInfo(String loginId, int getScore) {
		this.loginId = loginId;
		this.getScore = getScore;
	}
	
	public LoginInfo(String loginId) {
		this.loginId = loginId;
	}

	public int getGetScore() {
		return getScore;
	}

	public void setGetScore(int getScore) {
		this.getScore = getScore;
	}

	public LoginInfo(String loginId, int level, int record, int win, int lose, int exp, int getScore) {
		this.loginId = loginId;
		this.level = level;
		this.record = record;
		this.win = win;
		this.lose = lose;
		this.exp = exp;
		this.getScore = getScore;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPw() {
		return loginPw;
	}

	public void setLoginPw(String loginPw) {
		this.loginPw = loginPw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
