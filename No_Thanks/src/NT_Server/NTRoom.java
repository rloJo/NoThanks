package NT_Server;

import java.awt.EventQueue;
import java.util.Random;
import java.util.Vector;

public class NTRoom {
	public final int special_Size = 31; // �� ��忡 ���� ī�� ��
	public final int normal_Size = 33;
	
	public int total;
    private int roomId = 0; // �� �� ��ü�� ������ ��ȣ
    private String roomName;
    public Vector<String> users = new Vector<>();
    private int [] cards = new int [36];
    private int userCount; //���� �ο� ��
    private int status; //���� ����
    private boolean isPass; // ��й�ȣ ����
    private String passWd;
    private String roomType;
    private int mode; // �� ��� ���� 0:normal 1:special
    public int index =0;

    public NTRoom(int roomId, String roomName, String roomType, String roomManager, boolean isPass) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.users.add(roomManager);
        this.isPass = isPass; //��� ��ȣ ����
        this.mode = (roomType == "normal") ? 0 : 1;
        this.total = (mode == 0) ? normal_Size : special_Size; 
        this.userCount = users.size();
        set_cards(this.mode); // �ʱ� ī�� �迭�� mode�� ���� ����
    }
    
    public void set_cards (int roomType) // ī�带 ���� �޼ҵ� 
    {
    	if (roomType == 0)  // normal ��� �϶�
    	{
    		for(int i=3;i<36;i++)
    			cards[i-3] = i;
    		
    		shuffle_cards(cards,normal_Size);
    	}
    	
    	else // special ��� �϶� 
    	{
    		int i=3,k=0; // i�� 3���� 35�� ī��, k�� cards �迭 index ���߱� ���� ����
    		Random random = new Random();
    		int temp1, temp2;
    		do {
    			temp1 = random.nextInt(32)+3;
    			temp2 = random.nextInt(32)+3;
    		}while(temp1 == temp2);
  
    		while(i<36) {
    			if(i!=temp1 && i!= temp2) {
    				cards[k] =i;
    				k++;
    			}
    			i++;
    		}
    		shuffle_cards(cards,special_Size);
    	}
    }
    
    public void shuffle_cards(int [] array, int count) { //�迭�� �����ִ� �Լ�
    	Random rand = new Random();
    	for(int i=0;i<count; i++) {
    		int randIndexToSwap = rand.nextInt(count);
    		int temp = array[randIndexToSwap];
    		array[randIndexToSwap] = array[i];
    		array[i] = temp;
    	}
    }
     
    // Getter �޼��� �߰�
    public String getRoomName() {
        return roomName;
    }

    public int getRandCard() {
    	
        return cards[index];
    }
    
    
    public int getUserCount() {
        return users.size();
    }

    public int getStatus() {
        return status;
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
    
    public int getMode() {
		return this.mode;
	}
    
    //Setter �޼ҵ� �߰�
    
    public void setIndex() {
    	index++;
    }
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
	
	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
	
	public void setUserCount () {
		this.userCount = users.size();
	}
	
	public void setStatus(int i) {
		this.status = i;	
	}  
	
	public void setMode(int mode) {
		this.mode = mode;	
	}  
}
