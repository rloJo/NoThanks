package common;

import java.io.Serializable;

public class Msg implements Serializable{
	public final int p1 = 1;
	public final int p2 = 2;
	public final int p3 = 3;
	public final int p4 = 4;
	
	private int roomId;
	private String roomName;
	private String passWd;
	private boolean isPass;
	private int userCount;
	private int status = 0 ;
	private int mode;
	private int role = 0;
	private int card;
	private int token =0;
	private String code;
	private String userName;
	private String data;
	
	public Msg(String userName, String code, String data) {
		this.code = code;
		this.userName = userName;
		this.data = data;
	}
	
	//Getter 메소드 추가
	public String getRoomName() {
		return roomName;
	}
	public int getRoomId() {
		return roomId;
	}
	
	public boolean getIsPass() {
		return isPass;
	}
	
	public String getPassWd() {
		return passWd;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public int getUserCount() {
		return userCount;
	}
	
	public int getCard() {
        return card;
    }
	
	public String getCode() {
		return code;
	}
	public String getData() {
		return data;
	}
	
	public int getMode() {
		return mode;
	}
	
	public int getStatus() {
		return status;
	}
	
	public int getRole() {
		return role;
	}
	
	public int getToken() {
		return token;
	}
	
	//Setter 메서드 추가
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}
	
	public void setPassWd(String passwd) {
		this.passWd = passWd ;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setCard(int card) {
        this.card = card;
    }
	
	public void upToken() {
		this.token++;
	}
	
	public void setToken(int token) {
		this.token += token;
	}
	
}


	
	

