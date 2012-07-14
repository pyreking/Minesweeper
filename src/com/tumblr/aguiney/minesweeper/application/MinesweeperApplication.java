package com.tumblr.aguiney.minesweeper.application;
import com.tumblr.aguiney.minesweeper.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MinesweeperApplication extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 5124093907330223820L;
	Random random = new Random();
	ImageIcon bomb;
	ImageIcon clock;
	ImageIcon flag;
	ImageIcon skull;
	ImageIcon wrongFlag;
	/* the game starts on expert by default, but immediately changes to beginner afterwards.
	 * this is the easiest way to provide multiple levels of difficulty because I can always
	 * add/remove any number of buttons from the grid without worrying that the array might be
	 * too small for one of the difficulties. */
	private int rows = 16;
	private int columns = 30;
	private int bombCount = 99;
	private int numCloseBombs;
	private int flagCount;
	private boolean gameStarted = false;
	private boolean gameOver = false;
	private boolean won = false;
	String difficulty;
	GridLayout grid;
	JMenuBar menu = new JMenuBar();
	JMenu gameMenu = new JMenu("Game");
	JMenuItem newGame = new JMenuItem("New Game");
	JMenuItem options = new JMenuItem("Options");
	JMenuItem quitGame = new JMenuItem("Exit");
	JPanel gridPanel = new JPanel();
	JPanel informationPanel = new JPanel();
	JLabel bombsRemaingLabel;
	JLabel timerLabel;
	JTextField bombsRemaingField = new JTextField(4);
	TimerField timerField = new TimerField(5);
	MinesweeperButton[][] gridButtons = new MinesweeperButton[rows][columns];
	JButton restartButton = new JButton("Restart");
	OptionsFrameApplication ofa = new OptionsFrameApplication(this);
	
	public MinesweeperApplication() {
		super("Minesweeper");
		setSize(425, 425);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadImages();
		BorderLayout bord = new BorderLayout();
		grid = new GridLayout(rows, columns);
		setLayout(bord);
		gridPanel.setLayout(grid);	
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			gridButtons[row][column] = new MinesweeperButton();
			gridButtons[row][column].setSize(32, 32);
			gridButtons[row][column].addMouseListener(this);
			gridButtons[row][column].setState(0);
			}
		}
		restartButton.addActionListener(this);
		bombsRemaingField.setEditable(false);
		timerField.setEditable(false);
		timerLabel = new JLabel(clock);
		bombsRemaingLabel = new JLabel(bomb);
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
		setBoardDifficulty();
		setVisible(true);
	}
	
	// loads various images into memory
	public void loadImages() {
		try {
			bomb = new ImageIcon(MinesweeperApplication.class.getResource("res/bomb.gif"));
			clock = new ImageIcon(MinesweeperApplication.class.getResource("res/clock.png"));
			flag = new ImageIcon(MinesweeperApplication.class.getResource("res/flag.png"));
			skull = new ImageIcon(MinesweeperApplication.class.getResource("res/skull.gif"));
			wrongFlag = new ImageIcon(MinesweeperApplication.class.getResource("res/wrong_flag.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* adds the appropriate number of buttons to the grid based on the
	 * "rows" and "columns" variables. */
	public void addButtons() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
			gridPanel.add(gridButtons[row][column]);
			}
		}
	}
	
	/* changes the number of rows, columns, and bombs on the grid.
	 * difficulty stores the name of the current difficulty level. */
	public void setBoardAttributes(int rows, int columns, int bombCount, String difficulty) {
		this.rows = rows;
		this.columns = columns;
		this.bombCount = bombCount;
		this.difficulty = difficulty;
		grid.setRows(rows);
		grid.setColumns(columns);
	}
	
	
	 /* changes the board difficulty level based on the radio button that 
	 * is currently selected on options frame. */
	public void setBoardDifficulty() {
		if (ofa.choices[0]) {
			setSize(425, 425);
			gridPanel.removeAll();
			setBoardAttributes(9, 9, 10, "Beginner");
			addButtons();
			setUpGame();
		}
		
		if (ofa.choices[1]) {
			setSize(720, 640);
			gridPanel.removeAll();
			setBoardAttributes(16, 16, 40, "Intermediate");
			addButtons();
			setUpGame();
		}
		
		if (ofa.choices[2]) {
			setSize(1300, 630);
			gridPanel.removeAll();
			setBoardAttributes(16, 30, 99, "Expert");
			addButtons();
			setUpGame();
		}
	}
	
	// sets up a new game for play.
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
		generateMines();
		// assigns a value to each square on the grid
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
	
	/* randomly generates a row and a column, then flags the corresponding
	 * square as a mine. if the row and column combination already has a mine, 
	 * keep generating numbers until the combination hasn't been used. */
	public void generateMines() {
		for (int i = 0; i < bombCount; i++) {
			int randomRow;
			int randomColumn;
			do {
				randomRow = random.nextInt(rows);
				randomColumn = random.nextInt(columns);
			} while(gridButtons[randomRow][randomColumn].isMine());
			gridButtons[randomRow][randomColumn].setMine(true);
		}
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
		if (e.getSource() == newGame || e.getSource() == restartButton) {
			setUpGame();
		}
		
		if (e.getSource() == options) {
			ofa.setVisible(true);
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
		
		// handles square flagging
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				/* if there is a right click, a button on the grid
				 * that hasn't been revealed is clicked, the icon of the button 
				 * isn't a skull, and the game has neither been lost nor won. */
				if (SwingUtilities.isRightMouseButton(e) 
				&& e.getSource() == gridButtons[row][column] 
				&& gridButtons[row][column].isEnabled()
				&& gridButtons[row][column].getIcon() != skull
				&& !won && !gameOver) {
				/* if there is already a flag on the square, get rid of it and decrease
				 * the flag count. */
				if (gridButtons[row][column].getIcon() != null) {
					gridButtons[row][column].setIcon(null);
					flagCount--;
					int current = Integer.parseInt(bombsRemaingField.getText());
					bombsRemaingField.setText(String.valueOf(current + 1));
					break;
					/* if there are available flags, set the square's icon to a flag
					 * and increase the flag count. */
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
			// handles game overs
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
				/* if there is a left click, a button on the grid that is a mine and hasn't 
				 * been flagged is clicked, and the game hasn't already been won or lost */
				if (SwingUtilities.isLeftMouseButton(e) 
					&& gridButtons[row][column].isMine() 
					&& e.getSource() == gridButtons[row][column]
					&& gridButtons[row][column].getIcon() != flag && !won && !gameOver) {
					/* set the icon of the square to a skull (to indicate a game over),
					 * stop the timer, reveal the remaining mines, and break the loop. */
					gridButtons[row][column].setIcon(skull);
					gameOver = true;
					timerField.getTimer().stop();
					revealBoard();
					break bombLoop;
					}
				}
			}
		
		// handles clicks on non-mine and non-flagged squares.
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				/* if there is a left click, a button on the square has that hasn't been
				 * revealed and isn't a flag has been clicked, and the game hasn't been won
				 * or lost */
				if (SwingUtilities.isLeftMouseButton(e)
					&& e.getSource() == gridButtons[row][column] && !gameOver
					&& gridButtons[row][column].getIcon() != flag && !won
					&& gridButtons[row][column].getState() != 1) {
					// if the square has no adjacent mines, reveal its surrounding squares
					if (gridButtons[row][column].getValue() == 0) {
						revealSurroundingSquares(row, column);
					}
					// disable the square from being clicked
					gridButtons[row][column].setState(1);
				}
			}
		}
		
		/* if the game has been won, stop the timer and show a win screen with options
		 * to restart the game and to quit the game. */
		if (checkGameWon()) {
			timerField.getTimer().stop();
			won = true;
		    String newline = System.getProperty("line.separator");
		    String finalTime = timerField.getText();
			int option = JOptionPane.showConfirmDialog(this, "You beat the " 
					+ difficulty + " board with a final time of " + finalTime + "!" 
					+ newline + "Play again?", "Congratulations!", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				setUpGame();
			}
			if (option == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
	}
	
	// returns true if the game has been won, false otherwise.
	public boolean checkGameWon() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				/* if a square hasn't been uncovered yet and is not a mine,
				 * then the game hasn't been won yet.*/
				if (gridButtons[row][column].getState() == 0 
					&& !gridButtons[row][column].isMine()) {
					return false;
				}
			}
		}
		return true;
	}
	
	// counts the number of mines adjacent to the specified row and column
	public void countSurroundingMines(int row, int column) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				// no out of bounds
				if (row + dr >= 0 && row + dr < rows &&
					column + dc >= 0 && column + dc < columns) {
					// if the square is a mine, add one to the total count.
					if (gridButtons[row + dr][column + dc].isMine()) {
						numCloseBombs++;
					}
				}
			}
		}
		gridButtons[row][column].setValue(numCloseBombs);
		numCloseBombs = 0;
	}
	
	// if a square has no adjacent mines, then it reveals every adjacent square and recurses.
	public void revealSurroundingSquares(int row, int column) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				// no out of bounds
				if (row + dr >= 0 && row + dr < rows &&
					column + dc >= 0 && column + dc < columns) {
					// if the square isn't already revealed, then reveal it
					if (gridButtons[row + dr][column + dc].getState() != 1) {
						gridButtons[row + dr][column + dc].setState(1);
						// if the revealed square has no adjacent mines, recurse
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
	
	public static void main(String[] args) {
		MinesweeperApplication ma = new MinesweeperApplication();
	}
}