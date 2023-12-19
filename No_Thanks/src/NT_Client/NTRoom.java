package NT_Client;

import java.util.Vector;

//클라이언트에서 게임 방을 관리하기 위한 클래스
public class NTRoom {
	private int roomId; //방 번호
	private String roomName; // 방 제목
	private int mode ; // 방 모드 0 normal  1 special
	private int userCount = 0; // 방에 유저 수
	private int status = 0; // 방 상태 0 대기중 1 게임중 
	
	public Vector<LobbyPanel> users = new Vector<>();
	
	NTRoom(int roomId){
		this.roomId = roomId;
	}
	
	//getter 메소드 
	public int getRoomId() {
		return this.roomId;
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public int getUserCount() {
		return this.userCount;
	}
	
	public String getRoomName() {
		return this.roomName;
	}
	
	public int getStatus() {
		return this.status;
	}

	
	public int getUsers() {
		return this.users.size();
	}
	//Setter 메소드 
	
	public void setRoomId(int RoomId) {
		this.roomId = roomId;
	}
    
    public void setRoomName(String roomName) {
    	this.roomName = roomName;
    }
			
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	public void setUserCount () {
		this.userCount++;
	}
	
	public void setStatus(int i) {
		this.status = i;
		
	}
}
