package Server;

public class RoomInfo {
	
	private String roomTilte;
	private String roomPw;
	private String totalPerson;
	
	public RoomInfo(String roomTitle, String roomPw, String totalPerson) {
		this.roomTilte = roomTitle;
		this.roomPw = roomPw;
		this.totalPerson = totalPerson;
	}

	public String getRoomTilte() {
		return roomTilte;
	}

	public void setRoomTilte(String roomTilte) {
		this.roomTilte = roomTilte;
	}

	public String getRoomPw() {
		return roomPw;
	}

	public void setRoomPw(String roomPw) {
		this.roomPw = roomPw;
	}

	public String getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(String totalPerson) {
		this.totalPerson = totalPerson;
	}
	
}
