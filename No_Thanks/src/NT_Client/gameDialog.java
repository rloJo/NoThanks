package NT_Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.Msg;

public class gameDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public JLabel DialogLabel;
	LobbyPanel lobbyPanel;
	
	/**
	 * Create the dialog.
	 */
	public gameDialog(LobbyPanel lobbyPanel) {
		this.lobbyPanel = lobbyPanel;
		setBounds(100, 100, 472, 334);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			DialogLabel = new JLabel("<html> 현재까지 쌓인 토큰" + lobbyPanel.IngamePanel.token_stack + "<br>"+ 
										lobbyPanel.userName + "님의 남은 토큰 : " + 
										Integer.toString(lobbyPanel.IngamePanel.token) + 
										"<br>" +
										lobbyPanel.IngamePanel.card.getText() + "를 가져 가겠습니까?</html>"
									);
			DialogLabel.setFont(new Font("굴림", Font.PLAIN, 22));
			DialogLabel.setBounds(140, 62, 240, 121);
			contentPanel.add(DialogLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("먹는다");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new OkBtnAction());
				getRootPane().setDefaultButton(okButton);
			}
			{
				
				JButton cancelButton = new JButton("안먹는다");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new CancelBtnAction());
				buttonPane.add(cancelButton);
				if(lobbyPanel.IngamePanel.token == 0)
					cancelButton.setEnabled(false);
			}
		}
	}
	
	class OkBtnAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = lobbyPanel.userName;
			Msg msg = new Msg(userName, "Eat", lobbyPanel.IngamePanel.card.getText());
			msg.setCard(Integer.parseInt(lobbyPanel.IngamePanel.card.getText()));
			msg.setRoomId(lobbyPanel.roomId);
			msg.setRole(lobbyPanel.IngamePanel.role);
			lobbyPanel.sendObject(msg);
			dispose();
		}
	}
	
	class CancelBtnAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = lobbyPanel.userName;
			Msg msg = new Msg(userName, "NoEat", lobbyPanel.IngamePanel.card.getText());
			msg.upToken();
			msg.setRoomId(lobbyPanel.roomId);
			msg.setRole(lobbyPanel.IngamePanel.role);
			lobbyPanel.sendObject(msg);
			dispose();
		}
	}
	
	
	
	
	
	

}
