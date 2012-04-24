import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;

public class Minesweeper extends JApplet implements MouseListener {
	Random random = new Random();
	ImageIcon bomb = new ImageIcon("/home/austin/Desktop/Java/School/res/bomb.gif");
	ImageIcon flag = new ImageIcon("/home/austin/Desktop/Java/School/res/flag.png");
	JPanel gridPanel = new JPanel();
	JPanel informationPanel = new JPanel();
	JButton[] gridButtons = new JButton[100];
	JButton restartButton = new JButton("Restart");
	JLabel[] buttonLabels = new JLabel[100];
	JLabel bombsRemaingLabel = new JLabel("#Bombs: ");
	JTextField bombNumberField = new JTextField(4);
	int bombCount = 10;
	int[] randomNumbers = new int[bombCount];
	int[] above, below, left, right = new int[100];
	int flagCount;
	
	public void init() {
		setSize(400, 400);
		BorderLayout bord = new BorderLayout();
		GridLayout grid = new GridLayout(10, 10);
		setLayout(bord);
		gridPanel.setLayout(grid);	
		for (int i = 0; i < 100; i++) {
			gridButtons[i] = new JButton();
			gridButtons[i].setSize(32, 32);
			gridButtons[i].addMouseListener(this);
			gridPanel.add(gridButtons[i]);
		}
		bombNumberField.setEditable(false);
		bombNumberField.setText(String.valueOf(bombCount));
		informationPanel.add(bombsRemaingLabel);
		informationPanel.add(bombNumberField);
		informationPanel.add(restartButton);
		add(gridPanel);
		add(informationPanel, BorderLayout.SOUTH);
		setUpGame();
	}
	
	public void setUpGame() {
		for (int i = 0; i < bombCount; i++) {
			randomNumbers[i] = random.nextInt(100);
		}
	}

	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < 100; i++) {
			if (SwingUtilities.isRightMouseButton(e) 
				&& e.getSource() == gridButtons[i]) {
				if (gridButtons[i].getIcon() != null) {
					gridButtons[i].setIcon(null);
					flagCount--;
					break;
				} else if (flagCount < 10) {
					gridButtons[i].setIcon(flag);
					flagCount++;
					break;
				}
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {

	}
}