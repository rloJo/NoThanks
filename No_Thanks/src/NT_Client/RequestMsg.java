package NT_Client;

import java.awt.Point;

public class RequestMsg {
	private static final long serialVersionUID = 1L;
	public final int black = 0;
	public final int white = 1;
	public final int view = 2;
	
	public String code;
	public String UserName;
	public String data;
	public Point point; // 바둑돌 놓을 때 사용
	public int role;
	
	public long roomId;
	public String roomName;
	public String password;
	public boolean isPass;
	public int userCount;
	public int status;

	public RequestMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}
