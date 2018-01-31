package project2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ConnectFourPanel extends JPanel implements ActionListener{
	
	private final int SIZE = 10;    
	private JLabel[][] matrix;
	private JButton[] selection;
	
	private JMenuItem newGameItem;
	private JMenuItem quitItem;
	private ImageIcon iconBlank;
	private ImageIcon iconPlayer1;
	private ImageIcon iconPlayer2;
	
	private ConnectFourGame game;
	
	public ConnectFourPanel(JMenuItem panelQuitItem, JMenuItem panelGameItem){
		
		game = new ConnectFourGame(10);   
		
		newGameItem = panelGameItem;
		quitItem = panelQuitItem;
		
		setLayout(new GridBagLayout());  // room for top row
		
		iconBlank = new ImageIcon ("blank.png");
		iconPlayer1 = new ImageIcon ("player1.png");
		iconPlayer2 = new ImageIcon ("player2.png");
		
		quitItem.addActionListener(this);
		newGameItem.addActionListener(this);       
		
		setLayout(new GridBagLayout());
        GridBagConstraints loc = new GridBagConstraints();
        
        
		
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
		
		for (int row = 0; row < SIZE - 1; row++) {
			for (int col = 0; col < SIZE; col++) {
				matrix[row][col] = new JLabel("",iconBlank,SwingConstants.CENTER);
				
				// Add each element to the GridBagLayout
		        loc = new GridBagConstraints();
		        loc.gridx = col;
		        loc.gridy = row + 1;
		        loc.insets.bottom = 5;
		        loc.insets.top = 5;
		        add(matrix[row][col], loc);				
			}
		}
	}
	
	private void clearBoard() {
		game.reset();
		for (int row = SIZE; row >= 0; row--) 
			for (int col = 0; col < SIZE; col++) 
				matrix[row][col].setIcon(iconBlank);
	}
	
	private void updateBoard() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (game.getPiece(col, row) == -1) {
					matrix[row][col].setIcon(iconBlank);
				} else if (game.getPiece(col, row) == 0 ) {
					matrix[row][col].setIcon(iconPlayer1);
				} else if (game.getPiece(col, row) == 1 ) {
					matrix[row][col].setIcon(iconPlayer2);
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
				if (row == -1)
					JOptionPane.showMessageDialog(null, "Col is full!");
				else if (game.getTurn() == 0) {
					//matrix[row][col].setIcon((game.getTurn() == 1) ? iconPlayer1 : iconPlayer2);
					matrix[row][col].setIcon(iconPlayer1);
					game.finishTurn();
					if (game.isWinner(1)) {
						JOptionPane.showMessageDialog(null, "Player " + (game.getTurn() + 1) + " Wins!");
						clearBoard();
					}
				} else {
					matrix[row][col].setIcon(iconPlayer2);
					game.finishTurn();
					if (game.isWinner(0)) {
						JOptionPane.showMessageDialog(null, "Player " + (game.getTurn() + 1) + " Wins!");
						clearBoard();
					}
				}
				
				if (row != -1) {
					game.computerTurn();
					updateBoard();
				}
			}
		}
		 
		if (comp == newGameItem) {    
			clearBoard();
		}
		
		if (comp == quitItem)
			System.exit(1);
	}
}