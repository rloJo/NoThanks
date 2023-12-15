package NT_Server;

public class NTRoom {
	//�Ⱦ�
    //private static int roomNumCounter = 0; // Ŭ���� ������ roomNum�� �����ϰ� �����ϱ� ���� static ����
    private int roomNum; // �� �� ��ü�� ������ ��ȣ
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

    // Getter �޼��� �߰�
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
