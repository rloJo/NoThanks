package NT_Client;

public class NTRoom {
	private static int roomNum = 0; 
	private String roomName = "";
	private int userCount = 0;
	private int status = 0;
	private boolean isPass = false;
	
	NTRoom(String roomName, boolean isPass){
		roomNum++;
		this.roomName = roomName;
		this.userCount++;
		this.isPass = isPass;
	}
	
	public int getRoomNum() {
		return this.roomNum;
	}
	
	public int getUserCount() {
		return this.userCount;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public boolean getIsPass() {
		return this.isPass;
	}
}
