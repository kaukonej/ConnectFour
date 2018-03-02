package project2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**********************************************************************
 * 
 * A panel which contains and displays a 3-Dimensional connectFour
 * game object. Pieces can be placed by pressing buttons, and a new
 * game can be started by going to File --> New Game. Also handles
 * the game logic (order of turns, etc). Displays one "depth" of the
 * board at a time, which can be cycled through with buttons.
 * @author Justin Kaukonen and Adrian Harrell
 *
 *********************************************************************/
public class ConnectFourPanel3D extends JPanel implements ActionListener 
{	
	// Game will always be size 10
	private final int SIZE = 10;    
	private JLabel[][] matrix;
	private JButton[] selection;
	private JButton leftButton;
	private JButton rightButton;
	private JLabel depthLabel;
	
	private JLabel currentTurnLabel;
	private JLabel currentTurnIcon;
	
	private JMenuItem newGameItem;
	private JMenuItem quitItem;
	private ImageIcon iconBlank;
	private ImageIcon iconPlayer1;
	private ImageIcon iconPlayer2;
	
	private ConnectFourGame3D game3D;
	
	public ConnectFourPanel3D(JMenuItem panelQuitItem, JMenuItem 
			panelGameItem){
		game3D = new ConnectFourGame3D(SIZE);
		
		newGameItem = panelGameItem;
		quitItem = panelQuitItem;
		
		iconBlank = new ImageIcon ("blank.png");
		iconPlayer1 = new ImageIcon ("player1.png");
		iconPlayer2 = new ImageIcon ("player2.png");
		
		quitItem.addActionListener(this);
		newGameItem.addActionListener(this);       
		
		setLayout(new GridBagLayout());
        GridBagConstraints loc = new GridBagConstraints();
		
        // Add selection buttons to GUI
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
		// Adds tiles to the board
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
		
		// Adds left/right buttons to cycle through the layers
		leftButton = new JButton("<");
		leftButton.addActionListener(this);
		loc = new GridBagConstraints();
        loc.gridx = SIZE - 1;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(leftButton, loc);	
        
		rightButton = new JButton(">");
		rightButton.addActionListener(this);
		loc = new GridBagConstraints();
        loc.gridx = SIZE;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(rightButton, loc);	
		
        // Adds label that shows current depth displayed on GUI
		depthLabel = new JLabel("Layer 1");
		loc.gridx = SIZE - 2;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(depthLabel, loc);
        
        // Adds label and icon that shows who goes next
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
	}
	
	private void updateBoard() {
		// Updates the GUI board to reflect the game board
		for (int row = SIZE - 1; row >= 0; row--)  {
			for (int col = 0; col < SIZE; col++) {
				if (game3D.getPiece(col, row, game3D.getCurrentDepth())
						== 0) {
					matrix[row][col].setIcon(iconPlayer1);
				} else if (game3D.getPiece(col, row, game3D.
						getCurrentDepth()) == 1) {
					matrix[row][col].setIcon(iconPlayer2);
				} else {
					matrix[row][col].setIcon(iconBlank);
				}			
			}
		}
	}
	
	private void clearBoard() {
		// Reset the board to be blank again
		game3D.reset();
		game3D.setCurrentDepth(0);
		depthLabel.setText("Layer " + (game3D.getCurrentDepth() + 1));
		currentTurnIcon.setIcon(iconPlayer1);
		for (int row = 0; row < SIZE; row++) 
			for (int col = 0; col < SIZE; col++) 
				matrix[row][col].setIcon(iconBlank);
	}
	
	//--------------------------------------------------------------
	//  Updates the counter and label when the button is pushed.
	//--------------------------------------------------------------
	public void actionPerformed (ActionEvent event)
	{
		
		JComponent comp = (JComponent) event.getSource();
		
		for (int col = 0; col < SIZE; col++) {
			if (selection[col] == comp) {
				int row = game3D.selectCol(col);
				if (row == -1) // IF column is full
					JOptionPane.showMessageDialog(null, "Col is full!");
				// If it's player 1's turn
				else if (game3D.getTurn() == 0) { 
					matrix[row][col].setIcon(iconPlayer1);
					if (game3D.isWinner(0)) { // Check for player 1 win
						JOptionPane.showMessageDialog(null, "Player 1 "
								+ "Wins!");
						clearBoard();
					}
					game3D.finishTurn();
					currentTurnIcon.setIcon(iconPlayer2);
				} else { // If it's player 2's turn
					matrix[row][col].setIcon(iconPlayer2);
					if (game3D.isWinner(1)) { // Check for player 2 win
						JOptionPane.showMessageDialog(null, "Player 2 "
								+ "Wins!");
						clearBoard();
					}
					game3D.finishTurn();
					currentTurnIcon.setIcon(iconPlayer1);
				}
			}
		}
		
		// If current layer displayed is leftmost layer, do not allow 
		// to get more left
		if (game3D.getCurrentDepth() > 0) {
			if (comp == leftButton) {
				game3D.setCurrentDepth(game3D.getCurrentDepth() - 1);
				depthLabel.setText("Layer " + (game3D.getCurrentDepth()
						+ 1));
				updateBoard();
			}
		}
		
		// If current layer displayed is rightmost layer, do not allow 
		// to get more right
		if (game3D.getCurrentDepth() < SIZE) {
			if (comp == rightButton) {
				game3D.setCurrentDepth(game3D.getCurrentDepth() + 1);
				depthLabel.setText("Layer " + (game3D.getCurrentDepth() 
						+ 1));
				updateBoard();
			}
		}
		 
		if (comp == newGameItem) {    
			clearBoard();
		}
		
		if (comp == quitItem)
			System.exit(1);
	}
	
	
}