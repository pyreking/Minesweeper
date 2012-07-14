package com.tumblr.aguiney.minesweeper.applet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsFrameApplet extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4715603895581307778L;
	MinesweeperApplet mine;
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JRadioButton beginner = new JRadioButton("Beginner (9x9 grid, 10 mines)", false);
	JRadioButton intermediate = new JRadioButton("Intermediate (16x16 grid, 40 mines)", false);
	JRadioButton expert = new JRadioButton("Expert (16x30 grid, 99 mines)", true);
	JRadioButton radioChoices[] = {beginner, intermediate, expert};
	boolean choices[] = {false, false, true};
	
	/* constructs the gui. each radio button belongs to a button group to ensure that only
	 * one button is selected at a time. the layout manager is a grid layout composed of
	 * 0 rows and one column. It takes a Minesweeper as an argument so that I can call its
	 * changeBoardSize() method after the user selects a radio button. */
	public OptionsFrameApplet(MinesweeperApplet mine) {
		super("Options");
		this.mine = mine;
		JPanel panel = new JPanel();
		ok.addActionListener(this);
		cancel.addActionListener(this);
		beginner.addActionListener(this);
		intermediate.addActionListener(this);
		expert.addActionListener(this);
		add(beginner);
		add(intermediate);
		add(expert);
		ButtonGroup group = new ButtonGroup();
		group.add(beginner);
		group.add(intermediate);
		group.add(expert);
		panel.add(ok);
		panel.add(cancel);
		add(panel);
		setSize(300, 220);
		setLayout(new GridLayout(0, 1));
		setVisible(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		/* if the use presses the ok button, then it determines which radio button was selected
		 * through setRadioButtonSwitch() before calling the changeBoardSize() method of the
		 * Minesweeper class. The frame then becomes invisible */
		if (e.getSource() == ok) {
			setRadioButtonSwitch();
			mine.setBoardDifficulty();
			setVisible(false);
		}
		
		/* if the user presses the cancel button, then the windows should turn invisible
		 * and the radio button that was last chosen should be selected */
		if (e.getSource() == cancel) {
			setVisible(false);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i]) {
					radioChoices[i].setSelected(true);
				}
			}
		}
	}
	
	/* pretty self-explanatory. when one of the radio buttons is selected, its 
	 * corresponding boolean is be set to true and the others set to false. */
	public void setRadioButtonSwitch() {
		if (beginner.isSelected()) {
			choices[0] = true;
			choices[1] = false;
			choices[2] = false;
		}
		
		if (intermediate.isSelected()) {
			choices[0] = false;
			choices[1] = true;
			choices[2] = false;
		}
		
		if (expert.isSelected()) {
			choices[0] = false;
			choices[1] = false;
			choices[2] = true;
		}
	}
}