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
                AppendText("NoThanks Server Running..");
                btnServerStart.setText("NoThanks Server Running..");
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
                    AppendText("새로운 참가자 연결");
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user);
                    //AppendText("사용자 입장. 현재 참가자 수 " + UserVec.size());
                    new_user.start();
                } catch (IOException e) {
                    AppendText("accept 에러 발생");
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
                AppendText("userService 에러 발생");
            }
        }
        
        public void Login() {
        	String data = "";
        	AppendText("새로운 참가자 [" + userName + "]님이 입장하셨습니다.");
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
        	AppendText("["+userName +"] 님이 퇴장하셨습니다.\n 남은 참가자 수 " + UserVec.size());
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
                AppendText("oos.writeOne() 에러 발생");
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
					} 
					else
						continue;
					
					if(msg.getCode().equals("Login")) { 
						userName = msg.getUserName();
						Login();
						roomListShow(userName);
					}
					
					else if(msg.getCode().equals("AllChat"))
					{
						AppendText("전채 채팅 :"+msg.getData());
						WriteAll(msg);
					}
					
					else if(msg.getCode().equals("RoomChat"))
					{
						String sender = String.format("[%s] %s", msg.getUserName(), msg.getData());
						AppendText(msg.getRoomId()+ "번 방 채팅 : " + sender);
						for(int i=0; i<user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
                            if (user != this && msg.getRoomId() == user.roomId) {
                                Msg obj = new Msg(userName, "RoomChat", msg.getData());
                                obj.setRoomId(msg.getRoomId());
                                user.WriteOne(obj);
                            }
						}
						
					}
					
					else if(msg.getCode().equals("CreateRoomInfo")) {		
						msg.setRoomId(roomNum++);
						str = String.format ("[%s]님이 %s번 %s", msg.getUserName(), msg.getRoomId() ,msg.getData());
						AppendText(str);
						str = String.format ("방 번호 : %d, 방 제목: %s 방이 만들어졌습니다.", msg.getRoomId(), msg.getRoomName());
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
						
						if(msg.getIsPass() == true) {
							ntRoom.setIsPass(true);
							ntRoom.setPassWd(msg.getPassWd());
							msg.setIsPass(true);
						}
						
						RoomVector.add(ntRoom);
						
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							if(user != this)
								user.WriteOne(msg);
						}
						Msg msg1 = new Msg(msg.getUserName(), "EnterRoom", msg.getUserName());
						this.role = msg1.p1;
						msg1.setRole(this.role);
						msg1.setRoomId(msg.getRoomId());
						this.roomId = msg.getRoomId();
						WriteOne(msg1);
											
					}
					
					else if (msg.getCode().equals("RoomRefresh")) { 
						NTRoom findRoom = null;
						String data = "";
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								findRoom = ntRoom; 
								break;
							}
						}
						
						for(int j=0; j<findRoom.users.size(); j++) {
							data += findRoom.users.elementAt(j) + " ";
						}						
						
						for(int i=0; i<user_vc.size(); i++) {
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.roomId == msg.getRoomId())
								u.WriteOne(new Msg("SERVER", "RoomRefresh", data));
						}
					}
						
					else if(msg.getCode().matches("EnterRoom")) { 
						NTRoom findRoom = null;
						
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								if(ntRoom.getIsPass() && msg.getPassWd().equals(ntRoom.getPassWd())) {
									findRoom = ntRoom;
									break;
								}
								else if(!ntRoom.getIsPass()) {
									findRoom = ntRoom; 
									break;
								}
								else break;
							}
						}
						
						if(findRoom == null ) {
							WriteOne(new Msg("SERVER","RoomFull","틀린 비밀번호"));
							continue;
						}
						
						 if(findRoom.getUserCount() == 1) {
							String data = findRoom.users.get(0) + " " + msg.getUserName();
							Msg obj = new Msg(userName,"EnterRoom", data);
							obj.setRole(obj.p2);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p2;
							this.roomId = msg.getRoomId();					
							
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(0));
							WriteOne(obj);
							
							findRoom.users.add(userName); 
						}
						 else if(findRoom.getUserCount() == 2) {

							String data = findRoom.users.get(0) + " " +findRoom.users.get(1) +" " + msg.getUserName();	
							Msg obj = new Msg(userName,"EnterRoom", data);
							obj.setRole(obj.p3);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p3;
							this.roomId = msg.getRoomId();
							
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(0));
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(1));
							WriteOne(obj);
							
							findRoom.users.add(userName); 
						}
						else if(findRoom.getUserCount() == 3) {
							String data = findRoom.users.get(0) + " " +findRoom.users.get(1) +" "
									+findRoom.users.get(2) +" "+ msg.getUserName();
							Msg obj = new Msg(userName, "EnterRoom", data);
							obj.setRole(obj.p4);
							obj.setRoomId(msg.getRoomId());
							this.role = obj.p4;
							this.roomId = msg.getRoomId();	
							
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(0));
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(1));
							WriteOne(obj);
							obj.setUserName(findRoom.users.get(2));
							WriteOne(obj);
							
							
							findRoom.users.add(userName); 
						}
						 
						else {
							Msg obj = new Msg(userName ,"RoomFull", "방이 꽉찼습니다");
							WriteOne(obj);
							break;
						}
						 
						String info = String.format("[%s]님이 %s번 방에 접속하셨습니다.", msg.getUserName(), roomId);
						AppendText(info);
						Msg roomListMsg = new Msg("server", "RoomList", "방 목록 정보 변경");
						System.out.println(findRoom.getRoomName()+ findRoom.getRoomId() + findRoom.getMode() + findRoom.getUserCount());
						roomListMsg.setRoomId(findRoom.getRoomId());
						roomListMsg.setMode(findRoom.getMode());
						roomListMsg.setRoomName(findRoom.getRoomName());
						roomListMsg.setUserCount(findRoom.getUserCount());
						roomListMsg.setStatus(findRoom.getStatus());
						WriteAll(roomListMsg);
						AppendText("방 인원수가 변경되어 상태를 update 합니다");

						 
						if(findRoom.getUserCount() == 4){ 
							String startMsg = "GameStartMsg";
							Msg obj = new Msg("server", startMsg, "player1 부터 게임을 시작합니다");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if(findRoom.getRoomId() == user.roomId) {
									user.WriteOne(obj);
								}
							}
							findRoom.setStatus(1);
							roomListMsg.setRoomName(findRoom.getRoomName());
							roomListMsg.setUserCount(findRoom.getUserCount());
							roomListMsg.setRoomId(findRoom.getRoomId());
							roomListMsg.setMode(findRoom.getMode());
							roomListMsg.setStatus(findRoom.getStatus());
							WriteAll(roomListMsg);
							AppendText(String.format("%d 번방의 상태가 변경되어 상태를 update 합니다",findRoom.getRoomId()));
							AppendText(roomId + "번 방 게임 시작!!");
						}
						 			
						 					 
					}
					
					else if (msg.getCode().equals("GameStart")) { 
						NTRoom gameRoom = null;
						String data = "게임을 시작합니다 1번 player부터 시작합니다";
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								gameRoom = ntRoom; 
								break;
							}
						}
																
						for(int i=0; i<user_vc.size(); i++) {
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.roomId == msg.getRoomId())
								u.WriteOne(new Msg("SERVER", "GameStart", data));
						}
					}
					
					else if (msg.getCode().equals("CardOpen")) { 
						NTRoom gameRoom2 = null ;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								gameRoom2 = ntRoom; 
								break;
							}
						}
						gameRoom2.setIndex();				
						gameRoom2.total--;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								RoomVector.elementAt(i).total = gameRoom2.total;
								System.out.println(RoomVector.elementAt(i).total + "R g"+ gameRoom2.total);
								break;
							}
						}
						String openStr = "[" + msg.getUserName() + "] 님이 카드를 열었습니다.";
						AppendText(openStr);
						Msg openMsg = new Msg("server", "CheckCard",openStr);
						openMsg.setRole(msg.getRole());
						openMsg.setToken(gameRoom2.total);
						openMsg.setCard(gameRoom2.getRandCard());
						for(int i=0; i<user_vc.size(); i++) {
							
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.roomId == msg.getRoomId())
							{
								u.WriteOne(openMsg);
							}
						}						
					}
					
					else if (msg.getCode().equals("Eat")) { //카드 오픈 버튼을 누르면
						NTRoom gameRoom2 = null ;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { // 클라이언트가 보낸 roomId를 비교해 해당 방을 찾는다
								gameRoom2 = ntRoom; // 찾은 방 저장
								break;
							}
						}
						System.out.println(gameRoom2.total);

						if(gameRoom2.total == 30) //0으로 수정
						{
							String EatStr = "[" + msg.getUserName() + "] 님이 카드를 먹었습니다.\n" + "카드가 모두 소진되어 게임이 종료 되었습니다.";
							AppendText(EatStr);
							AppendText("카드가 모두 소진되어 게임이 종료 되었습니다.");
							Msg Endmsg = new Msg("server", "End", EatStr);
							Endmsg.setRole(msg.getRole());
							for(int i=0; i<user_vc.size(); i++) {
								UserService u = (NTServer.UserService) user_vc.get(i);
								if(u.roomId == msg.getRoomId())
								{
									u.WriteOne(Endmsg);
								}
							}
						}
						else {
							String EatStr = "[" + msg.getUserName() + "] 님이 카드를 먹었습니다.";
							AppendText(EatStr);
							Msg Eatmsg = new Msg("server","Eat",EatStr);
							Eatmsg.setCard(msg.getCard());
							Eatmsg.setRole(msg.getRole());
							
							for(int i=0; i<user_vc.size(); i++) {
								UserService u = (NTServer.UserService) user_vc.get(i);
								if(u.roomId == msg.getRoomId())
								{
									u.WriteOne(Eatmsg);
								}
							}							
						}
					}
					
					else if (msg.getCode().equals("NoEat")) { 
						String noEatStr = "[" + msg.getUserName() + "] 님이 토큰을 지불하고 턴을 넘겼습니다.";
						AppendText(noEatStr);
						NTRoom gameRoom2 = null ;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								gameRoom2 = ntRoom; 
								break;
							}
						}				
						
						Msg noEatMsg = new Msg("server","NoEat" ,noEatStr);
						noEatMsg.setRole(msg.getRole());
						noEatMsg.setToken(msg.getToken());
						
						for(int i=0; i<user_vc.size(); i++) {
											
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.roomId == msg.getRoomId())
							{									
								u.WriteOne(noEatMsg);
							}
						}				
					}
																			
																										
                } catch (IOException e) {
                    AppendText("ois.readObject() 에러 발생");
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
