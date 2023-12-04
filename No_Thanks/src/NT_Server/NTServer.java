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

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  JTextArea textArea;
  private JTextField txtPortNumber;

  private ServerSocket socket; // ��������
  private Socket client_socket; // accept() ���� ������ client ����
  private Vector<UserService> UserVec = new Vector<>(); // ����� ����ڸ� ������ ����, ArrayList�� ���� ���� �迭�� ������ִ� �÷��� ��ü�̳� ����ȭ�� ���� ������ ���
  private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
  /**
   * Launch the application.
   */
  public static void main(String[] args) {   // ���� ���־� �����̳ʸ� �̿��� GUI�� ����� �ڵ����� �����Ǵ� main �Լ�
      EventQueue.invokeLater(new Runnable() {
          public void run() {
              try {
            	  NTServer frame = new NTServer();      // JavaChatServer Ŭ������ ��ü ����
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
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
              }
              AppendText("Chat Server Running..");
              btnServerStart.setText("Chat Server Running..");
              btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
              txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
              AcceptServer accept_server = new AcceptServer();   // ��Ƽ ������ ��ü ����
              accept_server.start();
          }
      });
      btnServerStart.setBounds(12, 300, 300, 35);
      contentPane.add(btnServerStart);
  }

  // ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�. �ѹ� ���� ��� ����ϴ� ������
  class AcceptServer extends Thread {
      @SuppressWarnings("unchecked")
      public void run() {
          while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
              try {
                  AppendText("Waiting clients ...");
                  client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
                  AppendText("���ο� ������ from " + client_socket);
                  // User �� �ϳ��� Thread ����
                  UserService new_user = new UserService(client_socket);
                  UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
                  AppendText("����� ����. ���� ������ �� " + UserVec.size());
                  new_user.start(); // ���� ��ü�� ������ ����
              } catch (IOException e) {
                  AppendText("!!!! accept ���� �߻�... !!!!");
              }
          }
      }
  }

  //JtextArea�� ���ڿ��� ����� �ִ� ����� �����ϴ� �Լ�
  public void AppendText(String str) {
      textArea.append(str + "\n");   //���޵� ���ڿ� str�� textArea�� �߰�
      textArea.setCaretPosition(textArea.getText().length());  // textArea�� Ŀ��(ĳ��) ��ġ�� �ؽ�Ʈ ������ ���������� �̵�
  }

  // User �� �����Ǵ� Thread, ������ ����ŭ ������ ����
  // Read One ���� ��� -> Write All
  class UserService extends Thread {
      private InputStream is;
      private OutputStream os;
      private DataInputStream dis;
      private DataOutputStream dos;
      private Socket client_socket;
      private Vector<UserService> user_vc; // ���׸� Ÿ�� ���
      private String UserName = "";

      public UserService(Socket client_socket) {
          // �Ű������� �Ѿ�� �ڷ� ����
          this.client_socket = client_socket;
          this.user_vc = UserVec;
          try {
              is = client_socket.getInputStream();
              dis = new DataInputStream(is);
              os = client_socket.getOutputStream();
              dos = new DataOutputStream(os);
              String line1 = dis.readUTF();      // ���� ó�� ����Ǹ� SendMessage("/login " + UserName);�� ���� "/login UserName" ���ڿ��� ����
              String[] msg = line1.split(" ");   //line1�̶�� ���ڿ��� ����(" ")�� �������� ����
              UserName = msg[1].trim();          //���ҵ� ���ڿ� �迭 msg�� �� ��° ���(�ε��� 1)�� ������ trim �޼ҵ带 ����Ͽ� �յ��� ������ ����
              AppendText("���ο� ������ " + UserName + " ����.");
              WriteOne("Welcome to Java chat server\n");
              WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
          } catch (Exception e) {
              AppendText("userService error");
          }
      }


      // Ŭ���̾�Ʈ�� �޽��� ����
      public void WriteOne(String msg) {
          try {
              dos.writeUTF(msg);
          } catch (IOException e) {
              AppendText("dos.write() error");
              try {
                  dos.close();
                  dis.close();
                  client_socket.close();
              } catch (IOException e1) {
                  e1.printStackTrace();
              }
              UserVec.removeElement(this); // �������� ���� ��ü�� ���Ϳ��� �����
              AppendText("����� ����. ���� ������ �� " + UserVec.size());
          }
      }

      
      //��� ���� Ŭ���̾�Ʈ���� ���������� ä�� �޽��� ����
      public void WriteAll(String str) {  
          for (int i = 0; i < user_vc.size(); i++) {
          	UserService user = user_vc.get(i);     // get(i) �޼ҵ�� user_vc �÷����� i��° ��Ҹ� ��ȯ
              user.WriteOne(str);
          }
      }
      
      
      public void run() {
          while (true) {
              try {
                  String msg = dis.readUTF(); 
                  msg = msg.trim();   //msg�� ������ trim �޼ҵ带 ����Ͽ� �յ��� ������ ����
                  AppendText(msg); // server ȭ�鿡 ���
                  WriteAll(msg + "\n"); // Write All
              } catch (IOException e) {
                  AppendText("dis.readUTF() error");
                  try {
                      dos.close();
                      dis.close();
                      client_socket.close();
                      UserVec.removeElement(this); // ������ �� ���� ��ü�� ���Ϳ��� �����
                      AppendText("����� ����. ���� ������ �� " + UserVec.size());
                      break;
                  } catch (Exception ee) {
                      break;
                  } 
              }
          }
      }
      
  }
}
