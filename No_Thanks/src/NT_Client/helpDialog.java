package NT_Client;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;

//게임 설명이 나오는 다이얼로그 
// 도움말 버튼을 누르면 나오는 다이얼로그
public class helpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	
	/**
	 * Create the dialog.
	 */
	public helpDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(helpDialog.class.getResource("/NT_Client/img/NT_icon.png")));
		setTitle("\uAC8C\uC784 \uC124\uBA85");
		setBounds(100, 100, 753, 474);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("\uAC8C\uC784 \uC124\uBA85");
			lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
			lblNewLabel.setBounds(12, 26, 99, 33);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("<html>" + "게임은 4인으로 진행된다. <br>" 
					+ "각 player는 11개의 토큰을 가지고 시작한다. <br>" 
					+ "게임이 시작되면 (3~30의 숫자)카드를 랜덤으로 한장씩 오픈한다. <br>"
					+ "순서대로 각 player가 해당 카드를 먹을지 패스할지 선택한다. <br>"
					+ "패스를 할 시 토큰 1개가 소모된다. <br>"
					+ "토큰이 없을 시 무조건 해당 카드를 먹어야한다. <br>"
					+ "카드가 전부 오픈되면, player가 가지고 있는 카드를 합한다.<br>"
					+ "카드를 더할 때 연속된 카드라면 제일 작은수만 더한다 EX) 4,5,6,20 = 4+20 <br>"
					+ "마지막으로 해당 유저가 가지고 있는 토큰을 뺀다.<br>"
					+ "숫자의 합이 제일 작은 player가 승리한다."+"</html>");
			lblNewLabel_1.setFont(new Font("함초롬돋움", Font.PLAIN, 20));
			lblNewLabel_1.setBounds(12, 69, 701, 325);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("\uB2EB\uAE30");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			}
		}
		setLocation(100,100);
		setVisible(true);
	}

}
