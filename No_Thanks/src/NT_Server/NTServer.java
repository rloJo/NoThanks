package NT_Server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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

public class NTServer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    private List<String> connectedUsers;
    private ArrayList<NTRoom> roomList = new ArrayList<>();
    NTRoom NTRoom;

    private ServerSocket socket;
    private Socket client_socket;
    private Vector<UserService> UserVec = new Vector<>();
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

    private void handleCreateRoomCommand(String[] commandParts, DataOutputStream dos) {
    	String roomName = Base64.getEncoder().encodeToString(commandParts[1].getBytes(StandardCharsets.UTF_8));
        boolean isPass = Boolean.parseBoolean(commandParts[2]);
        String roomType = Base64.getEncoder().encodeToString(commandParts[3].getBytes(StandardCharsets.UTF_8));

        NTRoom newRoom = new NTRoom(roomName, roomType, isPass);
   
        roomList.add(newRoom);

        // 방 목록을 모든 클라이언트에게 전송
        updateRoomListToAllClients();
    }

    private void updateRoomListToAllClients() {
        StringBuilder roomListMessage = new StringBuilder("/roomlist ");
        for (NTRoom room : roomList) {
        	roomListMessage.append(Integer.toString(room.getRoomNum())).append(" ")
				            .append(room.getRoomName()).append(" ")
				            .append(Integer.toString(room.getUserCount())).append(" ")
				            .append(Integer.toString(room.getStatus())).append(" ")
				            .append(room.getIsPass()).append(" ");
        }
        broadcastMessage(roomListMessage.toString());
    }

    private void broadcastMessage(String msg) {
        for (UserService user : UserVec) {
            user.WriteOne(msg);
        }
    }

    private void updateConnectedUsers() {
        StringBuilder userListMessage = new StringBuilder("/userlist ");
        for (String user : connectedUsers) {
            userListMessage.append(user).append(" ");
        }
        for (UserService user : UserVec) {
            user.WriteOne(userListMessage.toString());
        }
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

        connectedUsers = new ArrayList<>();

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
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user);
                    connectedUsers.add(new_user.getUserName());
                    updateConnectedUsers();
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

    class UserService extends Thread {
        private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client_socket;
        private Vector<UserService> user_vc;
        private String UserName = "";

        public String getUserName() {
            return UserName;
        }

        public UserService(Socket client_socket) {
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
                is = client_socket.getInputStream();
                dis = new DataInputStream(is);
                os = client_socket.getOutputStream();
                dos = new DataOutputStream(os);
                String line1 = dis.readUTF();
                String[] msg = line1.split(" ");
                UserName = msg[1].trim();
                AppendText("새로운 참가자 " + UserName + " 입장.");
                WriteOne("Welcome to Java chat server\n");
                WriteOne(UserName + "님 환영합니다.\n");
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public void WriteOne(String msg) {
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                AppendText("dos.write() error");
                try {
                    dos.close();
                    dis.close();
                    client_socket.close();
                    UserVec.removeElement(this);
                    connectedUsers.remove(UserName);
                    updateConnectedUsers();
                    AppendText("사용자 퇴장. 남은 참가자 수 " + UserVec.size());
                } catch (Exception ee) {
                }
            }
        }

        public void WriteAll(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = user_vc.get(i);
                user.WriteOne(str);
            }
        }

        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    msg = msg.trim();
                    AppendText(msg);
                    WriteAll(msg + "\n");
                } catch (IOException e) {
                    AppendText("dis.readUTF() error");
                    try {
                        dos.close();
                        dis.close();
                        client_socket.close();
                        UserVec.removeElement(this);
                        connectedUsers.remove(UserName);
                        updateConnectedUsers();
                        AppendText("사용자 퇴장. 남은 참가자 수 " + UserVec.size());
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
}
