package project2;

public class ConnectFourGame {
	// connect 4 board, it's size x size tiles large
	private int[][] board;
	// total number of rows and columns on the board
	private int size;
	// Whose turn is it?
	private int currentTurn;
	public static final int BLANK = -1;
	public static final int USER = 0;
	public static final int COMPUTER = 1;

	public ConnectFourGame(int pSize) {
		// 10x10 board
		size = 10;
		// Cols, rows
		board = new int[size][(size)];
		// all board is blank
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				board[row][col] = BLANK;
			}	
		}
	}

	// Selects a column to drop a piece into, and it falls to the lowest available space.
	// If no spaces available, returns -1. Otherwise, returns the row it falls to.
	public int selectCol(int col) {		
		for (int row = size - 1; row >= 0; row--) {
			// try to place piece, starting from the bottom (height 0 to size)
			if (!spaceOccupied(row, col)) {
				board[row][col] = getTurn();
				return row;
			}
		}
		return -1;
	}

	// Checks to see if a passed in person is a winner
	public boolean isWinner(int person) {
		// start from bottom left so don't have to check any pieces below
		for (int row = size - 1; row >= 0; row--) {
			for (int col = 0; col < size; col++) {
				// if piece at xy belongs to passed user
				if (board[row][col] == person) {
					// check if 4 in a row, return true or false
					if (checkFourInARow(person, row, col)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// checks for 4 in a row in any direction, see the checkConnections method
	private boolean checkFourInARow(int pieceType, int row, int col) {
		// checks up, right, left, up-right, and up-left, respectively
		if (checkConnections(pieceType, 4, 0, 1, col, row)
				|| checkConnections(pieceType, 4, 1, 0, col, row)
				|| checkConnections(pieceType, 4, 1, 1, col, row)
				|| checkConnections(pieceType, 4, -1, 1, col, row)) {
			return true;
		}
		return false;
	}

	// pieceType is which player you're searching for, connectNum is how many pieces must be in a row to return true
	// horizontalDir and verticalDir are for which direction to check, where 1 is up/right, -1 is left/down, and 0 is no change
	// initialX and initialY and the coordinates to check from, e.g. check (0,0), (0,1), (0,2), and (0,3) would be (player, 4, 0, 1, 0, 0)
	private boolean checkConnections(int pieceType, int connectNum, int horizontalDir, int verticalDir, int initialX, int initialY) {
		int xCoord = initialX;
		int yCoord = initialY;

		for (int numInARow = 0; numInARow < connectNum; numInARow++) {
			if (xCoord >= size || yCoord >= size  || xCoord < 0 || yCoord < 0) {
				return false;
			} else if (board[yCoord][xCoord] != pieceType) {
				return false;
			}
			xCoord += horizontalDir;
			yCoord -= verticalDir;
		} 
		return true;
	}

	public void reset() {
		// reset instance variables to their original values
		size = 10;
		board = new int[size][(size)];
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				board[col][row] = BLANK;
			}

		}
	}

	public void computerTurn() {
		compAttemptWinOrBlock(0);

		if (!isWinner(COMPUTER)) {
			compAttemptWinOrBlock(1);
		}

		// place in a random column (does not work)
		if (getTurn() == COMPUTER) {
			// do AI
		}
	}

	private void compAttemptWinOrBlock(int person) {
		// place in every column. if win, you win. if not, undo.
		for (int col = 0; col < size; col++) {
			// if can drop piece in a row
			if (!spaceOccupied(size, col)) {
				// place and check if win
				currentTurn = person;
				int lastY = selectCol(col);
				if (isWinner(person)) {
					// forces it to place computer's piece
					board[lastY][col] = COMPUTER;
					// if computer won, leave piece there and yay
					// if person would win, place piece there
				} else {
					// undo if no win
					board[lastY][col] = -1;
				}
			}
		}
	}

	// Checks if a given position is occupied
	public boolean spaceOccupied(int row, int col) {
		if (board[row][col] == USER || board[row][col] == COMPUTER) {
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

	// Checks if the space under a coordinate it filled or not.
	public boolean canSupportPiece(int row, int col) {
		if (row < size - 1) {
			if (board[row - 1][col] != -1) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	// get whose turn it is currently
	public int getTurn() {
		return currentTurn;
	}

	public int getPiece(int x, int y) {
		return board[y][x];
	}
}