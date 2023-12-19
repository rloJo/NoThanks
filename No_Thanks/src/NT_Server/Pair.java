package NT_Server;

import java.util.ArrayList;

public class Pair {
	public String userName;
	public ArrayList<Integer> cards; 
	public int token;
	public int total;
	
	public Pair(ArrayList<Integer> cards, String userName, int token){
		this.cards = cards;
		this.userName = userName;
		this.token = token;
		this.total = sum_cards(cards,token);
	}
	
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
	
	
	
}