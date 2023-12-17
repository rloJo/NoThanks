package NT_Server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import common.Msg;

public class NTServer extends JFrame {

    private static final long serialVersionUID = 1L;
    private static int roomNum = 1;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    private List<String> connectedUsers;
    private ArrayList<NTRoom> roomList = new ArrayList<>();
    NTRoom NTRoom;

    private ServerSocket socket;
    private Socket client_socket;
    private Vector<UserService> UserVec = new Vector<>(); //연결된 사용자 저장 벡터
    public Vector<NTRoom> RoomVector = new Vector<NTRoom>(); 
    
    private static final int BUF_LEN = 128;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NTServer frame = new NTServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public NTServer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 386);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 244);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(12, 264, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText("30000");
        txtPortNumber.setBounds(111, 264, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
                AppendText("Chat Server Running..");
                btnServerStart.setText("Chat Server Running..");
                btnServerStart.setEnabled(false);
                txtPortNumber.setEnabled(false);
                AcceptServer accept_server = new AcceptServer();
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 300, 300, 35);
        contentPane.add(btnServerStart);
    }

    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) {
                try {
                    AppendText("Waiting clients ...");
                    client_socket = socket.accept();
                    AppendText("새로운 참가자 from " + client_socket);
                    
                    //user 당 하나씩 스레드 생성
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user); // 새 참가자 배열에 추가
                    AppendText("사용자 입장. 현재 참가자 수 " + UserVec.size());
                    new_user.start();
                } catch (IOException e) {
                    AppendText("!!!! accept 에러 발생... !!!!");
                }
            }
        }
    }

    public void AppendText(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }
    
    public void AppendObject(Msg msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getUserName() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

    //User에게 생성되는 스레드
    class UserService extends Thread {
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Socket client_socket;
        private Vector user_vc;
        public String userName = "";
        public int roomId = -1;
        public int role = 0;
        
       
        public UserService(Socket client_socket) {
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());      
            } catch (Exception e) {
                AppendText("userService error");
            }
        }
        
        public void Login() {
        	String data = "";
        	AppendText("새로운 참가자" + userName + "입장");
        	for(int i=0;i<user_vc.size();i++) {
        		UserService user = (UserService) user_vc.elementAt(i);
        		data+=user.userName + " ";
        	}
        	WriteAll(new Msg("server","UserList",data));
        }
        
        public void Logout() {
        	int logoutUserRoomId = this.roomId;
        	UserVec.removeElement(this);
        	String data = "";
        	for(int i=0;i<user_vc.size();i++) {
        		UserService user = (UserService) user_vc.elementAt(i);
        		data += user.userName + " ";
        	}
        	WriteAll(new Msg("server", "logout", data));
        	AppendText("사용자 퇴장. 남은 참가자 수 " + UserVec.size());
        }

        public void roomListShow(String userName) {
        	// 방번호 . 이름, 인원수 모드 비밀번호 유무, 비밀번호
			for (int i = 0; i < RoomVector.size(); i++) {
				Msg msg = new Msg(null,null,null);
				msg.setUserName(userName);
				msg.setRoomId(RoomVector.elementAt(i).getRoomId());
				msg.setRoomName(RoomVector.elementAt(i).getRoomName());
				msg.setUserCount(RoomVector.elementAt(i).getUserCount());
				msg.setIsPass(RoomVector.elementAt(i).getIsPass());
				msg.setPassWd(RoomVector.elementAt(i).getPassWd());
				msg.setMode(RoomVector.elementAt(i).getMode());
				msg.setCode("CreateRoomInfo");
				WriteOne(msg);
			} 
        }
        
        
        
        
        
        public void WriteOne(Object ob) {
            try {
                oos.writeObject(ob);
            } catch (IOException e) {
                AppendText("oos.writeOne() error");
                try {
                    oos.close();
                    ois.close();
                    client_socket.close();
                    UserVec.removeElement(this);
                    Logout();
                } catch (Exception ee) {
                }
            }
        }

        public void WriteAll(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                user.WriteOne(ob);
            }
        }
              

        public void run() {
            while (true) {
                try {      
                	Object ob = null;
                	String str = null;
                	Msg msg = null;
                	if(socket == null)
                		break;
                	try {       
                		ob = ois.readObject();
                		msg = (Msg) ob;          
                	} 
                	catch(ClassNotFoundException e) {
                		System.out.println("ois read 오류");
                		return;
                	}
                	
                	if (ob == null)
						break;
					if (ob instanceof Msg) {
						msg = (Msg) ob;
			
						AppendObject(msg);
					} 
					else
						continue;
					
					if(msg.getCode().equals("Login")) { //login
						userName = msg.getUserName();
						Login();
						roomListShow(userName);
					}
					
					else if(msg.getCode().equals("AllChat"))
					{
						AppendText(msg.getData());
						WriteAll(msg);
					}
					
					else if(msg.getCode().equals("CreateRoomInfo")) {
						str = String.format ("[%s]님이 [%s]방을 만들었습니다.", msg.getUserName(), msg.getData());
						AppendText(str);
						msg.setRoomId(roomNum++);
						str = String.format ("방번호 : %d 방 제목: %s 방이 만들어졌습니다.", msg.getRoomId(), msg.getRoomName());
						AppendText(str);
						NTRoom ntRoom = new NTRoom(msg.getRoomId(),
												   msg.getRoomName(),
												   "normal",
												   msg.getUserName(),
												   msg.getIsPass());
						ntRoom.setRoomName(msg.getRoomName());
						ntRoom.setRoomId(msg.getRoomId());
						ntRoom.setUserCount();
						msg.setUserCount(ntRoom.getUserCount());
						// 만들어진 방에 비밀번호가 설정되어 있는지
						if(msg.getIsPass() == true) {
							ntRoom.setIsPass(true);
							ntRoom.setPassWd(msg.getPassWd());
							msg.setIsPass(true);
						}
						
						RoomVector.add(ntRoom);
						
						// 방 만들었다는 정보를 전체 유저에게 보내준다. 
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							if(user != this)
								user.WriteOne(msg);
						}
						Msg msg1 = new Msg("server", "EnterRoom", msg.getUserName());
						this.role = msg1.p1;
						msg1.setRole(this.role);
						msg1.setRoomId(msg.getRoomId());
						this.roomId = msg.getRoomId();
						WriteOne(msg1);
						
						Msg msg2 = new Msg("server", "400", "다른 참가자가 들어올 때 까지 잠시만 기다려 주세요...");
						msg2.setRoomId(msg.getRoomId());
						WriteOne(msg2);						
					}
						
					else if(msg.getCode().matches("EnterRoom")) { 
						NTRoom findRoom = null;
						
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { // 클라이언트가 보낸 roomId를 비교해 해당 방을 찾는다
								if(ntRoom.getIsPass() && msg.getPassWd().equals(ntRoom.getPassWd())) {
									findRoom = ntRoom;
									break;
								}
								else if(!ntRoom.getIsPass()) {
									System.out.println("방 찾음");
									findRoom = ntRoom; // 찾은 방 저장
									break;
								}
								else break;
							}
						}
						if(findRoom == null ) {
							WriteOne(new Msg("SERVER","RoomFull","틀린 비밀번호"));
							continue;
						}
						System.out.println("유저카운트" + findRoom.getUserCount());
						 if(findRoom.getUserCount() == 1) {
							//해당 방 유저 목록 띄우기 
							String data = findRoom.users.get(0) + " " + msg.getUserName();
							System.out.println("usercount == 1:" + data);
							Msg obj = new Msg(userName,"EnterRoom", data);
							obj.setRole(obj.p2);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p2;
							this.roomId = msg.getRoomId();
							System.out.println("방번호:" + msg.getRoomId());
							WriteOne(obj);
							findRoom.users.add(userName); // player 리스트에 추가
						}
						
						else if(findRoom.getUserCount() == 2) {
							//해당 방 유저 목록 띄우기 
							String data = findRoom.users.get(0) + " " +findRoom.users.get(1) +" " + msg.getUserName();	
							System.out.println("usercount == 2:" + data);
							Msg obj = new Msg(userName,"EnterRoom", data);
							obj.setRole(obj.p3);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p3;
							this.roomId = msg.getRoomId();
							System.out.println("방번호:" + msg.getRoomId());
							WriteOne(obj);
							findRoom.users.add(userName); // player 리스트에 추가
						}
						else if(findRoom.getUserCount() == 3) {
							String data = findRoom.users.get(0) + " " +findRoom.users.get(1) +" "
									+findRoom.users.get(2) +" "+ msg.getUserName();
							Msg obj = new Msg(userName, "EnterRoom", data);
							obj.setRole(obj.p4);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p4;
							this.roomId = msg.getRoomId();
							//System.out.println("role : " + this.role);
							WriteOne(obj);
							findRoom.users.add(userName); // player 리스트에 추가
						}
						
						/*else if(findRoom.getUserCount() == 4) {
							String data = findRoom.users.get(0) + " " + msg.getUserName();
							Msg obj = new Msg(userName, "EnterRoom", data);
							obj.setRole(obj.p4);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p4;
							this.roomId = msg.getRoomId();
							//System.out.println("role : " + this.role);
							WriteOne(obj);
							findRoom.users.add(userName); // player 리스트에 추가
						}*/
						
						/*if(findRoom.users.size() == 4) { //게임 시작
							Msg obj = new Msg("server", "400", "게임 시작!!"); //게임 시작 메시지를 방에 있는 모든 object에게 뿌림
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if(findRoom.getRoomId() == user.roomId) {
									user.WriteOne(obj);
								}
							}
							
							findRoom.setStatus(1);
							String data = findRoom.users.elementAt(0) + " " + findRoom.users.elementAt(1);
							obj = new Msg("server", "300", data);
							obj.setRoomId(findRoom.getRoomId());
							obj.setStatus(findRoom.getStatus());
							WriteAll(obj);
							
							AppendText("[" + roomId + "]방 게임 시작!!");
							AppendText("현재 [" + roomId + "]방에 있는 플레이어 수 : " + findRoom.users.size());
						}*/
					}
					
					String info = String.format("[%s]님이 [%s]방에 접속하셨습니다.", msg.getUserName(), msg.getData());
					AppendText(info);
					
					
                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
                        oos.close();
                        ois.close();
                        client_socket.close();
                        Logout();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            } //w
        } // r
    }
}
