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

//게임 실행에 필요한 다이얼로그 
//서버에게 자신이 카드를 먹을 것인지 토큰을 지불하고 안먹을 것인지 보여주는 다이얼로그
public class gameDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public JLabel DialogLabel;
	LobbyPanel lobbyPanel;
	
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
	
	// 먹는다는 버튼을 누르면 Eat 태그로 서버에 정보를 전달한다
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
	
	//안먹는다는 버튼을 누르면 NoEat 태그로 서버에게 정보를 전달한다
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
