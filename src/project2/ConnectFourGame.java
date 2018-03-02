package project2;

import java.util.Random;
/**********************************************************************
 * 
 * A 2-Dimensional game of Connect Four. Pieces can be dropped into a
 * column, and it can check to see if a given number of pieces are in
 * a row. If 4 pieces are in a row, the player who is control of those
 * pieces wins. The board can also vary in size.
 * @author Justin Kaukonen and Adrian Harrell
 *
 *********************************************************************/
public class ConnectFourGame {
	/** The Connect Four board, with rows and columns for coordinates, 
	 * respectively. */
	private int[][] board;

	/** The size of the board; the total number of columns and rows. */
	private int size;

	/** Whose turn it is. If 0, it's the user's turn. If 1, it's the 
	 * computer's turn. */
	private int currentTurn;

	/** Represents a blank piece on the board. */
	public static final int BLANK = -1;

	/** Represents a user-occupied piece on the board. */
	public static final int USER = 0;

	/** Represents a computer-occupied piece on the board. */
	public static final int COMPUTER = 1;

	/******************************************************************
	 * Constructor for a ConnectFourGame. Creates a board of pSize size, 
	 * and sets every space in it to be blank.
	 * @param pSize The size of the board; the total number of rows and 
	 * columns.
	 *****************************************************************/
	public ConnectFourGame(int pSize) {
		// If pSize is a valid size, set size to it.
		if (pSize <= 10 && pSize >= 4) {
			size = pSize;
		} else { // Otherwise default to 7.
			size = 7;
		}
		// Creates a board of size size. 
		board = new int[size][size];
		// Sets every coordinate on the board to blank.
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				board[row][col] = BLANK;
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
			// Try to place piece, starting from the bottom (height 0 
			// to size)
			if (!spaceOccupied(row, col)) {
				board[row][col] = getTurn();
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
		// Start check from bottom left so we don't have to check any
		// pieces below
		for (int row = size - 1; row >= 0; row--) {
			for (int col = 0; col < size; col++) {
				// If piece at coordinate belongs to passed user
				if (board[row][col] == person) {
					// Check if 4 in a row, return true or false
					if (checkFourInARow(person, row, col)) {
						return true;
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
	 * @return True if 4 in a row in any direction, false otherwise.
	 *****************************************************************/
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
	 * @param initialX Starting coordinate to check from for columns.
	 * @param initialY Starting coordinate to check from for rows.
	 * @return True if there are connectNum of pieceType pieces in a 
	 * row in the given direction from the given coordinate, false 
	 * otherwise.
	 *****************************************************************/
	private boolean checkConnections(int pieceType, int connectNum, 
			int horizontalDir, int verticalDir, int initialX, int 
			initialY) {
		int xCoord = initialX;
		int yCoord = initialY;
		// Must be able to loop connectNum times to return true.
		for (int numInARow = 0; numInARow < connectNum; numInARow++) {
			// If the coordinate being checked is out of bounds, 
			// return false.
			if (xCoord >= size || yCoord >= size  || xCoord < 0 || 
					yCoord < 0) {
				return false;
				// If the coordinate being checked is not the pieceType, 
				// return false.
			} else if (board[yCoord][xCoord] != pieceType) {
				return false;
			}
			// Increase once so the next piece in the given direction 
			// will be checked.
			xCoord += horizontalDir;
			yCoord -= verticalDir;
		} 
		return true;
	}

	/******************************************************************
	 * Resets the board to all blank pieces, and if the size has changed 
	 * it updates the size.
	 *****************************************************************/
	public void reset() {
		board = new int[size][size];
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				board[col][row] = BLANK;
			}
		}
	}

	/******************************************************************
	 * The computer tries first to win, and if it can't win it tries to 
	 * block the player from winning. If neither of these conditions can 
	 * be met, it tries to make a move that will set it up for a win in 
	 * the future.
	 *****************************************************************/
	public void computerTurn() {
		// Computer tries to win.
		boolean turnDone = compAttemptWinOrBlock(1);
		// If it didn't win, tries to block.
		if (!turnDone) {
			turnDone = compAttemptWinOrBlock(0);
		}
		// If neither of the above have been met, try to make a logical 
		// move.
		if (!turnDone) {
			computerTurn(4);
		}
	}

	/******************************************************************
	 * This method checks if a given person can win next turn for any 
	 * column. If it can, place a computer piece there.
	 * @param person Who to check for a possible win.
	 * @return true if can make move
	 *****************************************************************/
	private boolean compAttemptWinOrBlock(int person) {
		// Place in every column.
		for (int col = 0; col < size; col++) {
			// If piece can drop in a row
			if (!spaceOccupied(0, col)) {
				// Make given person place a piece, and see if they win.
				currentTurn = person;
				int lastY = selectCol(col);
				// If they can win, places the computer piece at that 
				// coordinate.
				if (isWinner(person)) {
					board[lastY][col] = COMPUTER;
					return true;
				} else {
					// If they can't win, undo the move.
					board[lastY][col] = -1;
				}
			}
		}
		return false;
	}

	/******************************************************************
	 * This method looks to make a connection of pieces for the
	 * computer, and if it can't, looks for a move to block the player
	 * of the same amount of connections.
	 * @param Number of connections to look for and block against
	 * ex. 5 would look for connections of 5, 4, 3, and 2 before
	 * placing a piece in a random column for the computer.
	 * @return Returns an integer to stop checking for more connections
	 * if a good move is found.
	 *****************************************************************/
	public int computerTurn(int connections) {
		// Checks for a given number of connections
		for(int i = connections; i > 1; i--) {
			// See if it can win in any column
			for(int col = 0; col < size; col++) {
				setTurn(1);
				int row = selectCol(col);
				if (!(row == -1)) { // If the pieces can be placed, 
					// check if can win
					if(checkConnections(COMPUTER, i, 0, 1, col, row)
							|| checkConnections(COMPUTER, i, 1, 0, col, 
									row)
							|| checkConnections(COMPUTER, i, 1, 1, col, 
									row)
							|| checkConnections(COMPUTER, i, -1, 1, col,
									row)) {
						return 1;
					} else { // Remove piece if doesn't win
						board[row][col] = BLANK;
					}
				}
			}
			// See if player can win in any column
			for(int col = 0; col < size; col++)
			{
				setTurn(0);
				int row = selectCol(col);
				if (!(row == -1)) { // If the piece can be placed, 
					// check if can win
					if(checkConnections(USER, i, 0, 1, col, row)
							|| checkConnections(USER, i, 1, 0, col, row)
							|| checkConnections(USER, i, 1, 1, col, row)
							|| checkConnections(USER, i, -1, 1, col, row))
					{ // If they can win, block the player
						board[row][col] = COMPUTER;
						return -1;
					} else { // Otherwise remove the piece.
						board[row][col] = BLANK;
					}
				}
			}
		}
		// If there are no good moves, just place a piece randomly.
		Random rand = new Random();
		setTurn(1);
		selectCol(rand.nextInt(size));
		return 0;
	}

	/******************************************************************
	 * Checks to see if a given coordinate is occupied by either the 
	 * player or computer.
	 * @param row Row to check to see if occupied.
	 * @param col Column to check to see if occupied.
	 * @return True if the coordinate is occupied, false if it is not.
	 *****************************************************************/
	public boolean spaceOccupied(int row, int col) {
		if (board[row][col] == USER || board[row][col] == COMPUTER) {
			return true;
		} else {
			return false;
		}
	}

	/******************************************************************
	 * Changes the currentTurn to a given person's turn.
	 * @return Whose turn it now is.
	 *****************************************************************/
	public int switchPlayer() {
		currentTurn = (currentTurn + 1) % 2;
		return currentTurn;
	}

	/******************************************************************
	 * Sets the turn to the given number, assuming it's either 0 or 1.
	 *****************************************************************/
	public void setTurn(int turn) {
		if (turn == 0) {
			currentTurn = 0;
		} else if (turn == 1) {
			currentTurn = 1;
		}
	}

	/******************************************************************
	 * Checks if the space under a coordinate is occupied or not to 
	 * prevent a piece from floating.
	 * @param row Row of where a piece will be placed.
	 * @param col Column of where a piece will be placed.
	 * @return True if space under the coordinate is occupied, false 
	 * if it is not.
	 *****************************************************************/
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

	/******************************************************************
	 * Gets the currentTurn.
	 * @return Whose turn it currently is. (0 for player, 1 for 
	 * computer.)
	 *****************************************************************/
	public int getTurn() {
		return currentTurn;
	}

	/******************************************************************
	 * Returns the piece at a given coordinate on the board.
	 * @param x Column to check for a piece.
	 * @param y Row to check for a piece.
	 * @return What value is at the given coordinate. (0 for player, 
	 * 1 for computer, or -1 for empty.)
	 *****************************************************************/
	public int getPiece(int x, int y) {
		return board[y][x];
	}

	/******************************************************************
	 * Gets the current size of the board.
	 * @return The current size of the board.
	 *****************************************************************/
	public int getSize() {
		return size;
	}

	/******************************************************************
	 * Sets the size to a passed in size between 5 and 10 (inclusive). 
	 * If out of bounds, defaults to 7.
	 * @param size Total number of rows and columns for the board.
	 *****************************************************************/
	public void setSize(int size) {
		if (size >= 5 && size <= 10) {
			this.size = size;
		} else {
			this.size = 7;
		}
	}
}

//private boolean checkConnectionsMinmax(int[][] brd, int pieceType, 
//	int connectNum, 
//	int horizontalDir, int verticalDir, int initialX, int 
//	initialY) {
//	int xCoord = initialX;
//	int yCoord = initialY;
//	// Must be able to loop connectNum times to return true.
//	for (int numInARow = 0; numInARow < connectNum; numInARow++) {
//		// If the coordinate being checked is out of bounds, 
//		// return false.
//		if (xCoord >= size || yCoord >= size  || xCoord < 0 || 
//				yCoord < 0) {
//		return false;
//		// If the coordinate being checked is not the pieceType, 
//		// return false.
//		} else if (brd[yCoord][xCoord] != pieceType) {
//			return false;
//		}
//	// Increase once so the next piece in the given direction 
//	// will be checked.
//		xCoord += horizontalDir;
//		yCoord -= verticalDir;
//	} 
//	return true;
//	}
//	public int selectColMinmax(int[][] brd,int col) {		
//		for (int row = size - 1; row >= 0; row--) {
//			// try to place piece, starting from the bottom (height 0 
//			// to size)
//			if (!spaceOccupiedMinmax(brd, row, col)) {
//				brd[row][col] = getTurn();
//				return row;
//			}
//		}
//		return -1;
//	}
//	public boolean spaceOccupiedMinmax(int[][] brd, int row, int col) {
//		if (brd[row][col] == USER || brd[row][col] == COMPUTER) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	// Scores the move based on if the computer or player are 
//	   closer to winning
//			private int score(int[][] board, boolean computerTurn,
//			int col, int row, int depth)
//			{
//				if(computerTurn)
//				{
//			if(checkConnectionsMinmax(board, COMPUTER, 2, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 2, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 2, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 2, -1, 1, col, row))
//					{
//						return 50/depth;
//					}
//				
//			if(checkConnectionsMinmax(board, COMPUTER, 3, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 3, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 3, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 3, -1, 1, col, row))
//					{
//						return 75/depth;
//					}
//				
//			if(checkConnectionsMinmax(board, COMPUTER, 4, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 4, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 4, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, COMPUTER, 4, -1, 1, col, row))
//					{
//						return 1000/depth;
//					}
//				}
//				else
//				{
//			if(checkConnectionsMinmax(board, USER, 2, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 2, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, USER, 2, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 2, -1, 1, col, row))
//					{
//						return -50/depth;
//					}
//				
//			if(checkConnectionsMinmax(board, USER, 3, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 3, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, USER, 3, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 3, -1, 1, col, row))
//					{
//						return -75/depth;
//					}
//				
//			if(checkConnectionsMinmax(board, USER, 4, 0, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 4, 1, 0, col, row)
//			|| checkConnectionsMinmax(board, USER, 4, 1, 1, col, row)
//			|| checkConnectionsMinmax(board, USER, 4, -1, 1, col, row))
//					{
//						return -1000/depth;
//					}
//				}
//				return 0;
//			}
//			
//		   Places the piece the column that maximizes comp win
// 			potential and minimizes player win potential
//			private void minmax()
//			{
//				int row = 0;
//				int[] moves = new int[size];
//				int[][] testboard = board;
//				int score = 0;
//					
//					// Tree
//					for(int m = 0; m < size; m++)
//					{
//						setTurn(1);
//						int[][] branch1 = testboard;
//						
//					// Branch1 //
//						
//						// Computer's portion of branch
//						
//					// Place a piece in the mth column for the computer
//						row = selectColMinmax(branch1, m);
//						score += score(branch1, true, m, row, 1);
//						
//						// Branch2 //
//						for(int p = 0; p < size; p++)
//						{
//							setTurn(0);
//							int[][] branch2 = branch1;
//					// Place a piece in the pth column for the player
//							row = selectColMinmax(branch2, p);
//							score += score(branch2, false, p, row, 2);
//							
//							// Branch3 //
//							for(int q = 0; q < size; q++)
//							{
//							setTurn(1);
//							int[][] branch3 = branch2;
//					// Place a piece in the qth column for the computer
//							row = selectColMinmax(branch3, q);
//							score += score(branch3, true, q, row, 3);
//							
//							// Branch4 //
//							for(int r = 0; r < size; r++)
//							{
//								setTurn(0);
//								int[][] branch4 = branch3;
//					// Place a piece in the rth column for the player
//							row = selectColMinmax(branch4, r);
//							score += score(branch4, false, q, row, 4);
//							}
//						}
//					}
//					// Add the score of the move to the array of moves
//					moves[m] += score;
//				}
//					
//				// Pick the best move from the moves arraylist
//				int bestScore = 0;
//				int bestMove = 0;
//				for(int i = 0; i < moves.length; i++)
//				{
//					if(moves[i] > bestScore)
//					{
//						bestScore = moves[i];
//						bestMove = i;
//					}
//				}
//				
//				// Play the best possible move
//				setTurn(1);
//				selectCol(bestMove);
//			}
