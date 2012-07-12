import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.Timer;

public class TimerField extends JTextField implements ActionListener {
	private int secondsElapsed;
	private int minutesElapsed;
	private int hoursElapsed;
	// the delay for the timer is 1000 milliseconds or 1 second
	Timer timer = new Timer(1000, this);
	
	public TimerField(int columns) {
		setColumns(columns);
		setText("00:00:00");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			calculateTime();
		}
	}
	
	/* calculates and displays the current amount of elapsed time in terms of hours,
	 * minutes, and seconds. */
	public void calculateTime() {
		secondsElapsed++;
		
		// after 60 secs, add a minute to the clock and reset secs back to 0.
		if (secondsElapsed == 60) {
			minutesElapsed++;
			secondsElapsed = 0;
		}
		
		// after 60 mins, add an hour to the clock and reset mins and secs to 0.
		if (minutesElapsed == 60) {
			hoursElapsed++;
			minutesElapsed = 0;
			secondsElapsed = 0;
		}
		
		// 00:00:00 --> 00:00:09 etc
		if (hoursElapsed < 10 && minutesElapsed < 10 && secondsElapsed < 10) {
			setText("0" + hoursElapsed + ":0" + minutesElapsed + ":0" + secondsElapsed);
		
		// 00:00:10 --> 00:00:59 etc
		} else if (hoursElapsed < 10 && minutesElapsed < 10 && secondsElapsed < 60) {
			setText("0" + hoursElapsed + ":0" + minutesElapsed + ":" + secondsElapsed);
		}
		
		// 00:10:00 --> 00:10:09 etc
		if (hoursElapsed < 10 && minutesElapsed >= 10 && secondsElapsed < 10) {
			setText("0" + hoursElapsed + ":" + minutesElapsed + ":0" + secondsElapsed);
		
		// 00:10:10 --> 09:10:59 etc
		} else if (hoursElapsed < 10 && minutesElapsed >= 10 && secondsElapsed < 60) {
			setText("0" + hoursElapsed + ":" + minutesElapsed + ":" + secondsElapsed);
		}
		
		// 10:00:00 --> 10:00:09 etc
		if (hoursElapsed >= 10 && minutesElapsed < 10 && secondsElapsed < 10) {
			setText(hoursElapsed + ":0" + minutesElapsed + ":0" + secondsElapsed);
			
		// 10:00:10 --> 10:00:59 etc
		} else if (hoursElapsed >= 10 && minutesElapsed < 10 && secondsElapsed < 60) {
			setText(hoursElapsed + ":0" + minutesElapsed + ":" + secondsElapsed);
		}
		
		// 10:10:00 --> 10:10:09 etc
		if (hoursElapsed >= 10 && minutesElapsed >= 10 && secondsElapsed < 10) {
			setText(hoursElapsed + ":" + minutesElapsed + ":0" + secondsElapsed);
		
		// 10:10:10 --> 10:10:59 etc
		} else if (hoursElapsed >= 10 && minutesElapsed >= 10 && secondsElapsed < 60) {
			setText(hoursElapsed + ":" + minutesElapsed + ":" + secondsElapsed);
		}
	}
	
	// self-explanatory
	public void resetElapsedTime() {
		secondsElapsed = 0;
		minutesElapsed = 0;
		hoursElapsed = 0;
		setText("00:00:00");
	}
	
	// this is so that the Minesweeper class can start/stop the timer as it pleases
	public Timer getTimer() {
		return timer;
	}
}
