package NT_Server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    NTRoom NTRoom; //사용중인 방의 정보를 접근/지정 하기 위한 NTRoom Class

    private ServerSocket socket;
    private Socket client_socket;
    private Vector<UserService> UserVec = new Vector<>(); //사용자들을 접근/지정 하기위한 Vector
    public Vector<NTRoom> RoomVector = new Vector<NTRoom>();  //방들의 정보를 저장하기위한 Vector
    
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
        /*
         * 서버 시작 버튼을 눌러야 서버 시작
         */
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
        public void run() {
            while (true) {
                try {
                    AppendText("Waiting clients ...");
                    client_socket = socket.accept();
                    AppendText("새로운 참가자 연결");
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user);                  
                    new_user.start();
                } catch (IOException e) {
                    AppendText("accept 에러 발생");
                }
            }
        }
    }
    /*
     * 서버에 상태창을 보여주기 위한 AppendText메소드
     */
    public void AppendText(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }
    /*
     * 서버로 들어온 Msg객체를 서버에 보여주기 위한 AppendObeject메소드
     */
    public void AppendObject(Msg msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getUserName() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

    //User에게 생성되는 스레드 클래스
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
        	UserVec.removeElement(this);
        	String data = "";
        	for(int i=0;i<user_vc.size();i++) {
        		UserService user = (UserService) user_vc.elementAt(i);
        		data += user.userName + " ";
        	}
        	WriteAll(new Msg("server", "UserList", data));
        	AppendText("["+userName +"] 님이 퇴장하셨습니다.\n 남은 참가자 수 " + UserVec.size());
        }

        public void roomListShow(String userName) {
        	// 이름을 전달받고 방번호, 이름, 인원수, 모드를 저장하고 클라이언트에 전달
			for (int i = 0; i < RoomVector.size(); i++) {
				Msg msg = new Msg(null,null,null);
				msg.setUserName(userName);
				msg.setRoomId(RoomVector.elementAt(i).getRoomId());
				msg.setRoomName(RoomVector.elementAt(i).getRoomName());
				msg.setUserCount(RoomVector.elementAt(i).getUserCount());
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
					/*
					 * 이하 if문에서 클라이언트로부터 받은 msg객체의 어떤 code가 있는지에 따라 다른 행동을 함
					 * 전달 받은 Code로 클라이언트가 어떤 행동을 했는지 파악하기 위해 사용
					 * 또한 Code형식을 사용하여 Code를 추가하여 추가적인 기능 구현이 용이해짐
					 */
					
					
					/*
					 * Login이라는 Code가 포함된 Msg객체를 클라이언트에게 받을 시 사용자 스레드를 생성하고 방의 목록을 전달
					 */
					if(msg.getCode().equals("Login")) { 
						userName = msg.getUserName();
						Login();
						roomListShow(userName);
					}
					
					// AllChat: 서버에 해당 내용을 쓰고 모든 클라이언트에게 전달
					else if(msg.getCode().equals("AllChat"))
					{
						AppendText("전채 채팅 :"+msg.getData());
						WriteAll(msg);
					}
					
					// RoomChat: 특정 방에 소속된 클라이언트에게 온 채팅을 확인하여 해당 방을 확인 후
					// 해당 방에 있는 모든 클라이언트에게 전달
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
					
					/*
					 * CreateRoomInfo: 사용자가 방을 생성 시 방의 정보가 포함된 msg객체를 읽어들인 후 방 객체에 저장,
					 * 후에 그 방들을 Vector에 저장하여 사용
					 * 또한 해당 방의 정보를 클라이언트에게 전달하여 방 목록을 갱신
					 */
					else if(msg.getCode().equals("CreateRoomInfo")) {		
						msg.setRoomId(roomNum++);
						str = String.format ("[%s]님이 %s번 %s", msg.getUserName(), msg.getRoomId() ,msg.getData());
						AppendText(str);
						str = String.format ("방 번호 : %d, 방 제목: %s 방이 만들어졌습니다.", msg.getRoomId(), msg.getRoomName());
						AppendText(str);
						NTRoom ntRoom = new NTRoom(msg.getRoomId(),
												   msg.getRoomName(),
												   (msg.getMode() == 0) ?"normal" : "special",
												   msg.getUserName()
												   );
						ntRoom.setRoomName(msg.getRoomName());
						ntRoom.setRoomId(msg.getRoomId());
						ntRoom.setUserCount();
						msg.setUserCount(ntRoom.getUserCount());
												
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
					/*
					 * RoomRefresh: 클라이언트들에게 방의 정보를 갱신하여 전달함
					 */
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
					/*
					 * 	EnterRoom: 클라이언트가 방의 목록을 Double-Click시 해당 Msg가 전달됨
					 *  이미 방에 들어가 있는 클라이언트의 갯수의 따라 방의 정보를 수정하여
					 *  해당 방에 들어가있는 모든 클라이언트에게 정보를 전달 -> 접속 시 마다 정보 갱신
					 *  또한 Lobby에 있는 사용자 한테도 방의 정보를 갱신하여 전달
					 *  방에 4명이 다 들어가면 자동 게임 시작 등의 기능 실행 정보 전달
					 */
					else if(msg.getCode().matches("EnterRoom")) { 
						NTRoom findRoom = null;
						
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								findRoom = ntRoom; 
								break;
							}
							
							else break;
						}
											
						if(findRoom == null ) {
							WriteOne(new Msg("SERVER","RoomFull","틀린 비밀번호"));
							continue;
						}
						//방에 들어가있는 인원수에 따라 다르게 작동
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
						/*
						 * 방에 인원이 들어가면 Lobby에 있는 클라이언트들에게도 해당 정보를 전달하는 부분
						 */
						String info = String.format("[%s]님이 %s번 방에 접속하셨습니다.", msg.getUserName(), roomId);
						AppendText(info);
						Msg roomListMsg = new Msg("server", "RoomList", "방 목록 정보 변경");
						roomListMsg.setRoomId(findRoom.getRoomId());
						roomListMsg.setMode(findRoom.getMode());
						roomListMsg.setRoomName(findRoom.getRoomName());
						roomListMsg.setUserCount(findRoom.getUserCount());
						roomListMsg.setStatus(findRoom.getStatus());
						WriteAll(roomListMsg);
						AppendText("방 인원수가 변경되어 상태를 update 합니다");

						/*
						 * 인원이 4명이 되면 자동으로 시작하고 방의 상태를 변경
						 */
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
					/*
					 * GameStart: 게임 시작 시 시작 멘트를 게임 방 속 클라이언트들에게 전달하고 게임을 활성화 하기 위한 정보 전달
					 */
					else if (msg.getCode().equals("GameStart")) { 
						String data = "게임을 시작합니다 1번 player부터 시작합니다";
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								break;
							}
						}
																
						for(int i=0; i<user_vc.size(); i++) {
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.roomId == msg.getRoomId())
								u.WriteOne(new Msg("SERVER", "GameStart", data));
						}
					}
					/*
					 * CardOpen: 사용자가 게임을 진행하여 CardOpen시 다음 기능을 수행
					 * 어떤 사용자가 카드를 열었는 지 클라이언트들에게 알려주고
					 * 랜덤하게 지정되어 있는 숫자를 클라이언트들에게 전달 및 남은 카드의 숫자 갱신 하여 정보전달
					 */
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
					/*
					 * Eat: 사용자가 '먹는다' 버튼 클릭 시 방의 번호를 찾고 저장한 다음,
					 * 남은 카드의 수가 0이 되면 게임 종료 정보 전달 
					 * 그렇지 않다면 어떤 사용자가 카드를 먹었는지 클라이언트들에게 전달
					 */
					else if (msg.getCode().equals("Eat")) { 
						NTRoom gameRoom2 = null ;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								gameRoom2 = ntRoom; 
								break;
							}
						}

						if(gameRoom2.total == 0) 
						{
							String EatStr = "[" + msg.getUserName() + "] 님이 카드를 먹었습니다.\n" + "카드가 모두 소진되어 게임이 종료 되었습니다.";
							AppendText(EatStr);
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
					/*
					 * NoEat: 사용자가 '먹지 않는다' 버튼을 클릭시 아래의 기능 수행
					 * Eat과 동일하게 방의 정보를 기억한 후 해당 방에 있는 클라이언트들에게 정보 전달
					 */
					else if (msg.getCode().equals("NoEat")) { 
						String noEatStr = "[" + msg.getUserName() + "] 님이 토큰을 지불하고 턴을 넘겼습니다.";
						AppendText(noEatStr);
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
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
					/*
					 * Winner: 진행중인 방의 카드 수가 0이 되면 서버에서 승자를 판별한 후 해당 방의 클라이언트들에게
					 * 승리자의 정보를 전달
					 */
					else if (msg.getCode().equals("Winner")) { 
						String EndStr = "승자판별중 .... ";
						AppendText(EndStr);
						NTRoom gameRoom2 = null ;
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom ntRoom = (NTRoom) RoomVector.elementAt(i);
							if(msg.getRoomId() == ntRoom.getRoomId()) { 
								gameRoom2 = ntRoom; 
								break;
							}
						}				
						Pair pair = new Pair(msg.p_cards, msg.getUserName(), msg.getToken());
						gameRoom2.cardList.add(pair);
						Pair pair2 = gameRoom2.find_winner();
						Msg WinnerMsg = new Msg("server", "Winner" , " ");
						WinnerMsg.setData("*"+ pair2.userName + "* 님이 토큰 " 
											+ pair2.token + "개로" 
											+ pair2.total 
											+ "점을 획득하여 우승했습니다.");
											
						for(int i=0; i<user_vc.size(); i++) {				
							UserService u = (NTServer.UserService) user_vc.get(i);
							if(u.userName == msg.getUserName())
							{	
								u.WriteOne(WinnerMsg);
							}
						}
						
						for(int i=0; i<RoomVector.size(); i++) {
							NTRoom o = RoomVector.get(i);
							if(o.getRoomId() == this.roomId) {
								RoomVector.removeElementAt(i);
								break;
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
            }
        }
    }
}
