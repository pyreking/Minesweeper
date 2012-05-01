import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.util.*;

public class Minesweeper extends JApplet implements MouseListener {
	/*
	 * TODO: if 0, reveal other panels
	 * TODO: no duplicate numbers
	 * TODO: time mechanism
	 */
	
	Random random = new Random();
	ImageIcon bomb = new ImageIcon("/home/austin/Desktop/Java/School/res/bomb.gif");
	ImageIcon flag = new ImageIcon("/home/austin/Desktop/Java/School/res/flag.png");
	ImageIcon skull = new ImageIcon("/home/austin/Desktop/Java/School/res/skull.gif");
	ImageIcon clock = new ImageIcon("/home/austin/Desktop/Java/School/res/clock.png");
	int rows = 10;
	int columns = 10;
	int numSquares = rows * columns;
	int bombCount = 10;
	int numCloseBombs;
	int currentPos[][];
	JPanel gridPanel = new JPanel();
	JPanel informationPanel = new JPanel();
	MinesweeperButton[][] gridButtons = new MinesweeperButton[rows][columns];
	JButton restartButton = new JButton("Restart");
	JLabel bombsRemaingLabel = new JLabel(bomb);
	JTextField bombsRemaingField = new JTextField(4);
	JLabel timerLabel = new JLabel(clock);
	JTextField timerField = new JTextField(4);
	int flagCount;
	int squaresRevealed;
	boolean gameOver = false;
	boolean won = false;
	
	public void init() {
		setSize(425, 425);
		BorderLayout bord = new BorderLayout();
		GridLayout grid = new GridLayout(rows, columns);
		setLayout(bord);
		gridPanel.setLayout(grid);	
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			gridButtons[row][column] = new MinesweeperButton();
			gridButtons[row][column].setSize(32, 32);
			gridButtons[row][column].addMouseListener(this);
			gridButtons[row][column].setState(0);
			gridPanel.add(gridButtons[row][column]);
			}
		}
		restartButton.addMouseListener(this);
		bombsRemaingField.setEditable(false);
		timerField.setEditable(false);
		timerField.setText("00:00");
		informationPanel.add(bombsRemaingLabel);
		informationPanel.add(bombsRemaingField);
		informationPanel.add(timerLabel);
		informationPanel.add(timerField);
		informationPanel.add(restartButton);
		add(informationPanel, BorderLayout.NORTH);
		add(gridPanel);
		setUpGame();
	}
	
	public void setUpGame() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			if (gridButtons[row][column].getIcon() != null) {
				gridButtons[row][column].setIcon(null);
			}
			gridButtons[row][column].setState(0);
			gridButtons[row][column].setMine(false);
			gridButtons[row][column].setText(null);
			}
		}
		for (int i = 0; i < bombCount; i++) {
			int randomRow = random.nextInt(rows);
			int randomColumn = random.nextInt(columns);
			gridButtons[randomRow][randomColumn].setState(-1);
			// Debug
			System.out.print(randomRow + 1 + ", ");
			System.out.println(randomColumn + 1);
		}
		// Debug
		System.out.println("---");
		gameOver = false;
		won = false;
		bombsRemaingField.setText(String.valueOf(bombCount));
		flagCount = 0;
		squaresRevealed = 0;
	}
	
	public void revealBoard() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			if (gridButtons[row][column].getIcon() != skull 
				&& gridButtons[row][column].isMine()) {
				gridButtons[row][column].setIcon(bomb);
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		numCloseBombs = 0;
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			if (SwingUtilities.isRightMouseButton(e) 
				&& e.getSource() == gridButtons[row][column] 
				&& gridButtons[row][column].isEnabled()
				&& gridButtons[row][column].getIcon() != skull
				&& !won) {
				if (gridButtons[row][column].getIcon() != null) {
					gridButtons[row][column].setIcon(null);
					flagCount--;
					int current = Integer.parseInt(bombsRemaingField.getText());
					bombsRemaingField.setText(String.valueOf(current + 1));
					break;
					} else if (flagCount < bombCount) {
					gridButtons[row][column].setIcon(flag);
					flagCount++;
					bombsRemaingField.setText(String.valueOf(bombCount - flagCount));
					break;
					}
				}
			}
		}
		
		bombLoop:
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
				if (SwingUtilities.isLeftMouseButton(e) &&
					gridButtons[row][column].isMine() && e.getSource() == gridButtons[row][column]
					&& gridButtons[row][column].getIcon() != flag && !won) {
					gridButtons[row][column].setIcon(skull);
					gameOver = true;
					revealBoard();
					break bombLoop;
					}
				}
			}
			
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
				if (SwingUtilities.isLeftMouseButton(e)
					&& e.getSource() == gridButtons[row][column] && !gameOver
					&& gridButtons[row][column].getIcon() != flag && !won
					&& gridButtons[row][column].getState() != 1) {
					boolean up = row != 0;
					boolean down = row != rows - 1;
					boolean left = column != 0;
					boolean right = column != columns - 1;
					boolean upLeft = row != 0 && column != 0;
					boolean upRight = row != 0 && column != columns - 1;
					boolean downLeft = row != rows - 1 && column != 0;
					boolean downRight = row != rows - 1 && column != columns - 1;
					if (up && gridButtons[row - 1][column].isMine()) numCloseBombs++;
					if (down && gridButtons[row + 1][column].isMine()) numCloseBombs++;
					if (left && gridButtons[row][column - 1].isMine()) numCloseBombs++;
					if (right && gridButtons[row][column + 1].isMine()) numCloseBombs++;
					if (upLeft && gridButtons[row - 1][column - 1].isMine()) numCloseBombs++;
					if (upRight && gridButtons[row - 1][column + 1].isMine()) numCloseBombs++;
					if (downLeft && gridButtons[row + 1][column - 1].isMine()) numCloseBombs++;
					if (downRight && gridButtons[row + 1][column + 1].isMine()) numCloseBombs++;	
					gridButtons[row][column].setText(String.valueOf(numCloseBombs));
					gridButtons[row][column].setState(1);
					squaresRevealed++;
					System.out.println(squaresRevealed);
					}
				}
			}	
			
		if (squaresRevealed == numSquares - bombCount) {
			won = true;
		    String newline = System.getProperty("line.separator");  
			int option = JOptionPane.showConfirmDialog(this, "You've defused all " +
				"of the mines!" + newline + "Play again?", "Congratulations!", 
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				setUpGame();
			}
			if (option == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
			squaresRevealed = 0;
		}
		
		if (e.getSource() == restartButton) {
			setUpGame();
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