package NT_Server;

import java.util.Random;
import java.util.Vector;

public class NTRoom {
	final int special_Size = 30; // 각 모드에 따른 카드 수
	final int normal_Size = 32;
	
    private int roomId = 0; // 각 방 객체의 고유한 번호
    private String roomName;
    public Vector<String> users = new Vector<>();
    private int [] cards = new int [36];
    private int userCount; //방의 인원 수
    private int status; //방의 상태
    private boolean isPass; // 비밀번호 여부
    private String passWd;
    private int roomType; // 방 모드 변수 0:normal 1:special

    public NTRoom(int roomId, String roomName, String roomType, String roomManager, boolean isPass) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.users.add(roomManager);
        this.isPass = isPass; //비밀 번호 설정
        this.roomType = (roomType == "normal") ? 0 : 1;
        set_cards(this.roomType); // 초기 카드 배열을 mode에 따라 설정
    }
    
    public void set_cards (int roomType) // 카드를 섞는 메소드 
    {
    	if (roomType == 0)  // normal 모드 일때
    	{
    		for(int i=3;i<36;i++)
    			cards[i-3] = i;
    		
    		shuffle_cards(cards,normal_Size);
    	}
    	
    	else // special 모드 일때 
    	{
    		int i=3,k=0; // i는 3부터 35의 카드, k는 cards 배열 index 맞추기 위한 변수
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
    
    public void shuffle_cards(int [] array, int count) { //배열을 섞어주는 함수
    	Random rand = new Random();
    	for(int i=0;i<count; i++) {
    		int randIndexToSwap = rand.nextInt(count);
    		int temp = array[randIndexToSwap];
    		array[randIndexToSwap] = array[i];
    		array[i] = temp;
    	}
    }
    
    
    // Getter 메서드 추가
    public String getRoomName() {
        return roomName;
    }

    public int getUserCount() {
        return userCount;
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
    
    //Setter 메소드 추가
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
      
}
