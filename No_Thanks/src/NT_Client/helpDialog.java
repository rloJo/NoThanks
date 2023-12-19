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

//���� ������ ������ ���̾�α� 
// ���� ��ư�� ������ ������ ���̾�α�
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
			lblNewLabel.setFont(new Font("����", Font.BOLD, 20));
			lblNewLabel.setBounds(12, 26, 99, 33);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("<html>" + "������ 4������ ����ȴ�. <br>" 
					+ "�� player�� 11���� ��ū�� ������ �����Ѵ�. <br>" 
					+ "������ ���۵Ǹ� (3~30�� ����)ī�带 �������� ���徿 �����Ѵ�. <br>"
					+ "������� �� player�� �ش� ī�带 ������ �н����� �����Ѵ�. <br>"
					+ "�н��� �� �� ��ū 1���� �Ҹ�ȴ�. <br>"
					+ "��ū�� ���� �� ������ �ش� ī�带 �Ծ���Ѵ�. <br>"
					+ "ī�尡 ���� ���µǸ�, player�� ������ �ִ� ī�带 ���Ѵ�.<br>"
					+ "ī�带 ���� �� ���ӵ� ī���� ���� �������� ���Ѵ� EX) 4,5,6,20 = 4+20 <br>"
					+ "���������� �ش� ������ ������ �ִ� ��ū�� ����.<br>"
					+ "������ ���� ���� ���� player�� �¸��Ѵ�."+"</html>");
			lblNewLabel_1.setFont(new Font("���ʷҵ���", Font.PLAIN, 20));
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
