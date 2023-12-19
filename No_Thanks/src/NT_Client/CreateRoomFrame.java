package NT_Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.Color;
import javax.swing.JButton;

// �� ���� ��ư�� ������ ������ ���̾�α� 
// �� ���̾� �α׿��� �� ������ ����ڷ� ���� �Է� �޴´�
public class CreateRoomFrame extends JFrame {

	private LobbyPanel lobbyPanel;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField roomNameField;
	private JRadioButton normalBtn;
	private JRadioButton specialBtn;
	
	public CreateRoomFrame(LobbyPanel lobbyPanel) {
		this.lobbyPanel = lobbyPanel;
		setTitle("�� �����ϱ�");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CreateRoomFrame.class.getResource("/NT_Client/img/NT_icon.png")));
		//EXIT -> DISPOSE�� ����: �ش� â�� ������
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel roomNameLabel = new JLabel("�� ����");
		roomNameLabel.setFont(new Font("����", Font.BOLD, 19));
		roomNameLabel.setBounds(368, 91, 90, 40);
		contentPane.add(roomNameLabel);
		
		roomNameField = new JTextField();
		roomNameField.setBounds(187, 91, 160, 40);
		contentPane.add(roomNameField);
		roomNameField.setColumns(10);
		
		normalBtn = new JRadioButton("�Ϲ� ���");
		normalBtn.setBackground(new Color(255, 255, 255));
		normalBtn.setFont(new Font("����", Font.PLAIN, 15));
		normalBtn.setBounds(155, 230, 119, 23);
		contentPane.add(normalBtn);
		normalBtn.setSelected(true);
		
		specialBtn = new JRadioButton("Ư�� ���");
		specialBtn.setBackground(new Color(255, 255, 255));
		specialBtn.setFont(new Font("����", Font.PLAIN, 15));
		specialBtn.setBounds(342, 230, 119, 23);
		contentPane.add(specialBtn);
		
		ButtonGroup groupRd = new ButtonGroup();
		groupRd.add(normalBtn);
		groupRd.add(specialBtn);
		
		JButton CreateBtn = new JButton("\uC0DD\uC131");
		CreateBtn.setFont(new Font("����", Font.PLAIN, 17));
		CreateBtn.setBounds(221, 305, 112, 34);
		contentPane.add(CreateBtn);
		
		CreateBtn.addActionListener(new CreateBtnClick());
		this.setResizable(false);
	}
	
	//���� ��ư Ŭ���� ����Ǵ� �̺�Ʈ ��ü
	// �� �̸� �� ���(�븻, �����) ������ �ѱ��.
	class CreateBtnClick implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String roomName = roomNameField.getText();
	      
	        int roomType = (normalBtn.isSelected()) ? 0 : 1;
	        lobbyPanel.createRoom(roomName,roomType);
	        dispose();
		}
	}
}
