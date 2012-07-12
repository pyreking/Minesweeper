import javax.swing.*;

public class MinesweeperButton extends JButton {
	// is the square a mine
	private boolean isMine = false;
	// is the square open or closed
	private int state = 0;
	// number of adjacent squares that contain mines
	private int value;
	
	public MinesweeperButton() {

	}

	public boolean isMine() {
		return isMine;
	}
	
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int newState) {
		state = newState;
		switch (state) {
		case 0: setEnabled(true); // open square
		break;
		case 1: setEnabled(false); // closed square
				if (value != 0) setText(String.valueOf(value));
				if (value >= 0 && getIcon() != null) setIcon(null);
		}
	}
	
	// true if the square is open, false otherwise
	public boolean isPressed() {
		if (value == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}