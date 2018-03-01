package project2;

public class ConnectFourGame3D {
	/** The Connect Four board, with rows and columns for coordinates, 
	 * respectively. */
	private int[][][] board;
	
	/** The size of the board; the total number of columns and rows. */
	private int size;
	
	/** Whose turn it is. If 0, it's the user's turn. If 1, it's the 
	 * computer's turn. */
	private int currentTurn;
	
	private int currentDepth;
	
	/** Represents a blank piece on the board. */
	public static final int BLANK = -1;
	
	/** Represents a player 1-occupied piece on the board. */
	public static final int PLAYER1 = 0;
	
	/** Represents a player 2-occupied piece on the board. */
	public static final int PLAYER2 = 1;
	
	/******************************************************************
	 * Constructor for a ConnectFourGame. Creates a board of pSize size, 
	 * and sets every space in it to be blank.
	 * @param pSize The size of the board; the total number of rows and 
	 * columns.
	 *****************************************************************/
	public ConnectFourGame3D(int pSize) {
		// 10x10 board
		size = 10;
		board = new int[size][size][size];
		currentDepth = 0;
		// all board is blank
		for (int depth = 0; depth < size; depth++) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					board[row][col][depth] = BLANK;
				}	
			}
		}
	}

	/******************************************************************
	 * Selects a column to drop a piece into, and it falls to the 
	 * lowest available space.
	 * @param col The selected column to drop a piece into.
	 * @return If no spaces are available, return -1. Else, return the 
	 * value of the row that the piece was placed in.
	 *****************************************************************/
	public int selectCol(int col) {		
		for (int row = size - 1; row >= 0; row--) {
			// try to place piece, starting from the bottom (height 0 to size)
			if (!spaceOccupied(row, col, currentDepth)) {
				board[row][col][currentDepth] = getTurn();
				return row;
			}
		}
		return -1;
	}

	/******************************************************************
	 * Checks to see if a given player has won the game.
	 * @param person The person to be checked if they won the game.
	 * @return True if they have a winning move, false if not.
	 *****************************************************************/
	public boolean isWinner(int person) {
		// start from bottom left so don't have to check any pieces below
		for (int depth = 0; depth < size; depth++) {
			for (int row = size - 1; row >= 0; row--) {
				for (int col = 0; col < size; col++) {
					// if piece at xy belongs to passed user
					if (board[row][col][depth] == person) {
						// check if 4 in a row, return true or false
						if (checkFourInARow(person, row, col, depth)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/******************************************************************
	 * Checks for 4 pieces in a row of a given type, starting from a 
	 * given coordinate.
	 * @param pieceType Type of piece to check for 4 in a row of.
	 * @param row Starting coordinate for row.
	 * @param col Starting coordinate for column.
	 * @param depth Starting coordinate for depth.
	 * @return True if 4 in a row in any direction, false otherwise.
	 *****************************************************************/
	private boolean checkFourInARow(int pieceType, int row, int col, int depth) {
		// checks up connect four win
		if (checkConnections(pieceType, 4, 0, 1, 0, col, row, depth)
				// upright upleft
				|| checkConnections(pieceType, 4, 1, 1, 0, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, 0, col, row, depth)
				// uprightback upleftback
				|| checkConnections(pieceType, 4, 1, 1, 1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, 1, col, row, depth)
				// upback
				|| checkConnections(pieceType, 4, 0, 1, 1, col, row, depth)
				// uprightforward upleftforward
				|| checkConnections(pieceType, 4, 1, 1, -1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, -1, col, row, depth)
				// upforward
				|| checkConnections(pieceType, 4, 0, 1, -1, col, row, depth)
				// horizontalX horizontalZ
				|| checkConnections(pieceType, 4, 1, 0, 0, col, row, depth)
				|| checkConnections(pieceType, 4, 0, 0, 1, col, row, depth)
				// diagonal right, diagonal left
				|| checkConnections(pieceType, 4, 1, 0, 1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 0, 1, col, row, depth)) {
			return true;
		}
		return false;
	}

	// pieceType is which player you're searching for, connectNum is how many pieces must be in a row to return true
	// horizontalDir and verticalDir are for which direction to check, where 1 is up/right, -1 is left/down, and 0 is no change
	// initialX and initialY and the coordinates to check from, e.g. check (0,0), (0,1), (0,2), and (0,3) would be (player, 4, 0, 1, 0, 0)
//	private boolean checkConnections(int pieceType, int connectNum, int horizontalDir, int verticalDir, int depthDir, int initialX, int initialY, int initialZ) {
//		int xCoord = initialX;
//		int yCoord = initialY;
//		int zCoord = initialZ;
//		for (int numInARow = 0; numInARow < connectNum; numInARow++) {
//			if (board[yCoord][xCoord][zCoord] != pieceType) {
//				return false;
//				// TO DO: Check if Z-direction needs to be modified
//			} else if (numInARow == connectNum - 1 && yCoord == 0) {
//				return true;
//			} else if (xCoord + horizontalDir < size && yCoord - 
//					verticalDir < size && xCoord + horizontalDir 
//					>= 0 && yCoord - verticalDir >= 0 && zCoord 
//					+ depthDir < size && zCoord + depthDir >= 0) {
//				xCoord += horizontalDir;
//				yCoord -= verticalDir;
//				zCoord += depthDir;
//			} else {
//				return false;
//			}
//		}
//		return true;
//	}
	
	/******************************************************************
	 * Checks if a given piece type has a given number of connections 
	 * in a given direction from a given starting point. (It's a given.)
	 * @param pieceType The type of piece to check.
	 * @param connectNum How many pieces in a row are needed to return 
	 * true.
	 * @param horizontalDir How many spaces right/left to move to check 
	 * for another piece. (-1 is left, 1 is right, 0 is don't move.)
	 * @param verticalDir How many spaces up/down to move to check for 
	 * another piece. (-1 is down, 1 is up, 0 is don't move.)
	 * @param depthDir How many spaces ahead/behind to move to check for 
	 * another piece. (-1 is behind, 1 is ahead, 0 is don't move.)
	 * @param initialX Starting coordinate to check from for columns.
	 * @param initialY Starting coordinate to check from for rows.
	 * @param initialZ Starting coordinate to check from for depth.
	 * @return True if there are connectNum of pieceType pieces in a 
	 * row in the given direction from the given coordinate, false 
	 * otherwise.
	 *****************************************************************/
	private boolean checkConnections(int pieceType, int connectNum, int horizontalDir, int verticalDir, int depthDir, int initialX, int initialY, int initialZ) {
		int xCoord = initialX;
		int yCoord = initialY;
		int zCoord = initialZ;
		for (int numInARow = 0; numInARow < connectNum; numInARow++) {
			if (xCoord >= size || yCoord >= size || zCoord >= size || xCoord < 0 || yCoord < 0 || zCoord < 0) {
				return false;
			} else if (board[yCoord][xCoord][zCoord] != pieceType) {
				return false;
			}
			xCoord += horizontalDir;
			yCoord -= verticalDir;
			zCoord += depthDir;
		} 
		return true;
	}

	/******************************************************************
	 * Resets the board to all blank pieces, and if the size has changed 
	 * it updates the size.
	 *****************************************************************/
	public void reset() {
		// reset instance variables to their original values
		size = 10;
		currentDepth = 0;
		board = new int[size][size][size];
		for (int depth = 0; depth < size; depth++) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					board[row][col][depth] = BLANK;
				}	
			}
		}
	}

	/******************************************************************
	 * Checks to see if a given coordinate is occupied by either the 
	 * player or computer.
	 * @param row Row to check to see if occupied.
	 * @param col Column to check to see if occupied.
	 * @param depth Depth to check to see if occupied.
	 * @return True if the coordinate is occupied, false if it is not.
	 *****************************************************************/
	private boolean spaceOccupied(int row, int col, int depth) {
		if (board[row][col][currentDepth] == PLAYER1 || board[row][col][currentDepth] == PLAYER2) {
			return true;
		} else {
			return false;
		}
	}

	/******************************************************************
	 * End your turn and allow the other player to take their turn.
	 * @return The current player's turn.
	 *****************************************************************/
	public int finishTurn() {
		currentTurn = (currentTurn + 1) % 2;
		return currentTurn;
	}
	
	/******************************************************************
	 * Returns the piece at a given coordinate on the board.
	 * @param x Column to check for a piece.
	 * @param y Row to check for a piece.
	 * @param z Depth to check for a piece.
	 * @return What value is at the given coordinate. (0 for player, 
	 * 1 for computer, or -1 for empty.)
	 *****************************************************************/
	public int getPiece(int x, int y, int z) {
		return board[y][x][z];
	}

	/******************************************************************
	 * Gets the currentTurn.
	 * @return Whose turn it currently is. (0 for player, 1 for 
	 * computer.)
	 *****************************************************************/
	public int getTurn() {
		return currentTurn;
	}
	
	/******************************************************************
	 * Gets the currentDepth that is being displayed to the panel.
	 * @return The current board Depth.
	 *****************************************************************/
	public int getCurrentDepth() {
		return currentDepth;
	}

	/******************************************************************
	 * Sets the currentDepth that is being displayed to the panel.
	 * @return The current board Depth.
	 *****************************************************************/
	public void setCurrentDepth(int depth) {
		if (!(depth < 0) && !(depth >= size)) {
			currentDepth = depth;
		}
	}
}