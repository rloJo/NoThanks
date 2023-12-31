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

// 방 생성 버튼을 누르면 나오는 다이얼로그 
// 이 다이얼 로그에서 방 정보를 사용자로 부터 입력 받는다
public class CreateRoomFrame extends JFrame {

	private LobbyPanel lobbyPanel;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField roomNameField;
	private JRadioButton normalBtn;
	private JRadioButton specialBtn;
	
	public CreateRoomFrame(LobbyPanel lobbyPanel) {
		this.lobbyPanel = lobbyPanel;
		setTitle("방 생성하기");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CreateRoomFrame.class.getResource("/NT_Client/img/NT_icon.png")));
		//EXIT -> DISPOSE로 변경: 해당 창만 닫히게
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel roomNameLabel = new JLabel("방 제목");
		roomNameLabel.setFont(new Font("굴림", Font.BOLD, 19));
		roomNameLabel.setBounds(368, 91, 90, 40);
		contentPane.add(roomNameLabel);
		
		roomNameField = new JTextField();
		roomNameField.setBounds(187, 91, 160, 40);
		contentPane.add(roomNameField);
		roomNameField.setColumns(10);
		
		normalBtn = new JRadioButton("일반 모드");
		normalBtn.setBackground(new Color(255, 255, 255));
		normalBtn.setFont(new Font("굴림", Font.PLAIN, 15));
		normalBtn.setBounds(155, 230, 119, 23);
		contentPane.add(normalBtn);
		normalBtn.setSelected(true);
		
		specialBtn = new JRadioButton("특별 모드");
		specialBtn.setBackground(new Color(255, 255, 255));
		specialBtn.setFont(new Font("굴림", Font.PLAIN, 15));
		specialBtn.setBounds(342, 230, 119, 23);
		contentPane.add(specialBtn);
		
		ButtonGroup groupRd = new ButtonGroup();
		groupRd.add(normalBtn);
		groupRd.add(specialBtn);
		
		JButton CreateBtn = new JButton("\uC0DD\uC131");
		CreateBtn.setFont(new Font("굴림", Font.PLAIN, 17));
		CreateBtn.setBounds(221, 305, 112, 34);
		contentPane.add(CreateBtn);
		
		CreateBtn.addActionListener(new CreateBtnClick());
		this.setResizable(false);
	}
	
	//생성 버튼 클릭시 실행되는 이벤트 객체
	// 방 이름 및 모드(노말, 스페셜) 정보를 넘긴다.
	class CreateBtnClick implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String roomName = roomNameField.getText();
	      
	        int roomType = (normalBtn.isSelected()) ? 0 : 1;
	        lobbyPanel.createRoom(roomName,roomType);
	        dispose();
		}
	}
}
