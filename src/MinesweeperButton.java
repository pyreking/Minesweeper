import javax.swing.*;

public class MinesweeperButton extends JButton {
	private boolean isMine = false;
	private int state = 0;
	
	public MinesweeperButton() {

	}

	public boolean isMine() {
		return isMine;
	}
	
	public int getState() {
		return state;
	}
	
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public void setState(int newState) {
		state = newState;
		switch (state) {
		case -1: isMine = true;
		break;
		case 0: setEnabled(true);
		break;
		case 1: setEnabled(false);
		}
	}
}