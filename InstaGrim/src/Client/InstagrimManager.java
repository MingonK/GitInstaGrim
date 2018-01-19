package Client;

import java.io.IOException;

public class InstagrimManager {
	public String login(String id, String pw) { // 로그인을 처리하기 위해 만든 함수
		try {
			Client.getDos().writeUTF("login¿"); // 로그인을 하겠다고 서버로 구분자 전달
			Client.getDos().flush();

			Client.getDos().writeUTF(id); // 서버로 id필드에 적혀있는 데이터 전달
			Client.getDos().writeUTF(pw); // 서버로 pw필드에 적혀있는 데이터 전달
			Client.getDos().flush();

			boolean loginSuccess = Client.getDis().readBoolean();
			boolean AlreadyCon = Client.getDis().readBoolean();
			if (loginSuccess) { // 정상적으로 가입된 id, pw정보가 존재한다면 환영하는 메세지 띄움
				if (AlreadyCon == false) {
					return "success";
				} else {
					return "already connected";
				}
			} else {
				return "check id or pw";
			}
		} catch (Exception e) {
			return "error";
		}
	}

	public boolean logout() {
		try {
			Client.getDos().writeUTF("exit");
			Client.getDos().flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void sendMessage(String userID, String chattingMsg) {
		try {
			Client.getDos().writeUTF("chatMsg¿" + userID + ": " + chattingMsg + "\n");
			Client.getDos().flush();
		} catch (Exception e1) {
		}
	}
}
