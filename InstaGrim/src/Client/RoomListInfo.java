package Client;

public class RoomListInfo {

	private String roomTitle;
	private String roomPw;
	private String userCount;
	private String totalPerson;

	public RoomListInfo(String roomTitle, String roomPw, String userCount, String totalPerson) {
		this.roomTitle = roomTitle;
		this.roomPw = roomPw;
		this.userCount = userCount;
		this.totalPerson = totalPerson;
	}

	public RoomListInfo(String roomTitle, String totalPerson) {
		this.roomTitle = roomTitle;
		this.totalPerson = totalPerson;
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}

	public String getRoomPw() {
		return roomPw;
	}

	public void setRoomPw(String roomPw) {
		this.roomPw = roomPw;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(String totalPerson) {
		this.totalPerson = totalPerson;
	}

}
