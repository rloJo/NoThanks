package NT_Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class LobbyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFrame mainFrame;
	private LobbyPanel lobbyPanel;
	private InGamePanel IngamePanel;
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
	private int roomId;
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
		roomlist = new JList<String>(roomModel);
		roomlist.setBounds(96, 52, 800, 244);
		add(roomlist);
		
		chatinfoLabel = new JLabel("��ü ä��");
		chatinfoLabel.setFont(new Font("����", Font.BOLD, 24));
		chatinfoLabel.setBounds(96, 306, 119, 33);
		add(chatinfoLabel);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(96, 336, 533, 183);
		add(scrollPane);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
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
		userlist.setBounds(651, 336, 245, 183);
		add(userlist);
		
		AppendText("User " + userName + " connecting " + ip_addr + " " + port_num + "\n");
		this.userName = userName;
		
		CreateBtn = new JButton("�� ����");
		CreateBtn.setFont(new Font("����", Font.BOLD, 18));
		CreateBtn.setBounds(650, 525, 97, 32);
		add(CreateBtn);
		CreateBtn.addActionListener(createAction);

        try {
            socket = new Socket(ip_addr, Integer.parseInt(port_num));
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

           
            Msg loginMsg = new Msg(userName,"100", "login");
            sendObject(loginMsg);
            updateRoomList();
            
            ListenNetwork net = new ListenNetwork();
            net.start();
            
               
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
	}
	
	// keyboard enter key�� �����ų� ���� ��ư Ŭ���ϸ� ������ ���� 
	class SendMsgBtnClick implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			 updateRoomList();
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == chatTextField) {
				String msg = null;
				msg = String.format("[%s] %s\n", userName, chatTextField.getText());
				//SendMessage(msg);
				chatTextField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
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
	public void createRoom(String roomName, Boolean isPass, String passWd, int roomType)
	{
		IngamePanel = new InGamePanel(container, lobbyPanel);
		container.add(IngamePanel,"IngamePanel");
		cardLayout.show(container, "IngamePanel");
		
		Msg msg = new Msg(userName,"200","�� ����");
		msg.setUserName(userName);
		msg.setRoomName(roomName);
		msg.setIsPass(isPass);
		msg.setPassWd(passWd);
		msg.setMode(roomType);
		sendObject(msg);
		System.out.println("�� �����߾�~");
		
		IngamePanel.p1_nameLabel.setText(userName);
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
	
	public void sendAllChatMessage(String message) {
		try {
			Msg msg = new Msg(userName,"400",message);
			msg.setRoomId(IngamePanel.roomId);
			oos.writeObject(msg);
		} catch (Exception e) { 
			System.out.println("sendChatMessage error");
		}
	}
	
	//server�� ���� �� ���������� ä�� �޽��� ���� �޼ҵ�
	public void sendChatMessage(String message) {
		try {
			Msg msg = new Msg(userName,"400",message);
			msg.setRoomId(IngamePanel.roomId);
			oos.writeObject(msg);
		} catch (Exception e) { 
			System.out.println("sendChatMessage error");
		}
	}
	
    // ȭ�鿡 ���
    public void AppendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }


    
    //Ŭ���̾�Ʈ�� roomList�� ������Ʈ �ϱ� ���� �޼ҵ�
    public void updateRoomList() {
		for(int i=0; i<ntRooms.size(); i++) {
			NTRoom room = ntRooms.get(i);
			String status = "";
			if(room.getStatus() == 0) status = "��� ��";
			else status = "���� ��";
			String str = String.format("%-16s%-5d%-9s", room.getRoomName(), room.getUserCount(), status);
			roomModel.set(i, str);
		}
	}
    
 // Server Message�� �����ؼ� ȭ�鿡 ǥ��
    class ListenNetwork extends Thread {
    	public ListenNetwork() {
    		Msg roomList = new Msg(userName, "210", "roomList");
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
                    /*	100 - �α���
                     *  200 - �� ���� ������ ��´�.
                     *  201 - ������ �濡 �����ߴٴ� ����.
                     *  202 - �濡 �ο��� ���� �������� ���ߴٴ� ����
                     *  210 - ������ ���� ��ü �� ����� ���۵� ���
                     *  211 - ������ ���� ��ü ������ ����� ���۵� ���
                     *  300 - ���� ����
                     *  301 - �ش� ī�带 �Դ´ٴ� ���� ����
                     *  302 - �ش� ī�� �Ա� �ź� ���� ����
                     *  320 - ��Ⱑ ������ ���ڰ� ���� ����
                     *  400 -
                     *  420 - ���� ���� 
                     *  430 - ����� �� ����
                     *  500 - �ش� �� ä��
                     *  510 - �ش� �� user ǥ��
                     */
                    
                    switch (msg.getCode())
                    {
                    case "200":
                    	int roomId = msg.getRoomId();
                    	String roomName = msg.getRoomName();
                    	int userCount = msg.getUserCount();
                    	boolean IsPass = msg.getIsPass();
                    	String gameMode = (msg.getMode() == 0) ? "normal" : "special"; 
                    	String status = (msg.getStatus() == 0) ? "��� ��" : "���� ��"; 
                    	NTRoom newRoom = new NTRoom(roomId);
                    	ntRooms.addElement(newRoom);
                    	
                    	String roomInfo = String.format("%-3s %-8s %-5d %-8s %-6s"
                    			,msg.getRoomId()
                    			,msg.getRoomName()
                    			,msg.getUserCount()
                    			,msg.getMode()
                    			,status);
                    	roomModel.addElement(roomInfo);
                    	break;
                    	
                    	
                    case "201":
                    	lobbyPanel.roomId = msg.getRoomId();
                    	for(int i=0; i<ntRooms.size();i++)
                    	{
                    		NTRoom room = ntRooms.get(i);
                    		if(room.getRoomId() == msg.getRoomId())
                    			room.users.add(lobbyPanel);
                    	}
                    	
                    	IngamePanel = new InGamePanel(container, lobbyPanel);
                    	IngamePanel.roomId = msg.getRoomId();
                    	container.add(IngamePanel,"gamePanel");
                    	IngamePanel.role = msg.getRole();
                    	
                    	if(msg.getRole() == msg.p1) {
                    		IngamePanel.p1_nameLabel.setText(msg.getUserName());
                    	}
                    	
                    	if(msg.getRole() == msg.p2) {
                    		IngamePanel.p2_nameLabel.setText(msg.getUserName());
                    	}
                    	
                    	if(msg.getRole() == msg.p3) {
                    		IngamePanel.p3_nameLabel.setText(msg.getUserName());
                        }  
                    	
                    	if(msg.getRole() == msg.p4) {
                    		IngamePanel.p4_nameLabel.setText(msg.getUserName());
                        }
                    	
                    	//mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    	cardLayout.show(container, "IngamePanel");
                    	IngamePanel.roomUserList();
                    	break;
                    	
                    	// ���� �濡 ���� �� �� ���� ���
					case "202":
						JOptionPane.showMessageDialog(mainFrame, msg.getData(), "error", JOptionPane.ERROR_MESSAGE);
						break;
						
					case "210" :
						int roomId2 = msg.getRoomId(); // �� ID
						String roomName2 = msg.getRoomName(); // �� �̸�
						int peopleCount2 = msg.getUserCount(); // �� �ο� ��
						
						NTRoom newRoom2 = new NTRoom(roomId2); // ���ο� �� �����
						newRoom2.setRoomName(roomName2);
						newRoom2.setUserCount(peopleCount2);
						newRoom2.setStatus(msg.getStatus());
						newRoom2.setIsPass(msg.getIsPass());
						ntRooms.add(newRoom2); // �� ����Ʈ�� �߰�
						
						String s = "";
						if(newRoom2.getStatus() == 0) s = "���� ��� ��";
						else s = "���� ��";
						String roomStr2 = String.format("%-3s %-8s %d/4 %-8s %-6s"
                    			,msg.getRoomId()
                    			,msg.getRoomName()
                    			,msg.getUserCount()
                    			,msg.getMode()
                    			,s);
						roomModel.addElement(roomStr2); 
						
						roomlist.setModel(roomModel);
						roomlist.repaint();
						break;
						
					case "211":
                    	String str = msg.getData();
                    	StringTokenizer tokenizer = new StringTokenizer(str);
                    	userModel.removeAllElements();
                    	while(tokenizer.hasMoreTokens()) {
                    		String user = tokenizer.nextToken();
                    		userModel.addElement(user);
                    	}
                    	break;					
						
					case "300":
						// �ڽ��� �÷��̾��� ���
						if(IngamePanel != null && msg.getRoomId() == IngamePanel.roomId) {
							String userNames = msg.getData();
							StringTokenizer st = new StringTokenizer(userNames);
							if(st.hasMoreTokens())							
								IngamePanel.p1_nameLabel.setText(st.nextToken());
							if(st.hasMoreTokens())
								IngamePanel.p2_nameLabel.setText(st.nextToken());
							if(st.hasMoreTokens())
								IngamePanel.p3_nameLabel.setText(st.nextToken());
							if(st.hasMoreTokens())
								IngamePanel.p4_nameLabel.setText(st.nextToken());
							
							if(IngamePanel.order == 1) { // ������ 1 �ΰ�� 
								IngamePanel.status = 1; // status 1�� �����Ͽ� ī�带 ���� �� �ִ� ���·� ����
							}
						}
						// �÷��̾ �ƴϸ� �� ����Ʈ ���� ������Ʈ�� ���ָ� ��
						else {
							for(int i=0; i<ntRooms.size(); i++) {
								if(ntRooms.get(i).getRoomId() == msg.getRoomId())
									ntRooms.get(i).setStatus(1);
							}
						}
						break;
                    	
                    case "400":
						IngamePanel.AppendChat(msg.getUserName(), msg.getData());
						break;
                                     	         
                    
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
  