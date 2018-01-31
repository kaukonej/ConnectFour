package project2;

import java.util.Random;

public class ConnectFourGame {
	// connect 4 board, it's size x size tiles large
	private int[][] board;
	// total number of rows and columns on the board
	private int size;
	// Is the player USER or COMPUTER? You can swap sides. (Cheater)
	private int player;
	// Whose turn is it?
	private int currentTurn;
	public static final int BLANK = -1;
	public static final int USER = 0;
	public static final int COMPUTER = 1;
	
	// col = column, or your x-axis
	// row is your y-axis
	
	public ConnectFourGame(int pSize) {
		// 7x6 board
		size = 10;
		// Cols, rows
		board = new int[size][(size - 1)];
		// all board is blank
		for (int row = 0; row < size - 1; row++) {
			for (int col = 0; col < size; col++) {
				board[col][row] = BLANK;
			}	
		}
		// player defaults to user
		player = USER;
	}
	
	public int selectCol(int col) {		
		for (int row = size - 2; row >= 0; row--) {
			// try to place piece, starting from the bottom (height 0 to size)
			if (!spaceOccupied(col, row)) {
				board[col][row] = getTurn();
				return row;
			}
		}
		return -1;
	}
	
	private boolean spaceOccupied(int col, int row) {
		if (board[col][row] == USER || board[col][row] == COMPUTER) {
			return true;
		} else {
			return false;
		}
	}
	
	// end your turn and allow the other player to take their turn
	public int finishTurn() {
		currentTurn = (currentTurn + 1) % 2;
		return currentTurn;
	}
	
	// switches player to the other side, idk what I'm supposed to return
	public void switchPlayer() {
		player = (player + 1) % 2;
	}
	
	// get whose turn it is currently
	public int getTurn() {
		return currentTurn;
	}
	
	public void reset() {
		// reset instance variables to their original values
		size = 10;
		board = new int[size][(size - 1)];
		for (int row = 0; row < size - 1; row++) {
			for (int col = 0; col < size; col++) {
				board[col][row] = BLANK;
			}
			
		}
		player = 0;
	}
	
	public boolean isWinner(int person) {
		// start from bottom left so don't have to check any pieces below
		for (int row = size - 2; row >= 0; row--) {
			for (int col = 0; col < size; col++) {
				// if piece at xy belongs to passed user
				if (board[col][row] == person) {
					// check if 4 in a row, return true or false
					if (checkFourInARow(person, col, row)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean checkFourInARow(int pieceType, int col, int row) {
		int piece = pieceType;
		int initialX = col;
		int initialY = row;
		// check up
		if (initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX][initialY - 1] == piece && board[initialX][initialY - 2] == piece &&
					board[initialX][initialY - 3] == piece) {
				return true;
			}
		}
		
		// check right
		if (initialX <= (size - 4)) {
			if (board[initialX][initialY] == piece && board[initialX + 1][initialY] == piece && board[initialX + 2][initialY] == piece &&
					board[initialX + 3][initialY] == piece) {
				return true;
			}
		}
		
		// check left
		if (initialX >= 3) {
			if (board[initialX][initialY] == piece && board[initialX - 1][initialY] == piece && board[initialX - 2][initialY] == piece &&
					board[initialX - 3][initialY] == piece) {
				return true;
			}
		}
		
		// check up-right
		if (initialX <= (size - 4) && initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX + 1][initialY - 1] == piece && board[initialX + 2][initialY - 2] == piece &&
					board[initialX + 3][initialY - 3] == piece) {
				return true;
			}
		}
		
		if (initialX >= 4 && initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX - 1][initialY - 1] == piece && board[initialX - 2][initialY - 2] == piece &&
					board[initialX - 3][initialY - 3] == piece) {
				return true;
			}
		}
		
		return false;
	}
	
	private void placePieceComp(int pieceType, int col, int row) {
		int piece = pieceType;
		int initialX = col;
		int initialY = row;
		// check up
		if (initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX][initialY - 1] == piece && board[initialX][initialY - 2] == piece &&
					board[initialX][initialY - 3] == -1) {
				selectCol(initialX);
			}
		}
		
		// check right
		if (initialX <= (size - 4)) {
			if (board[initialX][initialY] == piece && board[initialX + 1][initialY] == piece && board[initialX + 2][initialY] == piece &&
					board[initialX + 3][initialY] == -1) {
				selectCol(initialX + 3);
			}
		}
		
		// check left
		if (initialX >= 3) {
			if (board[initialX][initialY] == piece && board[initialX - 1][initialY] == piece && board[initialX - 2][initialY] == piece &&
					board[initialX - 3][initialY] == -1) {
				selectCol(initialX - 3);
			}
		}
		
		// check up-right
		if (initialX <= (size - 4) && initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX + 1][initialY - 1] == piece && board[initialX + 2][initialY - 2] == piece &&
					board[initialX + 3][initialY - 3] == -1) {
				selectCol(initialX + 3);
			}
		}
		
		// check up-left
		if (initialX >= 3 && initialY >= 3) {
			if (board[initialX][initialY] == piece && board[initialX - 1][initialY - 1] == piece && board[initialX - 2][initialY - 2] == piece &&
					board[initialX - 3][initialY - 3] == -1) {
				selectCol(initialX - 3);
			}
		}
	}
	
	public void computerTurn() {
		// try to win
		for (int row = size - 2; row >= 0; row--) {
			for (int col = 0; col < size; col++) {
				placePieceComp(getTurn(), col, row);
			}
		}
		// try to block
		if (!isWinner(COMPUTER)) {
			for (int row = size - 2; row >= 0; row--) {
				for (int col = 0; col < size; col++) {
					placePieceComp(player, col, row);
				}
			}
		}
		// place in a random column
		if (getTurn() == COMPUTER) {
			Random rand = new Random();
			selectCol(rand.nextInt(size - 1));
		}
	}
	
	public int getPiece(int x, int y) {
		return board[x][y];
	}
}
	

