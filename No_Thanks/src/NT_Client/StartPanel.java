package NT_Client;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Container container;
	private JFrame mainFrame;
	private CardLayout cardLayout;
	private LobbyPanel lobbypanel;
	private JTextField nameTextField;
	private helpDialog helpdialog;
	private JTextField iptextField;
	private JTextField porttextField;

	/**
	 * Create the panel.
	 */
	public StartPanel(Container container, JFrame mainframe) {
		setBackground(Color.WHITE);
		this.container = container;
		this.mainFrame = mainFrame;
		this.cardLayout = (CardLayout) container.getLayout();
		setLayout(null);
		
		JButton startBtn = new JButton("\uC2DC\uC791\uD558\uAE30");
		startBtn.setBounds(600, 383, 100, 50);
		add(startBtn);
		StartBtnAction startAction = new StartBtnAction();
		startBtn.addActionListener(startAction);
		
		JButton HelpBtn = new JButton("\uB3C4\uC6C0\uB9D0");
		HelpBtn.setBounds(600, 464, 100, 50);
		HelpBtn.addActionListener(new helpBtnListener(helpdialog));
		add(HelpBtn);
		
		nameTextField = new JTextField();
		nameTextField.setBackground(new Color(255, 255, 255));
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setBounds(375, 384, 198, 50);
		add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel titleLabel = new JLabel("No Thanks");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 38));
		titleLabel.setBounds(300, 90, 400, 80);
		add(titleLabel);
		
		JLabel nickLabel = new JLabel("사용자 이름 입력");
		nickLabel.setFont(new Font("굴림", Font.BOLD, 21));
		nickLabel.setBounds(375, 329, 198, 45);
		add(nickLabel);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(261, 192, 82, 33);
		add(lblIpAddress);
		
		iptextField = new JTextField();
		iptextField.setText("127.0.0.1");
		iptextField.setHorizontalAlignment(SwingConstants.CENTER);
		iptextField.setColumns(10);
		iptextField.setBounds(382, 198, 116, 33);
		add(iptextField);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(261, 261, 82, 33);
		add(lblPortNumber);
		
		porttextField = new JTextField();
		porttextField.setText("30000");
		porttextField.setHorizontalAlignment(SwingConstants.CENTER);
		porttextField.setColumns(10);
		porttextField.setBounds(382, 261, 116, 33);
		add(porttextField);
	}
	
	class StartBtnAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = nameTextField.getText().trim();
			if(userName.equals("")) { // 이름 미입력 시
				userName = "default user";
			}
		
			String ip_addr = iptextField.getText().trim();
			String port_no = porttextField.getText().trim();
			
			lobbypanel = new LobbyPanel(container, mainFrame, userName, ip_addr, port_no);
			container.add(lobbypanel, "lobbypanel");
			cardLayout.show(container, "lobbypanel"); // 대기실 Panel로 변경
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		ImageIcon NTbgImg = new ImageIcon("images\\NT_bg.jpg");
		g.drawImage(NTbgImg.getImage(), 0, 0, 400,600,this);
	}
}
