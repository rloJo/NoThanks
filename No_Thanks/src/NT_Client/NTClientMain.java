package NT_Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;

public class NTClientMain extends JFrame {
	
	private  CardLayout cardLayout;
	private Container container;
	private StartPanel startPanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NTClientMain frame = new NTClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NTClientMain() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NTClientMain.class.getResource("/NT_Client/img/NT_icon.png")));
		this.setTitle("NoThanks");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cardLayout = new CardLayout();
		container = this.getContentPane();
		container.setLayout(cardLayout);
		
		this.startPanel = new StartPanel(container,this);
		startPanel.setBackground(new Color(255, 255, 255));
		container.add(startPanel, "startPanel");
		
		this.setBounds(100, 100, 1000, 600);
		setResizable(false); //창의 사이즈 조절을 막는다
	}
}

