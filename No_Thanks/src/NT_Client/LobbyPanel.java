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
import java.io.OutputStream;
import java.net.Socket;

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
	private static final int BUF_LEN = 128;
	private String userName;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
    private DataOutputStream dos;
	
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
		waitingroominfoLabel = new JLabel("���ȣ    ������              �ο���     ���� ���     �� ����");
		waitingroominfoLabel.setBackground(new Color(240, 240, 240));
		waitingroominfoLabel.setFont(new Font("����", Font.BOLD, 29));
		waitingroominfoLabel.setBounds(96, 10, 800, 45);
		add(waitingroominfoLabel);
		
		roomlist = new JList<String>();
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
		scrollPane.setViewportView(textArea);
		
		userinfoLabel = new JLabel("���� ���");
		userinfoLabel.setFont(new Font("����", Font.BOLD, 24));
		userinfoLabel.setBounds(641, 306, 119, 33);
		add(userinfoLabel);
		
		chatTextField = new TextField();
		chatTextField.setBounds(96, 525, 456, 31);
		add(chatTextField);
		
		sendBtn = new JButton("����");
		sendBtn.setFont(new Font("����", Font.BOLD, 18));
		sendBtn.setBounds(558, 529, 71, 31);
		add(sendBtn);
		
		userlist = new JList<String>();
		userlist.setBounds(651, 354, 245, 183);
		add(userlist);
		
		AppendText("User " + userName + " connecting " + ip_addr + " " + port_num + "\n");
		this.userName = userName;

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
            sendBtn.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
            chatTextField.addActionListener(action);
            chatTextField.requestFocus();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            AppendText("connect error");
        }
		

	}
	 // Server Message�� �����ؼ� ȭ�鿡 ǥ��
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    // Use readUTF to read messages
                    String msg = dis.readUTF();
                    AppendText(msg);
                } catch (IOException e) {
                    AppendText("dis.read() error");
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

	// keyboard enter key ġ�� ������ ����
	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == chatTextField) {
				String msg = null;
				msg = String.format("[%s] %s\n", userName, chatTextField.getText());
				SendMessage(msg);
				chatTextField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}

    // ȭ�鿡 ���
    public void AppendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }


    // Server���� network���� ����
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
	
}
