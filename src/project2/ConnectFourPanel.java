package project2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**********************************************************************
 * 
 * A panel which contains and displays a 2-Dimensional connectFour
 * game object. Pieces can be placed by pressing buttons, and a new
 * game can be started by going to File --> New Game. Also handles
 * the game logic (order of turns, etc.)
 * @author Justin Kaukonen and Adrian Harrell
 *
 *********************************************************************/
public class ConnectFourPanel extends JPanel implements ActionListener{

	private int SIZE = 10;    
	private JLabel[][] matrix;
	private JButton[] selection;

	private JMenuItem newGameItem;
	private JMenuItem quitItem;
	private ImageIcon iconBlank;
	private ImageIcon iconPlayer1;
	private ImageIcon iconPlayer2;
	
	private JLabel currentTurnLabel;
	private JLabel currentTurnIcon;

	private ConnectFourGame game;
	private boolean AIFlag;

	public ConnectFourPanel(JMenuItem panelQuitItem, JMenuItem 
			panelGameItem){
		// Creates a new game with the AI flag initially set to false.
		game = new ConnectFourGame(SIZE);
		AIFlag = false;
		// Asks the user to set a size for the board.
		try {
			game.setSize(Integer.parseInt(JOptionPane.showInputDialog
					("How many spaces wide/tall should the board be? "
							+ "(5 to 10)")));
		} catch (Exception e) { // If size is not valid, default to 7
			game.setSize(7);
			JOptionPane.showMessageDialog(this, "Invalid input, "
					+ "defaulting to 7 spaces");
		}
		SIZE = game.getSize();

		newGameItem = panelGameItem;
		quitItem = panelQuitItem;

		iconBlank = new ImageIcon ("blank.png");
		iconPlayer1 = new ImageIcon ("player1.png");
		iconPlayer2 = new ImageIcon ("player2.png");

		quitItem.addActionListener(this);
		newGameItem.addActionListener(this);       

		setLayout(new GridBagLayout());
		clearBoard();
	}

	private void clearBoard() {
		// Resets the game itself to all blank spaces.
		game.reset();
		// Removes all old components from the panel.
		removeAll();

		GridBagConstraints loc = new GridBagConstraints();
		
		// Adds JButtons to the Panel for selecting a column.
		selection = new JButton[SIZE];
		for (int col = 0; col < SIZE; col++) {
			selection[col] = new JButton ("Select");
			selection[col].addActionListener(this);

			// Add each element to the GridBagLayout
			loc = new GridBagConstraints();
			loc.gridx = col;
			loc.gridy = 0;
			loc.insets.bottom = 5;
			loc.insets.top = 5;
			add(selection[col], loc);
		}

		matrix = new JLabel[SIZE][SIZE];

		// Adds the tiles for the board.
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				matrix[row][col] = new JLabel("",iconBlank,
						SwingConstants.CENTER);

				// Add each element to the GridBagLayout
				loc = new GridBagConstraints();
				loc.gridx = col;
				loc.gridy = row + 1;
				loc.insets.bottom = 15;
				loc.insets.top = 15;
				add(matrix[row][col], loc);				
			}
		}
		
		// Adds a label and graphic to show who places the next piece.
		currentTurnLabel = new JLabel("Next:");
        loc.gridx = 0;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(currentTurnLabel, loc);
        
        currentTurnIcon = new JLabel("",iconPlayer1,
        		SwingConstants.CENTER);
        loc.gridx = 1;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(currentTurnIcon, loc);
		
        // Updates the panel.
		revalidate();
		repaint();
	}

	public void updateBoard() {
		// Sets every board space to the appropriate color to reflect 
		// the game variables.
		for (int row = SIZE - 1; row >= 0; row--) {
			for (int col = 0; col < SIZE; col++) {
				if (game.getPiece(col, row) == 0) {
					matrix[row][col].setIcon(iconPlayer1);
				} else if (game.getPiece(col, row) == 1) {
					matrix[row][col].setIcon(iconPlayer2);
				} else if (game.getPiece(col, row) == -1) {
					matrix[row][col].setIcon(iconBlank);
				}
			}
		}
	}
	
	//--------------------------------------------------------------
	//  Updates the counter and label when the button is pushed.
	//--------------------------------------------------------------
	public void actionPerformed (ActionEvent event)
	{
		JComponent comp = (JComponent) event.getSource();

		for (int col = 0; col < SIZE; col++) {
			if (selection[col] == comp) {

				int row = game.selectCol(col);
				if (row == -1) // If the column is full
					JOptionPane.showMessageDialog(null, "Col is "
							+ "full!");
				// If it's not full and also player 1's turn
				else if (game.getTurn() == 0) { 
					matrix[row][col].setIcon(iconPlayer1);
					currentTurnIcon.setIcon(iconPlayer2);
					game.setTurn(1);
				// If not AI game, player 2 must have been the one 
				// who took a turn
				} else if (!AIFlag){ 
					matrix[row][col].setIcon(iconPlayer2);
					currentTurnIcon.setIcon(iconPlayer1);
					game.setTurn(0);
				}
				// Check for player 1 win, and if win restart with 
				// player 1's turn next
				if (game.isWinner(0)) { 
					JOptionPane.showMessageDialog(null, "Player " + 
				(game.getTurn() + 1) + " Wins!");
					clearBoard();
					game.setTurn(0);
				} 
				// If row is not full and it is an AI game
				if (row != -1 && AIFlag && game.getTurn() == 1) { 
					game.computerTurn();
					game.setTurn(0);
					updateBoard();
				}
				// Check for player 2/computer win, and if win 
				// restart with player 1's turn next
				if (game.isWinner(1)) { 
					JOptionPane.showMessageDialog(null, "Player " + 
				(game.getTurn() + 1) + " Wins!");
					clearBoard();
					game.setTurn(0);
				}
				// If top space in a row is full, disable the button.
				if (game.spaceOccupied(0, col)) { 
					selection[col].setEnabled(false);
				}
			}
		}

		if (comp == newGameItem) {
			// Sets the size of a the game.
			try {
				game.setSize(Integer.parseInt(JOptionPane.
						showInputDialog("How many spaces wide/tall "
								+ "should the board be? (5 to 10)")));
			} catch (Exception e) {
				game.setSize(7);
				JOptionPane.showMessageDialog(this, "Invalid input, "
						+ "defaulting to 7 spaces");
			}
			SIZE = game.getSize();
			// Resets the game and resets the visuals.
			clearBoard();
		}

		if (comp == quitItem)
			System.exit(1);
	}
	
	public void setAI(boolean flag) {
		AIFlag = flag;
	}
}