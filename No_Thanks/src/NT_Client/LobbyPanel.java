package NT_Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LobbyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JFrame mainFrame;
	private LobbyPanel lobbypanel;
	private InGamePanel IngamePanel;
	private Container container;
	private CardLayout cardLayout;
	private JLabel waitingroominfoLabel;
	private JList<String> userlist;
	private JList<String> roomlist;
	private JLabel chatinfoLabel;
	private JScrollPane scrollPane;
	private TextArea textArea;
	private JLabel userinfoLabel;
	private TextField chatTextField;
	private JButton sendBtn;
	private JButton CreateBtn;
	private static final int BUF_LEN = 128;
	private String userName;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
    CreateRoomFrame createRoomFrame;
    private DefaultListModel<String> roomListModel;
	/**
	 * Create the panel.
	 */
	public LobbyPanel(Container container, JFrame mainFrame,String userName, String ip_addr, String port_num) {
		lobbypanel = this;
		this.mainFrame = mainFrame;
		this.container = container;
		this.cardLayout = (CardLayout) container.getLayout();
		
		setBackground(new Color(240, 240, 240));
		setLayout(null);
		
		this.userName = userName;
		waitingroominfoLabel = new JLabel("방번호    방제목              인원수     게임 모드     방 상태");
		waitingroominfoLabel.setBackground(new Color(240, 240, 240));
		waitingroominfoLabel.setFont(new Font("굴림", Font.BOLD, 29));
		waitingroominfoLabel.setBounds(96, 10, 800, 45);
		add(waitingroominfoLabel);
		
		roomListModel = new DefaultListModel<>();
        
		roomlist = new JList<String>();
		roomlist.setModel(roomListModel);
		roomlist.setBounds(96, 52, 800, 244);
		add(roomlist);
		
		chatinfoLabel = new JLabel("전체 채팅");
		chatinfoLabel.setFont(new Font("굴림", Font.BOLD, 24));
		chatinfoLabel.setBounds(96, 306, 119, 33);
		add(chatinfoLabel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(96, 336, 533, 183);
		add(scrollPane);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		userinfoLabel = new JLabel("유저 목록");
		userinfoLabel.setFont(new Font("굴림", Font.BOLD, 24));
		userinfoLabel.setBounds(641, 306, 119, 33);
		add(userinfoLabel);
		
		chatTextField = new TextField();
		chatTextField.setBounds(96, 525, 456, 31);
		add(chatTextField);
		
		sendBtn = new JButton("전송");
		sendBtn.setFont(new Font("굴림", Font.BOLD, 18));
		sendBtn.setBounds(558, 529, 71, 31);
		add(sendBtn);
		
		userlist = new JList<String>();
		userlist.setBounds(651, 336, 245, 183);
		add(userlist);
		
		AppendText("User " + userName + " connecting " + ip_addr + " " + port_num + "\n");
		this.userName = userName;
		
		CreateBtn = new JButton("방 생성");
		CreateBtn.setFont(new Font("굴림", Font.BOLD, 18));
		CreateBtn.setBounds(650, 525, 97, 32);
		add(CreateBtn);

        try {
            socket = new Socket(ip_addr, Integer.parseInt(port_num));
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            SendMessage("/login " + userName);
            ListenNetwork net = new ListenNetwork();
            net.start();
            Myaction action = new Myaction();
            sendBtn.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
            
            CreateRoom create = new CreateRoom();
            CreateBtn.addActionListener(create); // 방 생성 액션 리스너를 방 생성 버튼에 등록
            
            chatTextField.addActionListener(action);
            chatTextField.requestFocus();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            AppendText("connect error");
        }
		

	}
	 // Server Message를 수신해서 화면에 표시
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    // Use readUTF to read messages
                	// 받은 메세지가 무엇으로 시작하는 지에 따라 다른 행동 수행
                    String msg = dis.readUTF();
                    if (msg.startsWith("/userlist ")) {
                        String[] userList = msg.split(" ");
                        String[] users = new String[userList.length - 1];
                        System.arraycopy(userList, 1, users, 0, userList.length - 1);
                        userlist.setListData(users);
                        
                    } else if (msg.startsWith("/roomlist ")) {           
                    	// "/roomlist" 다음에 오는 문자열에서 방 정보 추출
                        String roomListInfo = msg.substring("/roomlist ".length());
                        String[] roomInfos = roomListInfo.split(" ");

                        // 방 정보를 LobbyPanel에 업데이트
                        for (int i = 0; i < roomInfos.length; i += 5) {
                        	
                        	if (i + 4 >= roomInfos.length) {
                                System.err.println("Not enough elements in roomInfos array.");
                                break;
                            }
                            int roomNum = Integer.parseInt(roomInfos[i].trim());
                            String roomName = roomInfos[i + 1];
                            int userCount = Integer.parseInt(roomInfos[i + 2].trim());
                            String status = roomInfos[i + 3].trim();
                            boolean isPass = Boolean.parseBoolean(roomInfos[i + 4]);

                            // LobbyPanel 업데이트
                            lobbypanel.updateRoomList(roomNum, roomName, userCount, status, isPass);
                        }
                        System.out.println("Received /roomlist message: " + msg);
                    } else {
                        AppendText(msg);
                    }
                } catch (IOException e) {
                    AppendText("dis.read() error. 서버가 닫혔습니다.");
                    try {
                        dos.close();
                        dis.close();
                        socket.close();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }

	// keyboard enter key 치면 서버로 전송
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == sendBtn || e.getSource() == chatTextField) {
				String msg = null;
				msg = String.format("[%s] %s\n", userName, chatTextField.getText());
				SendMessage(msg);
				chatTextField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				chatTextField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}
	// 방 생성 버튼 누르면 생성 창 뜨게함
	class CreateRoom implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			createRoomFrame = new CreateRoomFrame();
			
			// 방 생성 정보를 전송
			//createRoomFrame.addCreateRoomListener((roomName, isPass, roomType) ->
		    //SendMessage("/roomlist " + "001 " + roomName + " " + "0 "+ roomType + " " + isPass));
		}
		
	}
    // 화면에 출력
    public void AppendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }


    // Server에게 network으로 전송
    public void SendMessage(String msg) {
        try {
            // Use writeUTF to send messages
            dos.writeUTF(msg);
        } catch (IOException e) {
            AppendText("dos.write() error");
            try {
                dos.close();
                dis.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }
 // 클라이언트의 roomlist를 업데이트하기 위한 메서드
    private void updateRoomList(int roomNum, String roomName, int userCount, String status, boolean isPass) {
        // 방 정보를 나타내는 문자열 생성
        String roomInfo = String.format("%-50d\t%-70s\t%-50d\t%-50s\t%-30s", roomNum, roomName, userCount, status, (isPass ? "비공개" : "공개"));
        // roomListModel에 방 정보 추가
        roomListModel.addElement(roomInfo);
    }
    
  
      
    public void createRoom(String roomName, String password, int peopleCount) {
		createRoomFrame.dispose(); // 프레임 닫기
		
		IngamePanel = new InGamePanel(container, lobbypanel); 
		container.add(IngamePanel, "IngamePanel");
		cardLayout.show(container, "IngamePanel");
		
		RequestMsg RequestMsg = new RequestMsg(userName, "200", "방 만들기");
		sendObject(RequestMsg);
	}
    
    
    public void sendObject(Object obj) {
 		try {
 			if(obj instanceof RequestMsg) {
 				oos.writeObject(obj);
 			}
 		} catch (Exception e) {
 			System.out.println("client to server RequestMsg  error");
 		}
 	}
}