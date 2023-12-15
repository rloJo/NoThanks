package NT_Client;

public class NTRoom {
	private static int roomNum = 0; 
	private String roomName = "";
	private String hostName = "";
	private int userCount = 0;
	private int status = 0;
	private boolean isPass = false;
	private String roomPass="";
	
	private String roomType = "";
	
	NTRoom(int roomNum, String roomName, String roomType, boolean isPass){
		this.roomNum = roomNum;
		this.roomName = roomName;
		//this.userCount++;
		this.isPass = isPass;
		this.roomType = roomType;
		//this.hostName = hostName;
		//비밀번호 잠시 보류
		/*if(this.isPass)
		{
			this.roomPass = roomPass;
		}
		*/
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
	public String getRoomType() {
		return this.roomType;
	}
}
