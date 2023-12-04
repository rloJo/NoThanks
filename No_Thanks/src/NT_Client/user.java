package NT_Client;

import java.util.ArrayList;

class user {
	private static int user_count = 1; // default 유저 이름을 지어주기 위한 변수
	private String name;			   // 유저 이름
	private int token;				   // 게임 진행에 필요한 유저가 갖는 토큰
	private ArrayList<Integer> cards;  // 유저가 획득한 카드
	private int test;
	
	public user() {
		name = "#" + user_count + " user";
		token = 11;
		cards = new ArrayList<Integer>();
		user_count++;
	}
	
	public user(String u_name) {
		//user_count++;
		name = u_name;
		token = 11;
		cards = new ArrayList<Integer>();
	}
	
	public static int get_userCount(){
		return user_count;
	}
	
	public void set_token(int game_token) {
		this.token += game_token;
	}
	
	public int get_token() {	
		return this.token;
	}
	
	public String get_name() {	
		return this.name;
	}
	
	public void set_cards(int card_num) {
		cards.add(card_num);
		// 승자를 가리기위한 정렬 ?
	}
	
	public ArrayList<Integer> get_cards() {
		return this.cards;
	}
	
}

