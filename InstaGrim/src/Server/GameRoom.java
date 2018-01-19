package Server;

import java.util.ArrayList;

public class GameRoom {

	private String roomTitle;
	private String roomPw;
	private String totalPerson;
	private String statusGame = "대기중";

	private ArrayList<LoginInfo> userList;

	public GameRoom(String roomTitle, String roomPw, String totalPerson, LoginInfo gameUser) {
		MainServer.clientMap.get(gameUser.getLoginId()).setRoomTitle(roomTitle);
		this.roomTitle = roomTitle;
		this.roomPw = roomPw;
		this.totalPerson = totalPerson;

		userList = new ArrayList<LoginInfo>();
		userList.add(gameUser);
	}

	public void EnterRoom(LoginInfo user) {
		userList.add(user);
	}

	public void ExitRoom(LoginInfo user) {
		for (int i = 0; i < userList.size(); i++) {
			if (user.getLoginId().equals(userList.get(i).getLoginId())) {
				userList.remove(userList.get(i));
			}
		}
		
		if (userList.size() < 1) {
			for (int i = 0; i < MainServer.roomList.size(); i++) {
				if (MainServer.clientMap.get(user.getLoginId()).getRoomTitle()
						.equals(MainServer.roomList.get(i).getRoomTitle())) {
					GameRoom room = MainServer.roomList.get(i);
					MainServer.roomList.remove(room);
					break;
				}
			}
			return;
		}
	}

	public String getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(String totalPerson) {
		this.totalPerson = totalPerson;
	}

	public int getUserCount() {
		return userList.size();
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public String getRoomPw() {
		return roomPw;
	}

	public ArrayList<LoginInfo> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<LoginInfo> userList) {
		this.userList = userList;
	}

	public String getStatusGame() {
		return statusGame;
	}

	public void setStatusGame(String statusGame) {
		this.statusGame = statusGame;
	}
}
