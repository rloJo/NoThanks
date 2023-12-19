package NT_Server;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class NTRoom {
	public final int special_Size = 31; // 각 모드에 따른 카드 수
	public final int normal_Size = 33;
	public ArrayList<Pair> cardList = new ArrayList<Pair>();
	public int total;
    private int roomId = 0; // 각 방 객체의 고유한 번호
    private String roomName;
    public Vector<String> users = new Vector<>();
    private int [] cards = new int [36];
    private int userCount; //방의 인원 수
    private int status; //방의 상태
    private String roomType;
    private int mode; // 방 모드 변수 0:normal 1:special
    public int index =0;
      
    public NTRoom(int roomId, String roomName, String roomType, String roomManager) {
 	
        this.roomId = roomId;
        this.roomName = roomName;
        this.users.add(roomManager);
        this.mode = (roomType == "normal") ? 0 : 1;
        this.total = (mode == 0) ? normal_Size : special_Size; 
        this.userCount = users.size();
        set_cards(this.mode); // 초기 카드 배열을 mode에 따라 설정
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
    
    //승자를 구하는 메소드 
    // 연속된 숫자면 제일 작은 숫자만 더하고 , 연속되지 않으면 더한다
    // ex) 카드가 3 4 5 6 7 11 일 때
    // 3부터 7까지는 연속적이므로 제일 작은 3을 더하고 11은 끊김으로 3 + 11
    // 마지막으로 토큰을 빼면 끝
    public int sum_cards(ArrayList<Integer> array, int token) {
    	int sum = 0;
    	int index = 0;
    	// 3 5 6 7 
    	for(int i=0;i<array.size();i++)
    	{
    		if(array.get(i) - index !=1)
    		{
    			sum+=array.get(i);
    			index = array.get(i);
    		}
    		else {
    			index = array.get(i);
    		}
    	}
    	
    	return sum - token;
    }    
    
    public Pair find_winner() {
		int min = 100000;
		int index =0;
		int token =0 ;
		String userName = " ";
		Pair result;
    	for(int i=0; i<cardList.size();i++)
		{
			if(min > cardList.get(i).total)
			{
				min = cardList.get(i).total;
				index =i;
				userName =  cardList.get(i).userName;
				token = cardList.get(i).token;
			}
		}
    	result = new Pair(cardList.get(index).cards,userName,token);
    	
    	return result;
	}
    
     
    // Getter 메서드 추가
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
    
    public int getMode() {
		return this.mode;
	}
    
    //Setter 메소드 추가
    
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
