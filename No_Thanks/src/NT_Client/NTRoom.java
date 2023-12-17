package NT_Client;

import java.util.Vector;

public class NTRoom {
	private int roomId;
	private String roomName;
	private int mode ; // 0 normal  1 special
	private int userCount = 0;
	private int status = 0;
	private boolean isPass = false;
	private String passWd;
	
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
	
	public boolean getIsPass() {
		return this.isPass;
	}
	
	public String getPassWd() {
		return this.passWd;
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
		
	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}
	
	public void setUserCount () {
		this.userCount++;
	}
	
	public void setStatus(int i) {
		this.status = i;
		
	}
}
