package NT_Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//도움말 버튼 액션 리스너 도움말 다이얼로그가 나타나게한다
class helpBtnListener implements ActionListener{
	helpDialog help_Dialog;
	
	public helpBtnListener() {	
	}
	
	public helpBtnListener(helpDialog dialog){
		this.help_Dialog = dialog;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		help_Dialog = new helpDialog();
		
	}
}