import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Minesweeper extends JApplet {
	Random random = new Random();
	ImageIcon bomb = new ImageIcon("res/bomb.gif");
	JPanel gridPanel = new JPanel();
	JPanel informationPanel = new JPanel();
	JButton[] gridButtons = new JButton[100];
	JButton resetButton = new JButton("Reset");
	JLabel[] buttonLabels = new JLabel[100];
	JLabel bombsRemaingLabel = new JLabel("Bombs remaining: ");
	JTextField bombsRemaingField = new JTextField(4);
	Integer[] numbers = new Integer[100];
	
	public void init() {
		setSize(400, 400);
		BorderLayout bord = new BorderLayout();
		GridLayout grid = new GridLayout(10, 10);
		setLayout(bord);
		gridPanel.setLayout(grid);
		for (int i = 0; i < 100; i++) {
			gridButtons[i] = new JButton();
			gridButtons[i].setSize(32, 32);
			gridPanel.add(gridButtons[i]);
		}
		bombsRemaingField.setEditable(false);
		informationPanel.add(bombsRemaingLabel);
		informationPanel.add(bombsRemaingField);
		add(gridPanel);
		add(informationPanel, BorderLayout.SOUTH);
	}
}