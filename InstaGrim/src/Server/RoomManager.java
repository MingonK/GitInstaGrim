package Server;

import java.util.ArrayList;
import java.util.Vector;

public class RoomManager {

	private ArrayList<LoginInfo> usersInfo;
	private GameRoom room;
	protected String function;
	// ---------------------------inGame에 필요한 것들
	private Thread startGame;
	private String quizWriter; // 출제자 저장하는곳
	private String sendQuiz; // 퀴즈
	private int minimunUser = 2; // 게임 시작 최소 인원
	private int quizNum = 5; // 총 몇문제 출제 할 지 결정
	private boolean stopGameCountdown;
	private boolean isQuizAnswer = false; // 퀴즈 정답인지
	private int quizCountdown;

	private String[] quiz = { "고래", "원숭이", "스폰지밥", "잔치국수", "발굴단", "거미", "토끼", "거북이", "자동차", "자전거", "물고기", "나무늘보",
			"가는말이 고와야 오는말이 곱다", "낮말은 새가 듣고 밤말은 쥐가 듣는다", "스마트폰", "지갑", "호날두", "고등어", 
			"꽁치", "고양이", "강아지", "카카오톡", "배", "컴퓨터", "노트북" }; // 문제 설정
	private int num; // 퀴즈 배열 번호

	// ---------------------------inGame에 필요한 것들

	public void userTable(String roomTitle) { // 방 만들어지거나 유저 들어올 경우, 서버에서 방 만들어지면 그 제목을 끌어와야함
		usersInfo = new ArrayList<LoginInfo>();
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				// 접속된 유저들 중 같은 방에 있는 유저라면,
				try {
					String tableNick = MainServer.clientMap.get(key).getId();
					int getScore = MainServer.clientMap.get(key).getRightAnswerNum();
					usersInfo.add(new LoginInfo(tableNick, getScore));
					// 룸 매니저가 관리하고 있는 usersInfo라는 ArrayList에 유저정보 추가 
				} catch (Exception e) {
				}
			}
		}

		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				// 게임 방에 있는 유저 리스트를 갱신하기 위한 반복문
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("setNumRows¿");
					MainServer.clientMap.get(key).getDos().flush();

					for (int j = 0; j < usersInfo.size(); j++) {
						MainServer.clientMap.get(key).getDos().writeUTF(
								"userTable¿" + usersInfo.get(j).getLoginId() + "¿" + usersInfo.get(j).getGetScore());
						MainServer.clientMap.get(key).getDos().flush();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public String enterRoom(LoginInfo gameUser, String roomTitle) {
		String roomPw = null;
		int totalPerson = 0;
		GameRoom room = null;
		String Admissibility = "fail";

		for (int i = 0; i < MainServer.roomList.size(); i++) {
			if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
				room = MainServer.roomList.get(i);
				roomPw = MainServer.roomList.get(i).getRoomPw();
				totalPerson = Integer.parseInt(MainServer.roomList.get(i).getTotalPerson());
				break;
			}
		}

		if (roomPw.isEmpty()) {
			if (room.getUserCount() < totalPerson && room.getStatusGame().equals("대기중")) {
				for (int i = 0; i < MainServer.roomList.size(); i++) {
					if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
						MainServer.roomList.get(i).EnterRoom(gameUser);
						break;
					}
				}
				Admissibility = "comein";
				return Admissibility;
			} else if (room.getStatusGame().equals("게임중")) {
				Admissibility = "playing";
				return Admissibility;
			} else {
				return Admissibility;
			}
		} else {
			if (room.getStatusGame().equals("게임중")) {
				Admissibility = "playing";
				return Admissibility;
			}
			Admissibility = "insertPw";
			return Admissibility;
		}
	}

	public String enterPwExactly(LoginInfo gameUser, String roomTitle, String enteredPw) {
		String Admissibility = "notmatch";
		for (int i = 0; i < MainServer.roomList.size(); i++) {
			if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())
					&& enteredPw.equals(MainServer.roomList.get(i).getRoomPw())) {
				MainServer.roomList.get(i).EnterRoom(gameUser);
				Admissibility = "comein";
			}
		}
		return Admissibility;
	}

	public void createRoom(LoginInfo userInfo, String roomTitle, String roomPw, String totalPerson) {
		room = new GameRoom(roomTitle, roomPw, totalPerson, userInfo);
		MainServer.roomList.add(room);
	}

	public void updateRoomList() {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (MainServer.roomList.size() == 0) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("setGameListInit¿");
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			} else {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("setGameListInit¿");
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
				for (int j = 0; j < MainServer.roomList.size(); j++) {
					String roomsTitle = MainServer.roomList.get(j).getRoomTitle();
					String roomsPw = MainServer.roomList.get(j).getRoomPw();
					int userCount = MainServer.roomList.get(j).getUserCount();
					String totalPersons = MainServer.roomList.get(j).getTotalPerson();
					String statusGame = MainServer.roomList.get(j).getStatusGame();
					try {
						MainServer.clientMap.get(key).getDos().writeUTF("updateroom¿" + roomsTitle + "¿" + roomsPw + "¿"
								+ userCount + "¿" + totalPersons + "¿" + statusGame);
						MainServer.clientMap.get(key).getDos().flush();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public void userExitTable(String roomTitle, String exitId) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("setNumRows¿");
					MainServer.clientMap.get(key).getDos().flush();
					for (int j = 0; j < usersInfo.size(); j++) {
						if (!usersInfo.get(j).getLoginId().equals(exitId)) {
							MainServer.clientMap.get(key).getDos().writeUTF("userTable¿" + usersInfo.get(j).getLoginId()
									+ "¿" + usersInfo.get(j).getGetScore());
							MainServer.clientMap.get(key).getDos().flush();
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public void draw(String roomTitle, String draw) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF(draw);
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	public void moved(String roomTitle, String move) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF(move);
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	public void sendClearBt(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("clearBt");
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	public void inGameMessage(String roomTitle, String msg) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("inGameMsg¿" + msg);
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	public void inGameSendMessage(String roomTitle, String msg, String getText, String nick) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					if (MainServer.clientMap.get(key).isDoGame() == false) {
						// 게임중이 아니므로 정답자가 없음
						MainServer.clientMap.get(key).getDos().writeUTF("inGameMsg¿" + msg);
						MainServer.clientMap.get(key).getDos().flush();
					} else if (MainServer.clientMap.get(key).isDoGame() == true) {
						sendQuiz = MainServer.clientMap.get(nick).getQuiz();
						if (sendQuiz.equals(getText)) {
							// 정답자가 있을 경우.
							MainServer.clientMap.get(nick).setQuizAnswer(true); // 정답자의 키값의 quizAnswer에 true 줌
							MainServer.clientMap.get(key).getDos().writeUTF("inGameMsg¿" + msg);
							MainServer.clientMap.get(key).getDos().flush();
						} else {
							// 정답자가 없을 경우.
							MainServer.clientMap.get(key).getDos().writeUTF("inGameMsg¿" + msg);
							MainServer.clientMap.get(key).getDos().flush();
						}
					}
				} catch (Exception e) {
				}

			}
		}
	}

	// -------------------------------------------------------------레디기능
	public void readyCheck(String roomTitle, String id, boolean isReady) {
		try {
			int readyUser = 0; // 현재 레디하고있는 유저 수
			int gameUser = 0;
			if (isReady == false) {
				MainServer.clientMap.get(id).setReady(true);
				for (int i = 0; i < ConnectionThread.keys.size(); i++) {
					String key = ConnectionThread.keys.get(i);
					if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
						gameUser++;
						if (MainServer.clientMap.get(key).isReady() == true) {
							readyUser++;
						}
					}
				}
				if (gameUser == readyUser && readyUser >= minimunUser) { // 인원이 minimumUser 이상이고 모두 레디상태일 경우
					// 게임 시작
					startGame = new Thread() {
						public void run() {
							startCountdown(roomTitle);
							if (stopGameCountdown == true) {
								stopGameCountdown(roomTitle);
								stopGameCountdown = false;
							} else {
								startGame(roomTitle);
								finishGame(roomTitle);
							}
						}
					};
					startGame.start();
				}
			} else if (isReady == true) {
				MainServer.clientMap.get(id).setReady(false);
			}
		} catch (Exception e) {
		}

	}

	// ------------------------------------------------------------카운트다운 기능
	public void startCountdown(String roomTitle) {
		int startGameCountdown = 5; // 몇 초 뒤에 게임 시작할 지 설정
		while (startGameCountdown != 0) {
			int doGameUserTmp = doGameUserCount(roomTitle);
			try {
				for (int i = 0; i < ConnectionThread.keys.size(); i++) {
					String key = ConnectionThread.keys.get(i);
					if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
						if (doGameUserTmp < 2) {
							MainServer.clientMap.get(key).setStopGameCountdown(true);
							stopGameCountdown = true;
						}
						MainServer.clientMap.get(key).getDos()
								.writeUTF("startCountdown¿" + startGameCountdown + " 초 후 게임이 시작됩니다");
						MainServer.clientMap.get(key).getDos().flush();
					}
				}
				if (stopGameCountdown == true) {
					break;
				}
				Thread.sleep(1000);
				startGameCountdown--;
			} catch (Exception e) {
			}
		}
	}

	public int doGameUserCount(String roomTitle) {
		int result = 0;
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				result++;
			}
		}
		return result;
	}

	// ------------------------------------------------------------카운트다운 중지신호 받을 경우
	public void stopGameCountdown(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).setStopGameCountdown(false);
					MainServer.clientMap.get(key).getDos().writeUTF("stopGameCountdown¿");
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
	}

	// -----------------------------------------------------------게임 시작하라는 명령
	public void startGame(String roomTitle) {
		for (int i = 0; i < MainServer.roomList.size(); i++) {
			if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
				MainServer.roomList.get(i).setStatusGame("게임중");
			}
		}
		updateRoomList();

		Thread quizAnswerCheck = new Thread() {
			public void run() {
				while (true) {
					int i;
					for (i = 0; i < ConnectionThread.keys.size(); i++) {
						String key = ConnectionThread.keys.get(i);
						if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
							if (MainServer.clientMap.get(key).isQuizAnswer()) {
								isQuizAnswer = true;
								setNextQuizTrue(roomTitle);
							} else if (getQuizCountdown() == 0) {
								setNextQuizTrue(roomTitle);
							}
						}
					}
					i = 0;
				}
			}
		};
		quizAnswerCheck.start();

		for (int i = 1; i <= quizNum; i++) {
			Vector<String> quizWriters = getQuizWriter(roomTitle); // 출제자 받아옴
			sendQuiz(roomTitle); // 퀴즈 출제
			quizCountdown(roomTitle, quizWriters);
			setNextQuizFalse(roomTitle);
		}		
	}

	// ----------------------------------------------------------nextQuiz true로 변환
	public void setNextQuizTrue(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setNextQuiz(true);
			}
		}
	}

	// ----------------------------------------------------------nextQuiz false로 변환
	public void setNextQuizFalse(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setNextQuiz(false);
			}
		}
	}

	// ------------------------------------------------------------출제자 누구인지
	public Vector<String> getQuizWriter(String roomTitle) {
		Vector<String> quizWriters = new Vector<String>(); // 출제자 배열 만들어내는곳

		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				quizWriters.add(key);
			}
		}

		String keySet = null;
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			keySet = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(keySet).getRoomTitle())) {
				break;
			}
		}

		quizWriter = quizWriters.get(MainServer.clientMap.get(keySet).getWriterNum());
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setDoGame(true); // 출제자 받아오는 순간 유저들 doGame을 true로 변경(=게임중)
				MainServer.clientMap.get(key).setQuizWriter(quizWriter);
			}
		}

		String keySet2 = null;
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			keySet2 = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(keySet2).getRoomTitle())) {
				MainServer.clientMap.get(keySet2).setWriterNum(MainServer.clientMap.get(keySet2).getWriterNum() + 1);
			}
		}
		if (MainServer.clientMap.get(keySet2).getWriterNum() >= quizWriters.size()) {
			String keySet3 = null;
			for (int i = 0; i < ConnectionThread.keys.size(); i++) {
				keySet3 = ConnectionThread.keys.get(i);
				if (roomTitle.equals(MainServer.clientMap.get(keySet3).getRoomTitle())) {
					MainServer.clientMap.get(keySet3).setWriterNum(0);
				}
			}
		}
		return quizWriters;
	}

	// ----------------------------------------------------------퀴즈기능
	public void sendQuiz(String roomTitle) {
		num = (int) (Math.random() * quiz.length); // 문제 랜덤 설정
		sendQuiz = quiz[num];
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setReady(false); // 게임이 시작됐으므로 모든 ready값을 false로 되돌림
				MainServer.clientMap.get(key).setQuiz(sendQuiz); // 클라이언트 정보에 퀴즈 정답을 저장해둠
				try {
					MainServer.clientMap.get(key).getDos()
							.writeUTF("quizWriter¿" + MainServer.clientMap.get(key).getQuizWriter()); // 출제자가 누구인지 쏴줌
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}

		try {
			MainServer.clientMap.get(quizWriter).getDos()
					.writeUTF("quiz¿" + MainServer.clientMap.get(quizWriter).getQuiz()); // 출제자한테만 퀴즈 정답 보냄
			MainServer.clientMap.get(quizWriter).getDos().flush();
		} catch (Exception e) {
		}
	}

	// ---------------------------------------------------------퀴즈 카운트다운
	public void quizCountdown(String roomTitle, Vector<String> quizWriters) {
		int quizCountdown = 180;
		while (quizCountdown != 0) {
			setQuizCountdown(quizCountdown);
			try {
				for(int i = 0; i < quizWriters.size(); i++) {
					MainServer.clientMap.get(quizWriters.get(i)).getDos().writeUTF("quizCountdown¿" + "남은시간 : " + quizCountdown + "초");
					MainServer.clientMap.get(quizWriters.get(i)).getDos().flush();
					
				}
				quizCountdown--;
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			if (isQuizAnswer) { // 정답자가 있을 경우
				existQuizAnswer(roomTitle);
				setQuizNull(roomTitle); // 퀴즈값, 출제자, 정답자를 전부 비움
				isQuizAnswer = false;
				break;
			} else if (quizCountdown == 0) { // 정답자가 없을 경우
				noQuizAnswer(roomTitle);
				setQuizNull(roomTitle); // 퀴즈값, 출제자, 정답자를 전부 비움
				isQuizAnswer = false;
				break;
			} 
		}
	}
	// ----------------------------------------------------------정답자가 없을 경우
	public void noQuizAnswer(String roomTitle) {
		String rightQuizAnswer;
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					rightQuizAnswer = MainServer.clientMap.get(key).getQuiz();
					MainServer.clientMap.get(key).getDos().writeUTF("noQuizAnswer¿" + rightQuizAnswer); // 정답자가 없다고 알려줌
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
		}

	}

	// ---------------------------------------------------------해쉬맵의 퀴즈값과 출제자값을 전부
	// null로 바꿔줌
	public void setQuizNull(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setQuiz(null); // 퀴즈 전부 null로 돌림
				MainServer.clientMap.get(key).setQuizWriter(null); // 출제자 전부 null로 돌림
				MainServer.clientMap.get(key).setQuizAnswer(false); // 정답자 전부 false로 돌림
			}
		}
	}

	// ----------------------------------------------------------정답자가 있을 경우
	public void existQuizAnswer(String roomTitle) {
		String answer = null;
		String rightQuizAnswer = null;
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				if (MainServer.clientMap.get(key).isQuizAnswer() == true) {
					answer = MainServer.clientMap.get(key).getId();
					rightQuizAnswer = MainServer.clientMap.get(key).getQuiz();
				}
			}
		}
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				try {
					MainServer.clientMap.get(key).getDos()
							.writeUTF("existQuizAnswer¿" + answer + "¿" + rightQuizAnswer); // 정답자가 있다고 알려줌
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}
		MainServer.clientMap.get(answer).setRightAnswerNum(MainServer.clientMap.get(answer).getRightAnswerNum() + 1);
		userTable(roomTitle);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
		}
	}

	// ----------------------------------------------------------- 게임 끝 알림
	public void finishGame(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setReady(false);
				MainServer.clientMap.get(key).setDoGame(false);
				try {
					MainServer.clientMap.get(key).getDos().writeUTF("finishGame¿");
					MainServer.clientMap.get(key).getDos().flush();
				} catch (Exception e) {
				}
			}
		}

		for (int i = 0; i < MainServer.roomList.size(); i++) {
			if (roomTitle.equals(MainServer.roomList.get(i).getRoomTitle())) {
				MainServer.roomList.get(i).setStatusGame("대기중");
			}
		}
		updateRoomList();
	}

	// ----------------------------------------------------------- 점수 초기화
	public void setAnswerNumZero(String roomTitle) {
		for (int i = 0; i < ConnectionThread.keys.size(); i++) {
			String key = ConnectionThread.keys.get(i);
			if (roomTitle.equals(MainServer.clientMap.get(key).getRoomTitle())) {
				MainServer.clientMap.get(key).setRightAnswerNum(0);
			}
		}
		userTable(roomTitle);
	}

	public ArrayList<LoginInfo> getUsersInfo() {
		return usersInfo;
	}

	public int getQuizCountdown() {
		return quizCountdown;
	}

	public void setQuizCountdown(int quizCountdown) {
		this.quizCountdown = quizCountdown;
	}
}
