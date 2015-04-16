package main.java.org.hanzet23.gameframework.games.othello;

import java.util.ArrayList;

public class OthelloMinimaxAI extends OthelloAI {
	
	private final static int DEPTH = 6;
	private final static int FORFEIT = 35;
	private final static int FRONTIER = 10;
	private final static int MOBILITY = 5;
	private final static int STABILITY = 50;
	
	private final static int MAX_RANK = Integer.MAX_VALUE - 64;

	public OthelloMove getBestMove(char player, char[][] board)
	{
		// Initialize the alpha-beta cutoff values.
		int alpha = MAX_RANK + 64;
		int beta  = -alpha;

		// Kick off the look ahead.
		return this.getBestMove(board, player, 1, alpha, beta);
	}
	
	public OthelloMove getBestMove(char[][] board, char player, int depth, int alpha, int beta) {
		char opponent = 0;
		if (player == 'X') {
			opponent = 'O';
		} else {
			opponent = 'X';
		}
		
		// Initialize the best move.
		OthelloMove bestMove = new OthelloMove(-1, -1);
		bestMove.setValue(-1 * MAX_RANK);
		
		ArrayList<OthelloMove> moves = getValidMoves(player, board);

		// Find out how many valid moves we have so we can initialize the
		// mobility score.
		int validMoves = moves.size();
		
		for (OthelloMove move : moves) {
			// Copy the board
			char[][] tempBoard = othelloBoard.cloneBoard(board);
			
			// Make a fake move
			place(player, move, tempBoard);
			// Check the score
			int score = othelloBoard.countTiles(board, 'O') - othelloBoard.countTiles(board, 'X');

			// Check the board
			int forfeit = 0;
			boolean isEndGame = false;
			ArrayList<OthelloMove> opponentValidMoves = getValidMoves(opponent, tempBoard);
			
			if (opponentValidMoves.size() == 0) {
				// The opponent cannot move, count the forfeit.
				forfeit = player;
				
				// If the player cannot make a move, game over
				if (getValidMoves(player, tempBoard).size() == 0) {
					isEndGame = true;
				}
			}
			
			// If we reached the end of the look ahead (end game or
			// max depth), evaluate the board and set the move
			// rank.
			if (isEndGame || depth == DEPTH) {
				// For an end game, max the ranking and add on the
				// final score.
				if (isEndGame) {
					// Negative value for black win.
					if (score < 0)
						move.setValue(-MAX_RANK + score);
					
					// Positive value for white win.
					else if (score > 0)
						move.setValue(MAX_RANK + score);
					
					// Zero for a draw.
					else
						move.setValue(0);
				} else {
					// TODO: left out frontier, stability
					
					// It's not an end game so calculate the move rank.
					int rank = FORFEIT * forfeit + MOBILITY * player * (validMoves - opponentValidMoves.size());
						
					move.setValue(rank);
				}
			}
			
			// Otherwise, perform a look ahead.
			else {
				OthelloMove nextMove = getBestMove(tempBoard, opponent, depth + 1, alpha, beta);

				// Pull up the rank.
				move.setValue(nextMove.getValue());
				
				// Forfeits are cumulative, so if the move did not
				// result in an end game, add any current forfeit
				// value to the rank.
				if (forfeit != 0 && Math.abs(move.getValue()) < MAX_RANK) {
					int rank = move.getValue() + (FORFEIT * forfeit);
					move.setValue(rank);
				}
				
				// Adjust the alpha and beta values, if necessary.
				if (player == 'O'
						&& move.getValue() > beta)
					beta = move.getValue();
				if (player == 'X'
						&& move.getValue() < alpha)
					alpha = move.getValue();
			}
			
			// Perform a cutoff if the rank is outside tha alpha-beta
			// range.
			if (player == 'O' && move.getValue() > alpha) {
				move.setValue(alpha);
				return move;
			}
			if (player == 'X' && move.getValue() < beta) {
				move.setValue(beta);
				return move;
			}

			// If this is the first move tested, assume it is the
			// best for now.
			if (bestMove.getX() < 0)
				bestMove = move;

			// Otherwise, compare the test move to the current
			// best move and take the one that is better for this
			// color.
			else if (player * move.getValue() > player * bestMove.getValue())
				bestMove = move;
		}
		
		// Return the best move found.
		return bestMove;
	}

}
