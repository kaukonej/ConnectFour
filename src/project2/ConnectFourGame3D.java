package project2;

import java.util.Random;

public class ConnectFourGame3D {
	// connect 4 board, it's size x size tiles large
	private int[][][] board;
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
	private int currentDepth;
	
	public ConnectFourGame3D(int pSize) {
		// 7x6 board
		size = 10;
		// Cols, rows
		board = new int[size][size][size];
		// all board is blank
		for (int depth = 0; depth < size; depth++) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					board[row][col][depth] = BLANK;
				}	
			}
		}
		// player defaults to user
		player = USER;
	}
	
	// Selects a column to drop a piece into, and it falls to the lowest available space.
	// If no spaces available, returns -1. Otherwise, returns the row it falls to.
	public int selectCol(int col) {		
		// TO DO: Use currentDepth, or pass it in?
		for (int row = size - 1; row >= 0; row--) {
			// try to place piece, starting from the bottom (height 0 to size)
			if (!spaceOccupied(row, col, currentDepth)) {
				board[row][col][currentDepth] = getTurn();
				return row;
			}
		}
		return -1;
	}
	
	// Checks if a given position is occupied
	private boolean spaceOccupied(int row, int col, int depth) {
		if (board[row][col][currentDepth] == USER || board[row][col][currentDepth] == COMPUTER) {
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
		board = new int[size][size][size];
		for (int depth = 0; depth < size; depth++) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					board[col][row][depth] = BLANK;
				}	
			}
		}
		player = 0;
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
		// checks up, right, up-right, and up-left, respectively
		// then check for forward right, forward left, back right, back left diaganols
		// then check vertical forward right, forward left, back right, back left diaganols
		// then check only depth connect 4
		// TO DO: Add depthDir parameter to all checks
		if (checkConnections(pieceType, 4, 0, 1, 0, col, row, depth)
				|| checkConnections(pieceType, 4, 1, 0, 0, col, row, depth)
				|| checkConnections(pieceType, 4, 1, 1, 0, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, 0, col, row, depth)
				
				|| checkConnections(pieceType, 4, 1, 0, 1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 0, 1, col, row, depth)
				|| checkConnections(pieceType, 4, 1, 0, -1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 0, -1, col, row, depth)
				
				|| checkConnections(pieceType, 4, 1, 1, 1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, 1, col, row, depth)
				|| checkConnections(pieceType, 4, 1, 1, -1, col, row, depth)
				|| checkConnections(pieceType, 4, -1, 1, -1, col, row, depth)
				
				|| checkConnections(pieceType, 4, 0, 0, 1, col, row, depth)) {
			return true;
		}
		return false;
	}
	
	// pieceType is which player you're searching for, connectNum is how many pieces must be in a row to return true
	// horizontalDir and verticalDir are for which direction to check, where 1 is up/right, -1 is left/down, and 0 is no change
	// initialX and initialY and the coordinates to check from, e.g. check (0,0), (0,1), (0,2), and (0,3) would be (player, 4, 0, 1, 0, 0)
	private boolean checkConnections(int pieceType, int connectNum, int horizontalDir, int verticalDir, int depthDir, int initialX, int initialY, int initialZ) {
		int xCoord = initialX;
		int yCoord = initialY;
		int zCoord = initialZ;
		for (int numInARow = 0; numInARow < connectNum; numInARow++) {
			if (board[yCoord][xCoord][zCoord] != pieceType) {
				return false;
			} else if (numInARow == connectNum - 1 && yCoord == 0) {
				return true;
			} else if (xCoord + horizontalDir < size && yCoord - 
					verticalDir < size && xCoord + horizontalDir 
					>= 0 && yCoord - verticalDir >= 0 && zCoord 
					+ depthDir < size && zCoord + depthDir >= 0) {
				xCoord += horizontalDir;
				yCoord -= verticalDir;
				zCoord += depthDir;
			} else {
				return false;
			}
		}
		return true;
	}
	
	// Tries to place piece anywhere where there's 3 in a row, followed by an open space where a piece can be placed
	// open place must also have a piece below it to "support" the piece, keep it from "floating"
//	private void placePieceComp(int pieceType, int row, int col, int depth) {
//		// up, right, up right, left, and up left
//		// TO DO: Add depthDir
//		// TO DO: Make it change depth as it checks? Or add a last move location? idk
//		// TO DO: Fix just everything I guess
//		
//		// if three in a row (up), and can place a fourth, do it
//		if (checkConnections(pieceType, 3, 0, 1, 0, col, row, depth) && 
//				!spaceOccupied(row, col, depth)) {
//			selectCol(col);
//		// if three in a row (right), and can place a fourth, do it
//		} else if ((checkConnections(pieceType, 3, 1, 0, 0, col, row, depth) && 
//				spaceOccupied(row + 1, col + 3, depth) && (!spaceOccupied(row, 
//				col + 3, depth))) || (checkConnections(pieceType, 3, 1, 1, 0, col, 
//				row, depth) && spaceOccupied(row - 2, col + 3, depth) && !spaceOccupied(row 
//				- 3, col + 3, depth))) {
//			selectCol(col + 3);
//		// if three in a row (left), and can place a fourth, do it
//		} else if ((checkConnections(pieceType, 3, -1, 0, DEPTHDIR, col, row, depth) && 
//				spaceOccupied(row + 1, col - 3, DEPTH + ?) && !spaceOccupied(row, 
//				col - 3, DEPTH + ?)) || (checkConnections(pieceType, 3, -1, 1, DEPTHDIR, 
//				col, row, depth) && spaceOccupied(row - 2, col - 3, DEPTH + ?) && 
//				!spaceOccupied(row - 3, col - 3, DEPTH + ?))) {
//			selectCol(col - 3);
//		}	
//	}
	
	private void placePieceCompAlt(int person) {
		// place in every column. if win, you win. if not, undo.
		for (int depth = 0; depth < size; depth++) {
			for (int col = 0; col < size; depth++) {
				// if can drop piece in a row
				if (!spaceOccupied(size, col, depth)) {
					// place and check if win
					setTurn(person);
					int lastY = selectCol(col);
					if (isWinner(person)) {
						// forces it to place computer's piece
						setTurn(player + 1);
						board[lastY][col][depth] = getTurn();
						// if computer won, leave piece there and yay
						// if person would win, place piece there
					} else {
						// undo if no win
						board[lastY][col][depth] = -1;
					}
				}
			}
		}
	}
	
	private void setTurn(int turn) {
		currentTurn = turn % 2;
	}
	
	public void computerTurn() {
		// try to win
		placePieceCompAlt((player + 1) % 2);
		
		// try to block
		if (!isWinner(COMPUTER)) {
			placePieceCompAlt(player);
		}
		
		// place in a random column (does not work)
		if (getTurn() == COMPUTER) {
			Random rand = new Random();
			selectCol(rand.nextInt(size - 1));
		}
	}
	
	public int getPiece(int x, int y, int z) {
		return board[y][x][z];
	}
}
	
//// Checks if the space under a coordinate it filled or not.
//private boolean canSupportPiece(int row, int col) {
//	if (row < size - 1) {
//		if (board[row - 1][col] != -1) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	return true;
//}

//private boolean checkFourInARow(int pieceType, int row, int col) {
//	int piece = pieceType;
//	int initialY = row;
//	int initialX = col;
//	// check up
//	if (initialY >= 3) {
//		if (board[initialY][initialX] == piece && board[initialY - 1][initialX] == piece && board[initialY - 2][initialX] == piece &&
//				board[initialY - 3][initialX] == piece) {
//			return true;
//		}
//	}
//	
//	// check right
//	if (initialX <= (size - 3)) {
//		if (board[initialY][initialX] == piece && board[initialY][initialX + 1] == piece && board[initialY][initialX + 2] == piece &&
//				board[initialY][initialX + 3] == piece) {
//			return true;
//		}
//	}
//	
//	// check left
//	if (initialX >= 3) {
//		if (board[initialY][initialX] == piece && board[initialY][initialX - 1] == piece && board[initialY][initialX - 2] == piece &&
//				board[initialY][initialX - 3] == piece) {
//			return true;
//		}
//	}
//	
//	// check up-right
//	if (initialX <= (size - 3) && initialY >= 3) {
//		if (board[initialY][initialX] == piece && board[initialY - 1][initialX + 1] == piece && board[initialY - 2][initialX + 2] == piece &&
//				board[initialY - 3][initialX + 3] == piece) {
//			return true;
//		}
//	}
//	
//	// check up-left
//	if (initialX >= 3 && initialY >= 3) {
//		if (board[initialY][initialX] == piece && board[initialY - 1][initialX - 1] == piece && board[initialY - 2][initialX - 2] == piece &&
//				board[initialY - 3][initialX - 3] == piece) {
//			return true;
//		}
//	}
//	return false;
//}

//	private void placePieceComp(int pieceType, int row, int col) {
//		int piece = pieceType;
//		int initialY = row;
//		int initialX = col;
//		// check up
//		if (initialY >= 3) {
//			if (board[initialY][initialX] == piece && board[initialY - 1][initialX] == piece && board[initialY - 2][initialX] == piece &&
//					board[initialY - 3][initialX] == -1) {
//					selectCol(initialX);
//			}
//		}
//		
//		// check right
//		else if (initialX <= (size - 4)) {
//			if (board[initialY][initialX] == piece && board[initialY][initialX + 1] == piece && board[initialY][initialX + 2] == piece &&
//					board[initialY][initialX + 3] == -1) {
//				if (canSupportPiece(initialY, initialX + 3)) {
//					selectCol(initialX + 3);
//				}
//			}
//		}
//		
//		// check left
//		else if (initialX >= 3) {
//			if (board[initialY][initialX] == piece && board[initialY][initialX - 1] == piece && board[initialY][initialX - 2] == piece &&
//					board[initialY][initialX - 3] == -1) {
//				if (canSupportPiece(initialY, initialX - 3)) {
//					selectCol(initialX - 3);
//				}
//			}
//		}
//		
//		// check up-right
//		else if (initialX <= (size - 4) && initialY >= 3) {
//			if (board[initialY][initialX] == piece && board[initialY - 1][initialX + 1] == piece && board[initialY - 2][initialX + 2] == piece &&
//					board[initialY - 3][initialX + 3] == -1) {
//				if (canSupportPiece(initialY - 3, initialX + 3)) {
//					selectCol(initialX + 3);
//				}
//			}
//		}
//		
//		// check up-left
//		else if (initialX >= 3 && initialY >= 3) {
//			if (board[initialY][initialX] == piece && board[initialY - 1][initialX - 1] == piece && board[initialY - 2][initialX - 2] == piece &&
//					board[initialY - 3][initialX - 3] == -1) {
//				if (canSupportPiece(initialY - 3, initialX - 3)) {
//					selectCol(initialX - 3);
//				}
//			}
//		}
//	}