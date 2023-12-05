package NT_Server;

public class NTRoom {
    private static int roomNumCounter = 0; // Ŭ���� ������ roomNum�� �����ϰ� �����ϱ� ���� static ����
    private int roomNum; // �� �� ��ü�� ������ ��ȣ
    private String roomName;
    private int userCount;
    private int status;
    private boolean isPass;
    private String roomType;

    public NTRoom(String roomName,String roomType, boolean isPass) {
        roomNumCounter++;
        this.roomNum = roomNumCounter;
        this.roomName = roomName;
        this.userCount = 0; // �ʱⰪ 0���� ����
        this.isPass = isPass;
        this.roomType = roomType;
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
}
