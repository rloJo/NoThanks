package NT_Client;

import java.util.Vector;

//Ŭ���̾�Ʈ���� ���� ���� �����ϱ� ���� Ŭ����
public class NTRoom {
	private int roomId; //�� ��ȣ
	private String roomName; // �� ����
	private int mode ; // �� ��� 0 normal  1 special
	private int userCount = 0; // �濡 ���� ��
	private int status = 0; // �� ���� 0 ����� 1 ������ 
	
	public Vector<LobbyPanel> users = new Vector<>();
	
	NTRoom(int roomId){
		this.roomId = roomId;
	}
	
	//getter �޼ҵ� 
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
	//Setter �޼ҵ� 
	
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
