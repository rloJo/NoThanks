package NT_Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import common.Msg;
import javax.swing.ScrollPaneConstants;

public class LobbyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFrame mainFrame;
	private LobbyPanel lobbyPanel;
	public InGamePanel IngamePanel;
	private Container container;
	private CardLayout cardLayout;
	private CreateRoomFrame createRoomFrame;
	
	private JLabel waitingroominfoLabel;
	public Vector<NTRoom> ntRooms = new Vector<> ();
	private JList<String> userlist; 
	private DefaultListModel<String> userModel;
	private JList<String> roomlist;
	private DefaultListModel<String> roomModel;
	private JLabel chatinfoLabel;
	private JScrollPane scrollPane;
	private TextArea textArea;
	private JLabel userinfoLabel;
	private TextField chatTextField;
	private JButton sendBtn;
	private JButton CreateBtn;
	
	private static final int BUF_LEN = 128;
	
	public String userName;
	public int roomId;
	private Socket socket;
    private ObjectInputStream ois;
	private ObjectOutputStream oos;
    

    
	public LobbyPanel(Container container, JFrame mainFrame,String userName, String ip_addr, String port_num) {
		SendMsgBtnClick sendAction = new  SendMsgBtnClick();
		CreateRoomBtnClick createAction = new CreateRoomBtnClick();
		
		lobbyPanel = this;
		this.mainFrame = mainFrame;
		this.container = container;
		this.cardLayout = (CardLayout) container.getLayout();
		
		
		
		setBackground(new Color(240, 240, 240));
		setLayout(null);
		
		this.userName = userName;
		waitingroominfoLabel = new JLabel("���ȣ    ������              �ο���     ���� ���     �� ����");
		waitingroominfoLabel.setBackground(new Color(240, 240, 240));
		waitingroominfoLabel.setFont(new Font("����", Font.BOLD, 29));
		waitingroominfoLabel.setBounds(96, 10, 800, 45);
		add(waitingroominfoLabel);
		
		roomModel = new DefaultListModel<String>();
		
		chatinfoLabel = new JLabel("��ü ä��");
		chatinfoLabel.setFont(new Font("����", Font.BOLD, 24));
		chatinfoLabel.setBounds(96, 306, 119, 33);
		add(chatinfoLabel);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(96, 336, 543, 183);
		add(scrollPane);
		
		textArea = new TextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 17));
		textArea.setEditable(false);
		
		userinfoLabel = new JLabel("���� ���");
		userinfoLabel.setFont(new Font("����", Font.BOLD, 24));
		userinfoLabel.setBounds(641, 306, 119, 33);
		add(userinfoLabel);
	
		chatTextField = new TextField();
		chatTextField.setBounds(96, 525, 456, 31);
		add(chatTextField);
		chatTextField.addActionListener(sendAction);
        chatTextField.requestFocus();
		     
		sendBtn = new JButton("����");
		sendBtn.setFont(new Font("����", Font.BOLD, 18));
		sendBtn.setBounds(558, 529, 71, 31);
		add(sendBtn);
		sendBtn.addActionListener(sendAction);
		
		userModel = new DefaultListModel<String>();
		userlist = new JList<String>(userModel);
		userlist.setFont(new Font("����", Font.BOLD, 22));
		userlist.setBounds(651, 336, 245, 183);
		add(userlist);
		
		AppendText("[" + userName + "]�� �����Ͽ����ϴ�.\n");
		this.userName = userName;
		
		CreateBtn = new JButton("�� ����");
		CreateBtn.setFont(new Font("����", Font.BOLD, 18));
		CreateBtn.setBounds(650, 525, 97, 32);
		add(CreateBtn);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(96, 52, 800, 244);
		add(scrollPane_1);
		roomlist = new JList<String>(roomModel);
		scrollPane_1.setViewportView(roomlist);
		roomlist.setFont(new Font("����", Font.BOLD, 27));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(663, 374, 2, 2);
		add(scrollPane_2);
		roomlist.addMouseListener(new roomListClick());
		CreateBtn.addActionListener(createAction);

        try {
            socket = new Socket(ip_addr, Integer.parseInt(port_num));
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

           
            Msg loginMsg = new Msg(userName,"Login", "login");
            sendObject(loginMsg);
            updateRoomList();
            ListenNetwork net = new ListenNetwork();
            net.start();
            
               
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
	}
	
	
	//roomList�� ���� Ŭ���ϸ� �ش�� ���� 
	class roomListClick extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // ����Ŭ�� Ȯ��
                int selectedIndex = roomlist.getSelectedIndex();
                
                if (selectedIndex == -1)// ����Ŭ���� ���� ���� ��������
                	return;
                
                NTRoom selectedRoom = ntRooms.get(selectedIndex);            
                Msg msg = new Msg(userName,"EnterRoom","�� ����");
                msg.setRoomId(ntRooms.get(selectedIndex).getRoomId());
                sendObject(msg);	
                
            }
        }	
	}
	
	// keyboard enter key�� �����ų� ���� ��ư Ŭ���ϸ� ������ ���� 
	class SendMsgBtnClick implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == chatTextField) {
				Msg msg = new Msg(userName,"AllChat",String.format("[%s] %s", userName, chatTextField.getText()));
				sendObject(msg);
				chatTextField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
			}
		}
	}
	
	// �� ���� ��ư ������ ���� â �߰���
	class CreateRoomBtnClick implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			createRoomFrame = new CreateRoomFrame(lobbyPanel);
			createRoomFrame.setVisible(true);
		}
	}
	
	//CreateRoomFrame���� �� ����� ��ư�� ������ ����Ǵ� �Լ�
	public void createRoom(String roomName,int roomType)
	{
		IngamePanel = new InGamePanel(container, lobbyPanel, this.userName);
		container.add(IngamePanel,"IngamePanel");
		cardLayout.show(container, "IngamePanel");
		
		Msg msg = new Msg(userName,"CreateRoomInfo","�� ����");
		msg.setUserName(userName);
		msg.setRoomName(roomName);
		msg.setMode(roomType);
		sendObject(msg);
	}
	
	//server�� �޼��� ��ü(Msg)�� �����ϴ� �޼ҵ� 
	public void sendObject(Object obj)
	{
		try {
			if(obj instanceof Msg) {
				oos.writeObject(obj);
			}
		} catch (Exception e) {
			System.err.println("sendObject error: " + e.getMessage());
	        e.printStackTrace();
		}
	}
	
	//server�� ���� �� ���������� ä�� �޽��� ���� �޼ҵ�
	public void sendChatMessage(String message) {
		try {
			Msg msg = new Msg(userName,"RoomChat",message);
			msg.setRoomId(IngamePanel.roomId);
			oos.writeObject(msg);
		} catch (Exception e) { 
			System.out.println("sendChatMessage error");
		}
	}
	
	 //Ŭ���̾�Ʈ�� roomList�� ������Ʈ �ϱ� ���� �޼ҵ�
    public void updateRoomList() {
		for(int i=0; i<ntRooms.size(); i++) {
			NTRoom room = ntRooms.get(i);
			String status = "";
			if(room.getStatus() == 0) status = "��� ��";
			else status = "���� ��";
			String formattedString;
        	if(room.getRoomName().length() > 6)
        		formattedString= room.getRoomName().substring(0,6) + "...";
        	else {
        		formattedString = room.getRoomName();
        		for(int j=0;j<18-room.getRoomName().length();j++)
        			formattedString +=" ";
        	}
        	           
        	String roomInfo = String.format("%-10d    %8s   %5d/4           %s       %s"
        			,room.getRoomId()
        			,formattedString
        			,room.getUserCount()
        			,room.getMode() == 0 ? "normal" : "special"
        			,status);
			roomModel.set(i, roomInfo);
		}
	}
	
	
    // ȭ�鿡 ���
    public void AppendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }  
    
 // Server Message�� �����ؼ� ȭ�鿡 ǥ��
    class ListenNetwork extends Thread {
    	public ListenNetwork() {
    		Msg roomList = new Msg(userName, "RoomList", "roomList");
    		sendObject(roomList);
    	}
    	
        public void run() {
            while (true) {
                try {
                	// ���� �޼����� code�� ���� �ٸ� �ൿ ����
                    Object obj = null;
                    String message = null;
                    Msg msg;
                    try {
                    	obj = ois.readObject();
                    } catch(ClassNotFoundException e) {
                    	e.printStackTrace();
                    	break;
                    }
                    
                    if(obj == null)
                    	break;
                    
                    if(obj instanceof Msg) {
                    	msg = (Msg)obj;
                    } else {
                    	continue;
                    }
                    
                    /* ---------------- code �з� ----------------------------*/
                    /*	"Login" -> �α��� 100
                     *  "AllChat" -> ��ü ä�� 111
                     *  "CreateRoomInfo" -> �� ���� ������ �˸���. 200
                     *  "EnterRoom" -> ������ �濡 �����ߴٴ� ����. 201
                     *  
                     *  
                     *  
                     *  "RoomFull" -> �濡 �ο��� �� �Ǵ� ��й�ȣ ���� 202
                     *  "RoomList" - ������ ���� ��ü �� ����� ���۵� ��� 210
                     *  "UserList - ������ ���� ��ü ������ ����� ���۵� ��� 211
                     *  
                     *  
                     *  
                     *  300 - ���� ����
                     *  301 - �ش� ī�带 �Դ´ٴ� ���� ����
                     *  302 - �ش� ī�� �Ա� �ź� ���� ����
                     *  320 - ��Ⱑ ������ ���ڰ� ���� ����
                     *  400 - 
                     *  
                     *  420 - ���� ���� 
                     *  430 - ����� �� ����
                     *  500 - �ش� �� ä��
                     *  510 - �ش� �� user ǥ��
                     *  
       
                     */
                    
                    switch (msg.getCode())
                    {
                    
                    case "logout" :
                    	
                    case "AllChat":
                    	String chat = msg.getData();
                    	AppendText(chat +"\n");
                    	break;
                    
                    
                    case "CreateRoomInfo":
                    	int roomId = msg.getRoomId();
                    	String roomName = msg.getRoomName();
                    	int userCount = msg.getUserCount();
                    	String gameMode = (msg.getMode() == 0) ? "normal" : "special"; 
                    	String status = (msg.getStatus() == 0) ? "��� ��" : "���� ��"; 
                    	
                    	NTRoom newRoom = new NTRoom(roomId);
                    	ntRooms.addElement(newRoom);
                    	String formattedString;
                    	
                    	if(msg.getRoomName().length() > 6)
                    		formattedString= msg.getRoomName().substring(0,6) + "...";
                    	else {
                    		formattedString = msg.getRoomName();
                    		for(int i=0;i<18-msg.getRoomName().length();i++)
                    			formattedString +=" ";
                    	}
                    	           
                    	String roomInfo = String.format("%-10d    %8s   %5d/4           %s       %s"
                    			,msg.getRoomId()
                    			,formattedString
                    			,msg.getUserCount()
                    			,msg.getMode() == 0 ? "normal" : "special"
                    			,status);
                    	roomModel.addElement(roomInfo);
                    	break;
                    
                    case "EnterRoom":
                    	lobbyPanel.roomId = msg.getRoomId();
                    	for(int i=0; i<ntRooms.size();i++)
                    	{
                    		NTRoom room = ntRooms.get(i);
                    		if(room.getRoomId() == msg.getRoomId()) {             
                    			room.users.add(lobbyPanel);
                    		}
              
                    	}
              
                    	
                    	IngamePanel = new InGamePanel(container, lobbyPanel, msg.getUserName());
                    	IngamePanel.roomId = msg.getRoomId();
                    	container.add(IngamePanel,"IngamePanel");
                    	IngamePanel.role = msg.getRole();
                    	if(msg.getRole() == msg.p1) {
                    		IngamePanel.p1_nameLabel.setText(msg.getData());
                    	}
                    	
                    	if(msg.getRole() == msg.p2) {
                    		StringTokenizer tokenizer = new StringTokenizer(msg.getData());                             
                    		IngamePanel.p1_nameLabel.setText(tokenizer.nextToken());                    
                    		IngamePanel.p2_nameLabel.setText(tokenizer.nextToken());
                    		
                    	}
                    	
                    	if(msg.getRole() == msg.p3) {
                    		StringTokenizer tokenizer = new StringTokenizer(msg.getData());                             
                    		IngamePanel.p1_nameLabel.setText(tokenizer.nextToken());
                    		IngamePanel.p2_nameLabel.setText(tokenizer.nextToken());
                    		IngamePanel.p3_nameLabel.setText(tokenizer.nextToken());
                        }  
                    	
                    	if(msg.getRole() == msg.p4) {
                    		StringTokenizer tokenizer = new StringTokenizer(msg.getData());                             
                    		IngamePanel.p1_nameLabel.setText(tokenizer.nextToken());
                    		IngamePanel.p2_nameLabel.setText(tokenizer.nextToken());
                    		IngamePanel.p3_nameLabel.setText(tokenizer.nextToken());
                    		IngamePanel.p4_nameLabel.setText(tokenizer.nextToken());
                        }
                    	
              	
                    	cardLayout.show(container, "IngamePanel");
                    	IngamePanel.roomUserList();
                    	break;
                    	
                    case "RoomRefresh" :
                    	//IngamePanel.roomUserModel.removeAllElements();
                        String allUser = msg.getData();
                        StringTokenizer st2 = new StringTokenizer(allUser);
                        StringTokenizer st3 = new StringTokenizer(allUser);
                        int index=0;
                        while(st2.hasMoreElements()) {
                        	
                            String name = st2.nextToken();
                            index++;
                            
                        }
                        if(index == 2) {
                        	IngamePanel.p1_nameLabel.setText(st3.nextToken());                    
                    		IngamePanel.p2_nameLabel.setText(st3.nextToken());
                        }
                        if(index == 3) {
                        	IngamePanel.p1_nameLabel.setText(st3.nextToken());                    
                    		IngamePanel.p2_nameLabel.setText(st3.nextToken());
                    		IngamePanel.p3_nameLabel.setText(st3.nextToken());
                        }
                        if(index == 4) {
                        	IngamePanel.p1_nameLabel.setText(st3.nextToken());                    
                    		IngamePanel.p2_nameLabel.setText(st3.nextToken());
                    		IngamePanel.p3_nameLabel.setText(st3.nextToken());
                    		IngamePanel.p4_nameLabel.setText(st3.nextToken());
                        }
                    	break;
         
                    case "GameStartMsg":      
                    	IngamePanel.AppendChat(msg.getUserName(),msg.getData());
                    	
                    	if(IngamePanel.role == msg.p1)
                    	{            
                    		IngamePanel.openBtn.setEnabled(true);
                    	}
                  
                    	break;
                    
                    case "CheckCard":
                    	String label = "���� ī�� �� : " + msg.getToken();
                    	IngamePanel.card.setText(Integer.toString(msg.getCard()));
                    	IngamePanel.cardLabel.setText(label);
                    	if(msg.getRole() == IngamePanel.role)
                    	{                 
                    		IngamePanel.gameDialog = new gameDialog(lobbyPanel);
                    		IngamePanel.gameDialog.setVisible(true);
                    	}
                    	break;	
                    	               
                    case "Eat" :               
                    	IngamePanel.AppendChat(msg.getUserName(),msg.getData());
                    	if(msg.getRole() == IngamePanel.role)
                    	{
                    		IngamePanel.openBtn.setEnabled(true);
                    		IngamePanel.gameDialog = null;
                    		IngamePanel.token += IngamePanel.token_stack;                   		
                    	}
                    	IngamePanel.token_stack = 0;
                    	switch(msg.getRole())
                    	{
                    		case 1 :
                    			IngamePanel.p1_cardList.add(msg.getCard());                   			          
                    			break;                   			                   			
                    		case 2 :
                    			IngamePanel.p2_cardList.add(msg.getCard());                  			
                    			break;                 			
                    		case 3 :
                    			IngamePanel.p3_cardList.add(msg.getCard());                    			
                    			break;                   			
                    		case 4 :
                    			IngamePanel.p4_cardList.add(msg.getCard());                 
                    			break;         	                                	             
                    	}
                    	IngamePanel.setCard();
                    	break;
                    
                    case "NoEat" : 
                    	IngamePanel.AppendChat(msg.getUserName(),msg.getData());
                    	IngamePanel.token_stack += msg.getToken();
                    	if(msg.getRole() == IngamePanel.role)
                    	{
                    		IngamePanel.token--;
                    		IngamePanel.openBtn.setEnabled(false);
                    	}
                    	
                    	if(IngamePanel.role == (msg.getRole()%4+1))
                    	{
                    		IngamePanel.gameDialog = new gameDialog(lobbyPanel);
                    		IngamePanel.gameDialog.setVisible(true);
                    	}
                    	
                    	break;
                    	             	
                    	// ���� �濡 ���� �� �� ���� ���
					case "RoomFull":
						JOptionPane.showMessageDialog(mainFrame, msg.getData(), "error", JOptionPane.ERROR_MESSAGE);
						break;
						
					case "RoomList" :
						int roomId2 = msg.getRoomId(); // �� ID
						String roomName2 = msg.getRoomName(); // �� �̸�
						int peopleCount2 = msg.getUserCount(); // �� �ο� ��
						
						NTRoom newRoom2 = new NTRoom(roomId2); // ���ο� �� �����
						newRoom2.setRoomName(roomName2);
						newRoom2.setUserCount(peopleCount2);
						newRoom2.setStatus(msg.getStatus());
						ntRooms.add(newRoom2); // �� ����Ʈ�� �߰�
						
						if(newRoom2.getUserCount() == 4) 
							newRoom2.setStatus(1);
						String s = "";
						if(newRoom2.getStatus() == 0) s = "��� ��";
						else s = "���� ��";
						
						String RLformattedString = "";
						if(msg.getRoomName().length() > 6)
                    		RLformattedString= msg.getRoomName().substring(0,6) + "...";
                    	else {
                    		RLformattedString = msg.getRoomName();
                    		for(int i=0;i<18-msg.getRoomName().length();i++)
                    			RLformattedString +=" ";
                    	}
						
						String roomStr2 = String.format("%-10d    %8s   %5d/4           %s       %s"
                    			,msg.getRoomId()
                    			,RLformattedString
                    			,msg.getUserCount()
                    			,msg.getMode() == 0 ? "normal" : "special"
                    			,s);
			
						int RLindex = 0;
						for (int i = 0; i < roomModel.getSize(); i++) {
						    String currentString = roomModel.getElementAt(i);
						    if (Character.getNumericValue(currentString.charAt(0)) == msg.getRoomId()) {
						        RLindex = i;
						        roomModel.set(RLindex, roomStr2);
						    }
						}

						roomlist.setModel(roomModel);
						roomlist.repaint();
						break;
						
					case "UserList":
                    	String str = msg.getData();
                    	StringTokenizer tokenizer = new StringTokenizer(str);
                    	userModel.removeAllElements();
                    	while(tokenizer.hasMoreTokens()) {
                    		String user = tokenizer.nextToken();
                    		userModel.addElement(user);
                    	}
                    	break;					
						                   	                    	
                                   	
					case "End" :
						String endStr = msg.getData();
						IngamePanel.AppendChat(msg.getUserName(),endStr);
						IngamePanel.openBtn.setEnabled(false);
						Msg endMsg = new Msg(userName,"Winner","���� �Ǻ�");
						endMsg.setUserName(userName);
						if(lobbyPanel.IngamePanel.role == 1)
						{
							endMsg.p_cards = IngamePanel.p1_cardList;
							endMsg.setToken(IngamePanel.token);
							endMsg.setRoomId(IngamePanel.roomId);
							sendObject(endMsg);
						}
						else if(lobbyPanel.IngamePanel.role == 2)
						{
							endMsg.p_cards = IngamePanel.p2_cardList;
							endMsg.setToken(IngamePanel.token);
							endMsg.setRoomId(IngamePanel.roomId);
							sendObject(endMsg);
						}
						else if(lobbyPanel.IngamePanel.role == 3)
						{
							endMsg.p_cards = IngamePanel.p3_cardList;
							endMsg.setToken(IngamePanel.token);
							endMsg.setRoomId(IngamePanel.roomId);
							sendObject(endMsg);
						}
						else if(lobbyPanel.IngamePanel.role == 4)
						{
							endMsg.p_cards = IngamePanel.p4_cardList;
							endMsg.setToken(IngamePanel.token);
							endMsg.setRoomId(IngamePanel.roomId);
							sendObject(endMsg);
						}
						
						break;
						
					case "Winner" :
						IngamePanel.AppendChat(msg.getUserName(), msg.getData());
						break;
				
                    case "RoomChat":
						IngamePanel.AppendChat(msg.getUserName(), msg.getData());
						break;    
						
                    /*case "GameOver":
						int gameOverRoomId = msg.getRoomId();
						for(int i=0; i<ntRooms.size(); i++) {
							NTRoom o = ntRooms.get(i);
							if(o.getRoomId() == gameOverRoomId) {
								ntRooms.remove(i);
								roomModel.remove(i);
								roomlist.setModel(roomModel);
								roomlist.repaint();
								break;
							}
						}
						break;*/
                    }                
                }
                 catch (IOException e) {
                    AppendText("���� �߻�");
                    try {
                        oos.close();
                        ois.close();
                        socket.close();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                } //c
            }//w
        }//run
    }
}
  