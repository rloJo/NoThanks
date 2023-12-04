package NT_Client;

import java.util.ArrayList;

class user {
	private static int user_count = 1; // default ���� �̸��� �����ֱ� ���� ����
	private String name;			   // ���� �̸�
	private int token;				   // ���� ���࿡ �ʿ��� ������ ���� ��ū
	private ArrayList<Integer> cards;  // ������ ȹ���� ī��
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
		// ���ڸ� ���������� ���� ?
	}
	
	public ArrayList<Integer> get_cards() {
		return this.cards;
	}
	
}

