package project2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

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

	public ConnectFourPanel(JMenuItem panelQuitItem, JMenuItem panelGameItem){
		game = new ConnectFourGame(SIZE);
		AIFlag = false;
		try {
			game.setSize(Integer.parseInt(JOptionPane.showInputDialog("How many spaces wide/tall should the board be? (5 to 10)")));
		} catch (Exception e) {
			game.setSize(7);
			JOptionPane.showMessageDialog(this, "Invalid input, defaulting to 7 spaces");
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
		game.reset();
		removeAll();

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
		
		revalidate();
		repaint();
	}

	public void updateBoard() {
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
				if (row == -1) // if column full
					JOptionPane.showMessageDialog(null, "Col is full!");
				else if (game.getTurn() == 0) { // if not full and p1 turn
					matrix[row][col].setIcon(iconPlayer1);
					currentTurnIcon.setIcon(iconPlayer2);
					game.setTurn(1);
				} else if (!AIFlag){ // if not AI game, p2 must have been the one who took a turn
					matrix[row][col].setIcon(iconPlayer2);
					currentTurnIcon.setIcon(iconPlayer1);
					game.setTurn(0);
				}
				if (row != -1 && AIFlag) { // if not full and it is an AI game
					game.computerTurn();
					game.setTurn(0);
					updateBoard();
				}
				if (game.isWinner(0)) { // check for winner, and if win restart with p1's turn next
					JOptionPane.showMessageDialog(null, "Player " + (game.getTurn() + 1) + " Wins!");
					clearBoard();
					game.setTurn(0);
				} else if (game.isWinner(1)) {
					JOptionPane.showMessageDialog(null, "Player " + (game.getTurn() + 1) + " Wins!");
					clearBoard();
					game.setTurn(0);
				}
				
				if (game.spaceOccupied(0, col)) {
					selection[col].setEnabled(false);
				}
			}
		}

		if (comp == newGameItem) {
			try {
				game.setSize(Integer.parseInt(JOptionPane.showInputDialog("How many spaces wide/tall should the board be? (5 to 10)")));
			} catch (Exception e) {
				game.setSize(7);
				JOptionPane.showMessageDialog(this, "Invalid input, defaulting to 7 spaces");
			}
			SIZE = game.getSize();
			clearBoard();
		}

		if (comp == quitItem)
			System.exit(1);
	}
	
	public void setAI(boolean flag) {
		AIFlag = flag;
	}
}