package project2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ConnectFourPanel3D extends JPanel implements ActionListener {
	
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
	
	public ConnectFourPanel3D(JMenuItem panelQuitItem, JMenuItem panelGameItem){
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
		
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				matrix[row][col] = new JLabel("",iconBlank,SwingConstants.CENTER);
				
				// Add each element to the GridBagLayout
		        loc = new GridBagConstraints();
		        loc.gridx = col;
		        loc.gridy = row + 1;
		        loc.insets.bottom = 15;
		        loc.insets.top = 15;
		        add(matrix[row][col], loc);				
			}
		}
		
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
		
		depthLabel = new JLabel("Layer 1");
		loc.gridx = SIZE - 2;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(depthLabel, loc);
        
        currentTurnLabel = new JLabel("Next:");
        loc.gridx = 0;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(currentTurnLabel, loc);
        
        currentTurnIcon = new JLabel("",iconPlayer1,SwingConstants.CENTER);
        loc.gridx = 1;
        loc.gridy = SIZE + 2;
        loc.insets.bottom = 15;
        loc.insets.top = 15;
        add(currentTurnIcon, loc);
	}
	
	private void updateBoard() {
		for (int row = SIZE - 1; row >= 0; row--)  {
			for (int col = 0; col < SIZE; col++) {
				if (game3D.getPiece(col, row, game3D.getCurrentDepth()) == 0) {
					matrix[row][col].setIcon(iconPlayer1);
				} else if (game3D.getPiece(col, row, game3D.getCurrentDepth()) == 1) {
					matrix[row][col].setIcon(iconPlayer2);
				} else {
					matrix[row][col].setIcon(iconBlank);
				}			
			}
		}
	}
	
	private void clearBoard() {
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
				if (row == -1)
					JOptionPane.showMessageDialog(null, "Col is full!");
				else if (game3D.getTurn() == 0) {
					//matrix[row][col].setIcon((game.getTurn() == 1) ? iconPlayer1 : iconPlayer2);
					matrix[row][col].setIcon(iconPlayer1);
					if (game3D.isWinner(0)) { //PLAYER1
						JOptionPane.showMessageDialog(null, "Player 1 Wins!");
						clearBoard();
					}
					game3D.finishTurn();
					currentTurnIcon.setIcon(iconPlayer2);
				} else {
					matrix[row][col].setIcon(iconPlayer2);
					if (game3D.isWinner(1)) { // PLAYER2
						JOptionPane.showMessageDialog(null, "Player 2 Wins!");
						clearBoard();
					}
					game3D.finishTurn();
					currentTurnIcon.setIcon(iconPlayer1);
				}
			}
		}
		
		if (game3D.getCurrentDepth() > 0) {
			if (comp == leftButton) {
				game3D.setCurrentDepth(game3D.getCurrentDepth() - 1);
				depthLabel.setText("Layer " + (game3D.getCurrentDepth() + 1));
				updateBoard();
			}
		}
		
		if (game3D.getCurrentDepth() < SIZE) {
			if (comp == rightButton) {
				game3D.setCurrentDepth(game3D.getCurrentDepth() + 1);
				depthLabel.setText("Layer " + (game3D.getCurrentDepth() + 1));
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