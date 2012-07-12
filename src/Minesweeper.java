import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Minesweeper extends JApplet implements ActionListener, MouseListener {
	Random random = new Random();
	ImageIcon bomb = new ImageIcon("/home/austin/Desktop/Java/Minesweeper/res/bomb.gif");
	ImageIcon flag = new ImageIcon("/home/austin/Desktop/Java/Minesweeper/res/flag.png");
	ImageIcon skull = new ImageIcon("/home/austin/Desktop/Java/Minesweeper/res/skull.gif");
	ImageIcon clock = new ImageIcon("/home/austin/Desktop/Java/Minesweeper/res/clock.png");
	ImageIcon wrongFlag = new ImageIcon("/home/austin/Desktop/Java/Minesweeper/res/wrong_flag.png");
	private int rows = 9;
	private int columns = 9;
	private int bombCount = 10;
	private int numCloseBombs;
	private int flagCount;
	private boolean gameStarted = false;
	private boolean gameOver = false;
	private boolean won = false;
	JMenuBar menu = new JMenuBar();
	JMenu gameMenu = new JMenu("Game");
	JMenuItem newGame = new JMenuItem("New Game");
	JMenuItem options = new JMenuItem("Options");
	JMenuItem quitGame = new JMenuItem("Exit");
	JPanel gridPanel = new JPanel();
	JPanel informationPanel = new JPanel();
	JLabel bombsRemaingLabel = new JLabel(bomb);
	JLabel timerLabel = new JLabel(clock);
	JTextField bombsRemaingField = new JTextField(4);
	TimerField timerField = new TimerField(6);
	MinesweeperButton[][] gridButtons = new MinesweeperButton[rows][columns];
	JButton restartButton = new JButton("Restart");
	OptionsFrame of = new OptionsFrame(this);
	
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
		informationPanel.add(bombsRemaingLabel);
		informationPanel.add(bombsRemaingField);
		informationPanel.add(timerLabel);
		informationPanel.add(timerField);
		informationPanel.add(restartButton);
		newGame.setAccelerator(KeyStroke.getKeyStroke("F1"));
		newGame.addActionListener(this);
		options.setAccelerator(KeyStroke.getKeyStroke("F2"));
		options.addActionListener(this);
		quitGame.setAccelerator(KeyStroke.getKeyStroke("F3"));
		quitGame.addActionListener(this);
		gameMenu.add(newGame);
		gameMenu.add(options);
		gameMenu.add(quitGame);
		menu.add(gameMenu);
		setJMenuBar(menu);
		add(informationPanel, BorderLayout.NORTH);
		add(gridPanel);
		setUpGame();
	}
	
	public void setBoardSize() {
		if (of.choices[0]) {

		}
		
		if (of.choices[1]) {
			
		}
		
		if (of.choices[2]) {
			
		}
	}
	
	public void setUpGame() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				// if the square has an icon, replace it with nothing
				if (gridButtons[row][column].getIcon() != null) {
					gridButtons[row][column].setIcon(null);
				}
			// reset the squares to their natural state
			gridButtons[row][column].setState(0);
			gridButtons[row][column].setMine(false);
			gridButtons[row][column].setText(null);
			}
		}
		
		for (int i = 0; i < bombCount; i++) {
			int randomRow;
			int randomColumn;
			
			/* randomly generates a row and a column, then flag the corresponding
			 * square as a mine. if the row and column combination already has a mine, 
			 * keep generating numbers. */
			do {
			randomRow = random.nextInt(rows);
			randomColumn = random.nextInt(columns);
			} while(gridButtons[randomRow][randomColumn].isMine());
			gridButtons[randomRow][randomColumn].setMine(true);
			
			// Debug
			System.out.print(randomRow + 1 + ", ");
			System.out.println(randomColumn + 1);
		}
		// Debug
		System.out.println("---");
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				countSurroundingMines(row, column);
			}
		}
		gameStarted = false;
		gameOver = false;
		won = false;
		bombsRemaingField.setText(String.valueOf(bombCount));
		flagCount = 0;
		timerField.getTimer().stop();
		timerField.resetElapsedTime();
	}
	
	/* reveals all of the mines that weren't flagged by the user. additionally, it informs 
	 * the user if they flagged a non-mine. */
	public void revealBoard() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				/* if the square is a mine and its icon isn't a skull or a flag, then
				 * set the icon to a bomb. */
				if (gridButtons[row][column].getIcon() != skull 
				&& gridButtons[row][column].getIcon() != flag 
				&& gridButtons[row][column].isMine()) {
				gridButtons[row][column].setIcon(bomb);
				}
				/* if the square isn't a mine and its icon is a flag, change the icon to
				 * a wrong flag. */
				if (gridButtons[row][column].getIcon() == flag
				&& !gridButtons[row][column].isMine()) {
				gridButtons[row][column].setIcon(wrongFlag);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame) {
			setUpGame();
		}
		
		if (e.getSource() == options) {
			of.setVisible(true);
		}
		
		if (e.getSource() == quitGame) {
			System.exit(0);
		}
	}

	public void mouseClicked(MouseEvent e) {
		/* if the game hasn't already started, then set gameStarted to
	 	 * true and start the timer */
		if (!gameStarted) {
			gameStarted = true;
			timerField.getTimer().start();
		}
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (SwingUtilities.isRightMouseButton(e) 
				&& e.getSource() == gridButtons[row][column] 
				&& gridButtons[row][column].isEnabled()
				&& gridButtons[row][column].getIcon() != skull
				&& !won && !gameOver) {
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
				if (SwingUtilities.isLeftMouseButton(e) 
					&& gridButtons[row][column].isMine() 
					&& e.getSource() == gridButtons[row][column]
					&& gridButtons[row][column].getIcon() != flag && !won && !gameOver) {
					
					gridButtons[row][column].setIcon(skull);
					gameOver = true;
					timerField.getTimer().stop();
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
					
					if (gridButtons[row][column].getValue() == 0) {
						revealSurroundingSquares(row, column);
					}
					gridButtons[row][column].setState(1);
				}
			}
		}
		
		if (checkGameWon()) {
			timerField.getTimer().stop();
			won = true;
		    String newline = System.getProperty("line.separator");
		    String finalTime = timerField.getText();
			int option = JOptionPane.showConfirmDialog(this, "Your final time was "+ 
						finalTime + "!" + newline + "Play again?", "Congratulations!",
						JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				setUpGame();
			}
			if (option == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
		
		if (e.getSource() == restartButton) {
			setUpGame();
		}
	}
	
	public boolean checkGameWon() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (gridButtons[row][column].getState() == 0 
					&& !gridButtons[row][column].isMine()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void countSurroundingMines(int row, int column) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				if (row + dr >= 0 && row + dr < rows &&
					column + dc >= 0 && column + dc < columns) {
					if (gridButtons[row + dr][column + dc].isMine()) {
						numCloseBombs++;
					}
				}
			}
		}
		gridButtons[row][column].setValue(numCloseBombs);
		numCloseBombs = 0;
	}
	
	public void revealSurroundingSquares(int row, int column) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				if (row + dr >= 0 && row + dr < rows &&
					column + dc >= 0 && column + dc < columns) {
					if (gridButtons[row + dr][column + dc].getState() != 1) {
						gridButtons[row + dr][column + dc].setState(1);
						if (gridButtons[row + dr][column + dc].getValue() == 0) {
							revealSurroundingSquares(row + dr, column + dc);
						}
					}
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