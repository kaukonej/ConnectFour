package project2;

public class ConnectFourGame3D {
	// connect 4 board, it's size x size x size tiles large
	private int[][][] board;
	// total number of rows and columns on the board
	private int size;
	// Whose turn is it?
	private int currentTurn;
	private int currentDepth;
	public static final int BLANK = -1;
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;

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

	// Selects a column to drop a piece into, and it falls to the lowest available space.
	// If no spaces available, returns -1. Otherwise, returns the row it falls to.
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

	// Checks to see if a passed in person is a winner
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

	// checks for 4 in a row in any direction, see the checkConnections method
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

	// Checks if a given position is occupied
	private boolean spaceOccupied(int row, int col, int depth) {
		if (board[row][col][currentDepth] == PLAYER1 || board[row][col][currentDepth] == PLAYER2) {
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

	public int getPiece(int x, int y, int z) {
		return board[y][x][z];
	}

	// get whose turn it is currently
	public int getTurn() {
		return currentTurn;
	}

	public int getCurrentDepth() {
		return currentDepth;
	}

	public void setCurrentDepth(int depth) {
		if (!(depth < 0) && !(depth >= size)) {
			currentDepth = depth;
		}
	}
}