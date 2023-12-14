package NT_Client;

public class NTRoom {
	private static int roomNum = 0; 
	private String roomName = "";
	private String hostName = "";
	private int userCount = 0;
	private int status = 0;
	private boolean isPass = false;
	private String roomPass="";
	
	NTRoom(String hostName, String roomName, boolean isPass, String roomPass){
		roomNum++;
		this.roomName = roomName;
		this.userCount++;
		this.isPass = isPass;
		this.hostName = hostName;
		if(this.isPass)
		{
			this.roomPass = roomPass;
		}
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
