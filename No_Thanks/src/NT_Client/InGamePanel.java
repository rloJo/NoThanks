package NT_Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;

import common.Msg;

public class InGamePanel extends JPanel {
	
	public final int p1 = 0;
	public final int p2 = 1;
	public final int p3 = 2;
	public final int p4 = 3;
	
	public int roomId;
	public int role; //p1 p2 p3 p4
	public int order; // ī�带 �̴� ����
	public int status;
	
	private Container container;
	private CardLayout cardlayout;
	private LobbyPanel lobbyPanel;
	public JLabel p1_nameLabel;
	public JLabel p2_nameLabel;
	public JLabel p3_nameLabel;
	public JLabel p4_nameLabel;
	public JTextArea p1_cardArea;
	public JTextArea p2_cardArea;
	public JTextArea p3_cardArea;
	public JTextArea p4_cardArea;
	public JTextArea chattextArea;
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public InGamePanel(Container container, LobbyPanel lobbypanel) {
		this.container = container;
		this.lobbyPanel = lobbypanel;
		setLayout(null);
		
		JPanel p1_panel = new JPanel();
		p1_panel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(0, 0, 0)));
		p1_panel.setBounds(25, 27, 305, 91);
		add(p1_panel);
		p1_panel.setLayout(null);
		
		p1_nameLabel = new JLabel("p_name\r\n");
		p1_nameLabel.setFont(new Font("����", Font.BOLD, 18));
		p1_nameLabel.setBounds(12, 30, 85, 33);
		p1_panel.add(p1_nameLabel);
		
		p1_cardArea = new JTextArea();
		p1_cardArea.setBounds(98, 10, 195, 71);
		p1_panel.add(p1_cardArea);
		
		JPanel p3_panel = new JPanel();
		p3_panel.setLayout(null);
		p3_panel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(0, 0, 0)));
		p3_panel.setBounds(25, 451, 305, 91);
		add(p3_panel);
		
		p3_nameLabel = new JLabel("p_name\r\n");
		p3_nameLabel.setFont(new Font("����", Font.BOLD, 18));
		p3_nameLabel.setBounds(12, 30, 85, 33);
		p3_panel.add(p3_nameLabel);
		
		p3_cardArea = new JTextArea();
		p3_cardArea.setBounds(98, 10, 195, 71);
		p3_panel.add(p3_cardArea);
		
		JPanel p2_panel = new JPanel();
		p2_panel.setLayout(null);
		p2_panel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(0, 0, 0)));
		p2_panel.setBounds(671, 27, 305, 91);
		add(p2_panel);
		
		p2_nameLabel = new JLabel("p_name\r\n");
		p2_nameLabel.setFont(new Font("����", Font.BOLD, 18));
		p2_nameLabel.setBounds(213, 29, 80, 33);
		p2_panel.add(p2_nameLabel);
		
		p2_cardArea = new JTextArea();
		p2_cardArea.setBounds(12, 10, 195, 71);
		p2_panel.add(p2_cardArea);
		
		JPanel p4_panel = new JPanel();
		p4_panel.setLayout(null);
		p4_panel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(0, 0, 0)));
		p4_panel.setBounds(671, 451, 305, 91);
		add(p4_panel);
		
		JLabel p4_nameLabel = new JLabel("p_name\r\n");
		p4_nameLabel.setFont(new Font("����", Font.BOLD, 18));
		p4_nameLabel.setBounds(213, 29, 80, 33);
		p4_panel.add(p4_nameLabel);
		
		p4_cardArea = new JTextArea();
		p4_cardArea.setBounds(12, 10, 195, 71);
		p4_panel.add(p4_cardArea);
		
		JButton openBtn = new JButton("�ѱ��");
		openBtn.setFont(new Font("����", Font.BOLD | Font.ITALIC, 28));
		openBtn.setBounds(431, 350, 122, 53);
		add(openBtn);
		
		JLabel cardLabel = new JLabel("���� ī�� �� :");
		cardLabel.setFont(new Font("����", Font.BOLD, 19));
		cardLabel.setBounds(431, 133, 179, 34);
		add(cardLabel);
		
		JButton helpBtn = new JButton("����");
		helpBtn.setBounds(565, 369, 68, 23);
		add(helpBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(664, 183, 324, 173);
		add(scrollPane);
		
		chattextArea = new JTextArea();
		scrollPane.setViewportView(chattextArea);

	}
	
	public void roomUserList() {
		Msg msg = new Msg(lobbyPanel.userName,"401","userList");
	}
	
	public void AppendChat(String userName, String str) {
        chattextArea.append("["+ userName + "] : " + str + "\n");
        chattextArea.setCaretPosition(chattextArea.getText().length());
    }
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		ImageIcon NTqcImg = new ImageIcon("images\\questionmark_card.jpg");
		g.drawImage(NTqcImg.getImage(), 0, 0, 400,600,this);
		
		ImageIcon NTocImg = new ImageIcon("images\\1.jpg");
		g.drawImage(NTocImg.getImage(), 0, 0, 400,600,this);
	}
}
