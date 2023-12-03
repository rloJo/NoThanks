package NT_Client;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;

public class StartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Container container;
	private JFrame mainFrame;
	private JTextField nameTextField;
	private helpDialog helpdialog;

	/**
	 * Create the panel.
	 */
	public StartPanel(Container container, JFrame mainframe) {
		setBackground(new Color(225, 113, 0));
		this.container = container;
		this.mainFrame = mainFrame;
		setLayout(null);
		
		JButton startBtn = new JButton("\uC2DC\uC791\uD558\uAE30");
		startBtn.setBounds(600, 383, 100, 50);
		add(startBtn);
		
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
		titleLabel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 38));
		titleLabel.setBounds(300, 90, 400, 80);
		add(titleLabel);
		
		JLabel nickLabel = new JLabel("\uC0AC\uC6A9\uC790 \uC774\uB984 \uC785\uB825");
		nickLabel.setFont(new Font("±¼¸²", Font.BOLD, 21));
		nickLabel.setBounds(375, 329, 198, 45);
		add(nickLabel);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		ImageIcon NTbgImg = new ImageIcon("images\\NT_bg.jpg");
		g.drawImage(NTbgImg.getImage(), 0, 0, 400,600,this);
	}

	
}
