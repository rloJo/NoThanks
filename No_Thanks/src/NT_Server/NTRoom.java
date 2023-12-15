package NT_Server;

public class NTRoom {
	//안씀
    //private static int roomNumCounter = 0; // 클래스 내에서 roomNum을 유일하게 관리하기 위한 static 변수
    private int roomNum; // 각 방 객체의 고유한 번호
    private String roomName;
    private int userCount = 0;
    private int status;
    private boolean isPass;
    private String roomType;

    public NTRoom(int roomNum, String roomName,int userCount, String roomType, boolean isPass) {
        this.roomNum = roomNum;
        this.roomName = roomName;
        this.userCount++;
        this.isPass = isPass;
        this.roomType = roomType;
        //this.hostName = hostName;
        /*
        if (this.isPass) {
            this.roomPass = roomPass;
        }
        */
    }

    // Getter 메서드 추가
    public int getRoomNum() {
        return roomNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getStatus() {
        return status;
    }

    public boolean getIsPass() {
        return isPass;
    }
    public String getRoomType() {
		return this.roomType;
	}
}
