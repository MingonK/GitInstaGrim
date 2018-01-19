package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ConnectionThread extends Thread {

	private DBManager manager = new DBManager(); // 데이터베이스를 다른 클래스에서 다시 인스턴스를 생성하지 않게 하도록 static으로 인스턴스 생성
	private DataOutputStream dos; // DataOutputStream을 static으로 선언해서 데이터의 전송을 편리하게 하기 위한 변수
	private DataInputStream dis; // DataInputStream을 static으로 선언해서 데이터의 입력을 편리하게 하기 위한 변수

	private String clientId;
	private Iterator<String> iterator;
	public static ArrayList<String> keys;
	public static RoomManager roomManager;
	private LoginInfo userInfo;
	private ArrayList<LoginInfo> loginList = null;

	// --------------------------------------InGame에 필요한 것들
	GameInfo roomInfo;
	private String getText; // 채팅내용
	// --------------------------------------InGame에 필요한 것들

	public ConnectionThread(Socket sock) {
		this.iterator = MainServer.clientMap.keySet().iterator();
		keys = new ArrayList<String>();
		try {
			this.loginList = manager.AllgetData();
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());
		} catch (Exception e) {
		}
		roomManager = new RoomManager();
	}

	@Override
	public void run() {
		try {
			while (true) {
				String msgType = dis.readUTF(); // 메세지의 타입에 구별
				String delimiter[] = msgType.split("¿");
				if (delimiter[0].equals("joinMember")) { // 만약 메세지 타입이 회원가입을 위한 것이라면 회원가입을 위한 기능 실행
					String clientId = dis.readUTF(); // 받은 클라이언트의 ID를 clientId라는 변수에 저장해서 데이터베이스에 존재하는지 확인해야 함
					boolean isIdExist = manager.isIdExist(clientId); // DBManager클래스에 isIDExist()메서드를 가지고 아이디의 중복을 확인함
					dos.writeBoolean(isIdExist); // 아이디가 존재하면 true, 존재하지 않으면 false를 받어서 클라이언트로 전송
					dos.flush();
					if (!isIdExist) { // 아이디가 존재하지 않는 경우
						String clientPw = dis.readUTF(); // 클라이언트의 PW를 clientPw라는 변수에 저장한다.
						String clientName = dis.readUTF();
						String clientEmail = dis.readUTF();
						int result = manager
								.insertData(new LoginInfo(clientId, clientPw, clientName, clientEmail, 1, 0, 0, 0, 0)); // 데이터베이스에
						// 저장한다.
						dos.writeInt(result);
						dos.flush();
					}
				}

				if (delimiter[0].equals("findID")) { // 전달받은 메세지의 타입이 findID일 때 아이디 찾기를 실행
					String clientname = dis.readUTF();
					String clientemail = dis.readUTF();
					// ↑ 전달받은 이름과 이메일 주소를 지역변수인 clientname, clientemail에 저장
					boolean findId = manager.findID(clientname, clientemail);
					// ↑ findID라는 메소드를 통해 가입된 정보가 있는지 없는지 확인
					dos.writeBoolean(findId);
					dos.flush();
					// ↑ 반환되어 오는 boolean데이터를 클라이언트로 전송, 그래서 있는지 없는지 확인하고 메세지를
					// 보여줄 때 필요
					// ↓ 반환된 정보가 true이면 데이터베이스 내에 가입된 정보가 있는 것
					if (findId) {
						ArrayList<LoginInfo> loginList = manager.AllgetData();
						// ↑ 가입된 정보를 모두 가져와서 ↓ 반복문 돌며 원하는 결과가 있는 지 검색
						for (int i = 0; i < loginList.size(); i++) {
							if (loginList.get(i).getName().equals(clientname)
									&& loginList.get(i).getEmailAddress().equals(clientemail)) {
								// ↓ 찾은 결과인 아이디를 클라이언트로 보내줌
								dos.writeUTF(loginList.get(i).getLoginId());
								dos.flush();
							}
						}
					}
				}
				// ↓ 메제지 타입이 findPW일 경우 비밀번호찾기 실행
				if (delimiter[0].equals("findPW")) {
					String clientId = dis.readUTF();
					String clientname = dis.readUTF();
					String clientemail = dis.readUTF();
					// ↑ 클라이언트로부터 id, 이름, 이메일주소를 받아 각각을
					// clientId, clientname, clientemail 라는 지역변수에 저장
					boolean findPw = manager.findPW(clientId, clientname, clientemail);
					dos.writeBoolean(findPw);
					dos.flush();
					// 받아온 id, 이름, 이메일정보로 데이터베이스목록에 있는지 확인하고 그 결과를 boolean값으로 받아서
					// 있는지 없는지 확인
					// ↓ 데이터베이스 내에 원하는 정보가 있으면 true를 반환하므로 아래 조건문 실행
					if (findPw) {
						ArrayList<LoginInfo> loginList = manager.AllgetData();
						// ↑ 검색결과를 loginList로 받아와서 ↓ loginList에 찾고자 하는 정보가 있는지 검색
						for (int i = 0; i < loginList.size(); i++) {
							if (loginList.get(i).getLoginId().equals(clientId)
									&& loginList.get(i).getName().equals(clientname)
									&& loginList.get(i).getEmailAddress().equals(clientemail)) {
								// ↓ 찾은 결과인 비밀번호를 클라이언트로 전송
								dos.writeUTF(loginList.get(i).getLoginPw());
								dos.flush();
							}
						}
					}
				}
				// ↓ 로그인을 하겠다라는 구분자로 "login"을 받았을 때
				if (delimiter[0].equals("login")) {
					this.clientId = dis.readUTF();
					String clientPw = dis.readUTF();
					// ↑ 전달받은 아이디와 비밀번호를 지역변수인 clientId, clientPw에 저장
					boolean loginSuccess = manager.login(clientId, clientPw);
					// ↑ DBManager 클래스의 login메소드를 통해 아이디와 비밀번호정보가 있는지 없는지 확인
					dos.writeBoolean(loginSuccess);
					// ↑ 결과가 true이면 정보가 있는 것, false이면 없는 것이므로 이 결과를 클라이언트에게 전달
					dos.flush();

					boolean AlreadyCon = false;
					while (iterator.hasNext()) {
						String key = iterator.next();
						if (key.equals(clientId)) {
							AlreadyCon = true;
						}
					}
					dos.writeBoolean(AlreadyCon);
					dos.flush();

					if (loginSuccess && !AlreadyCon) {
						addClient(clientId, dos);
					}
					roomManager.updateRoomList();
				}
				if (delimiter[0].equals("chatMsg")) {
					String clientMsg = delimiter[1];
					if (!clientMsg.equals("chatMsg")) {
						sendMessage(clientMsg);
					}
				}
				if (delimiter[0].equals("makeroom")) {
					String roomCreater = delimiter[1];
					String roomTitle = delimiter[2];
					String roomPw = delimiter[3];
					String totalPerson = delimiter[4];

					int clientLv = 0;
					int clientRecord = 0;
					int clientWin = 0;
					int clientLose = 0;
					int clientExp = 0;

					Iterator<String> iterator = MainServer.clientMap.keySet().iterator();
					String key = "";
					while (iterator.hasNext()) {
						key = iterator.next();
						if (roomCreater.equals(MainServer.clientMap.get(key).getId())) {
							clientLv = MainServer.clientMap.get(key).getClientLv();
							clientRecord = MainServer.clientMap.get(key).getClientRecord();
							clientWin = MainServer.clientMap.get(key).getClientWin();
							clientLose = MainServer.clientMap.get(key).getClientLose();
							clientExp = MainServer.clientMap.get(key).getClientExp();
							break;
						}
					}
					userInfo = new LoginInfo(roomCreater, clientLv, clientRecord, clientWin, clientLose, clientExp, 0);

					if (MainServer.roomList.size() == 0) {
						roomManager.createRoom(userInfo, roomTitle, roomPw, totalPerson);
						dos.writeUTF("createRoom¿");
						dos.flush();
						MainServer.clientMap.get(roomCreater).setRoomTitle(roomTitle);
						roomManager.userTable(roomTitle);
						String msg = roomCreater + " 님이 입장하셨습니다.";
						roomManager.inGameMessage(roomTitle, msg);

					} else {
						for (int i = 0; i < MainServer.roomList.size(); i++) {
							if (!roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
								roomManager.createRoom(userInfo, roomTitle, roomPw, totalPerson);
								dos.writeUTF("createRoom¿");
								dos.flush();
								MainServer.clientMap.get(roomCreater).setRoomTitle(roomTitle);
								roomManager.userTable(roomTitle);

								String msg = roomCreater + " 님이 입장하셨습니다.";
								roomManager.inGameMessage(roomTitle, msg);
								break;
							} else {
								dos.writeUTF("uncreatedRoom¿");
								dos.flush();
							}
						}
					}
				}
				if (delimiter[0].equals("updateRoomList")) {
					roomManager.updateRoomList();
				}
				if (delimiter[0].equals("exit")) {
					removeClient(clientId);
					dos.close();
					dis.close();
				}
				if (delimiter[0].equals("enteredroom")) {
					String roomTitle = delimiter[1];
					String enteringID = delimiter[2];

					int clientLv = 0;
					int clientRecord = 0;
					int clientWin = 0;
					int clientLose = 0;
					int clientExp = 0;

					Iterator<String> iterator = MainServer.clientMap.keySet().iterator();
					String key = "";
					while (iterator.hasNext()) {
						key = iterator.next();
						if (enteringID.equals(MainServer.clientMap.get(key).getId())) {
							clientLv = MainServer.clientMap.get(key).getClientLv();
							clientRecord = MainServer.clientMap.get(key).getClientRecord();
							clientWin = MainServer.clientMap.get(key).getClientWin();
							clientLose = MainServer.clientMap.get(key).getClientLose();
							clientExp = MainServer.clientMap.get(key).getClientExp();
							break;
						}
					}
					userInfo = new LoginInfo(enteringID, clientLv, clientRecord, clientWin, clientLose, clientExp, 0);

					String Admissibility = roomManager.enterRoom(userInfo, roomTitle);
					if (Admissibility.equals("comein")) {
						dos.writeUTF(Admissibility + "¿");
						dos.flush();
						roomManager.updateRoomList();
						MainServer.clientMap.get(enteringID).setRoomTitle(roomTitle);
						roomManager.userTable(roomTitle);
						String msg = enteringID + " 님이 접속하셨습니다.";
						roomManager.inGameMessage(roomTitle, msg);
					} else if (Admissibility.equals("fail")) {
						dos.writeUTF(Admissibility + "¿");
						dos.flush();
					} else if (Admissibility.equals("insertPw")) {
						dos.writeUTF(Admissibility + "¿");
						dos.flush();
					} else if (Admissibility.equals("playing")) {
						dos.writeUTF(Admissibility + "¿");
						dos.flush();
					}
				}
				if (delimiter[0].equals("enteredroomPw")) {
					String roomTitle = delimiter[1];
					String password = delimiter[2];
					String Admissibility = roomManager.enterPwExactly(userInfo, roomTitle, password);
					dos.writeUTF(Admissibility + "¿");
					dos.flush();
					if (Admissibility.equals("comein")) {
						MainServer.clientMap.get(userInfo.getLoginId()).setRoomTitle(roomTitle);
						roomManager.userTable(roomTitle);
					}
				}
				if (delimiter[0].equals("exitroom")) {
					String clientId = delimiter[1];
					String roomTitle = delimiter[2];
					userInfo = new LoginInfo(clientId);
					for (int i = 0; i < MainServer.roomList.size(); i++) {
						GameRoom room = MainServer.roomList.get(i);
						for (int j = 0; j < room.getUserList().size(); j++) {
							if (clientId.equals(room.getUserList().get(j).getLoginId())) {
								room.ExitRoom(room.getUserList().get(j));
								break;
							}
						}
					}

					roomManager.updateRoomList();
					for (int i = 0; i < roomManager.getUsersInfo().size(); i++) {
						if (userInfo.getLoginId().equals(roomManager.getUsersInfo().get(i).getLoginId())) {
							roomManager.getUsersInfo().remove(i);
							break;
						}
					}
					MainServer.clientMap.get(clientId).setRoomTitle(null);
					MainServer.clientMap.get(clientId).setReady(false);
					roomManager.userTable(roomTitle);
					String msg = clientId + " 님이 퇴장하셨습니다.";
					roomManager.inGameMessage(roomTitle, msg);
				}

				if (delimiter[0].equals("game")) { // 게임 명령 받음
					String function = delimiter[1]; // function을 통해 기능을 나눠줌
					String roomTitle = delimiter[2];

					if (function.equals("canvas")) {
						String drawOrMove = delimiter[3];
						if (drawOrMove.equals("draw")) {
							String draw = dis.readUTF();
							roomManager.draw(roomTitle, draw);
						} else if (drawOrMove.equals("move")) {
							String move = dis.readUTF();
							roomManager.moved(roomTitle, move);
						}

					} else if (function.equals("clearBt")) {
						roomManager.sendClearBt(roomTitle);

					} else if (function.equals("inGameMsg")) {
						// 채팅을 원함
						getText = delimiter[3];
						String msg = (clientId + " : " + getText);
						if (!getText.equals("")) {
							roomManager.inGameSendMessage(roomTitle, msg, getText, clientId);
						}

					} else if (function.equals("ready")) {
						String id = delimiter[3];
						String isReady = delimiter[4]; // 레디중인지 아닌지 받아와서 저장함
						boolean ready = false;

						if (isReady.equals("false")) {
							ready = false;
						} else if (isReady.equals("true")) {
							ready = true;
						}
						roomManager.readyCheck(roomTitle, id, ready);

					} else if (function.equals("setAnswerNumZero")) {
						roomManager.setAnswerNumZero(roomTitle);
					}
				}
			}
		} catch (Exception e) {
			int gameUser = 0;
			removeClient(clientId);
			String msg = clientId + " 님이 퇴장하셨습니다.";
			// 만약 유저가 게임방에서 강제 종료한 경우
			String roomTitle = MainServer.clientMap.get(clientId).getRoomTitle();
			roomManager.inGameMessage(roomTitle, msg);
			roomManager.userExitTable(roomTitle, clientId);

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
					gameUser++;
					// 반복문을 돌며 같은 방에 있는 유저의 수를 다시 카운트
				}
			}

			if (gameUser < 1) {
				for (int i = 0; i < MainServer.roomList.size(); i++) {
					if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
						MainServer.roomList.remove(MainServer.roomList.get(i));
						break;
						// 만약 게임 방에 있는 유저가 0명이라면 룸을 지워야 함
					}
				}
			}
			MainServer.clientMap.remove(clientId);
			// 그리고 Server에 있는 ClientMap에서 유저를 삭제
		}
	}

	public ArrayList<String> addkey() {
		Iterator<String> iterator = MainServer.clientMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			keys.add(key);
		}
		return keys;
	}

	public void removeClient(String clientId) {
		MainServer.clientMap.remove(clientId);
		Iterator<String> iterator = MainServer.clientMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			try {
				MainServer.clientMap.get(key).getDos().writeUTF("remove¿" + clientId);
				MainServer.clientMap.get(key).getDos().flush();
			} catch (Exception e) {
			}
			keys.remove(clientId);
		}
	}

	public void addClient(String clientId, DataOutputStream dos) {
		GameInfo gameInfo = new GameInfo();
		gameInfo.setDos(dos);
		gameInfo.setId(clientId);
		MainServer.clientMap.put(clientId, gameInfo);
		updateUserList(addkey());
	}

	// 유저가 접속할 때마다 키 값이 맨 처음부터 돌도록 설정해야 전체리스트를 전체 유저한테 뿌려줄 수 있음
	public void updateUserList(ArrayList<String> keys) {
		int clientLv = 0;
		int clientRecord = 0;
		int clientWin = 0;
		int clientLose = 0;
		int clientExp = 0;

		Iterator<String> iterator = MainServer.clientMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			for (int i = 0; i < keys.size(); i++) {
				for (int j = 0; i < loginList.size(); j++) {
					if (keys.get(i).equals(loginList.get(j).getLoginId())) {
						clientLv = loginList.get(j).getLevel();
						clientRecord = loginList.get(j).getRecord();
						clientWin = loginList.get(j).getWin();
						clientLose = loginList.get(j).getLose();
						clientExp = loginList.get(j).getExp();
						break;
					}
				}
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("connect¿" + keys.get(i) + "¿" + clientLv + "¿"
							+ clientRecord + "¿" + clientWin + "¿" + clientLose + "¿" + clientExp);
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	public void sendMessage(String msg) {
		Iterator<String> iterator = MainServer.clientMap.keySet().iterator(); // key셋으로 반복자지정
		String key = "";

		while (iterator.hasNext()) {
			key = iterator.next();
			try {
				DataOutputStream clientDos = MainServer.clientMap.get(key).getDos();
				clientDos.writeUTF("chatMsg¿" + msg);
				clientDos.flush();
			} catch (Exception e) {
			}
		}
	}
}