// Class: Othello Player Agent
// Author: Steel Boyer
// Date: 12/2/2024
// Purpose: AI Agent for the game of Othello. Utilizes a minimax algorithm with Alpha-Beta Prunning.

import java.util.ArrayList;

public class OthelloPlayerBoy extends OthelloPlayer {
	// constant that stores the maximum amount of time (in milliseconds) a turn can take to calculate.
	final long MAX_TIME = 5000;
	
	// Global variable
	private long start;
	
	// Class that stores an array of best-moves the player can do,
	// along with the expected utility at that particular node
	private class turnInfo
	{
		private ArrayList<OthelloMove> playerMove;
		private int utility;
		
		// Constructor method
		public turnInfo(ArrayList<OthelloMove> moves, int utility)
		{
			this.playerMove = new ArrayList<>();
			
			if(moves != null)
			{
				for(OthelloMove move: moves)
				{
					this.playerMove.add(move);
				}
				// end for
			}
			// end if
			
			this.utility = utility;
		}
	}

	// Method that calculates AI's agents next move to make, utilizes miniMax with alpha-beta pruning algorithm.
	public OthelloMove getNextMove(OthelloBoard g)
	{
		// Stores the start time so we know how long we've been calculating our turn.
		start = System.currentTimeMillis();

		if (!g.hasMove(color))
			return null;
		// end if

		turnInfo move = miniMaxAB(g, MAX_TIME, true, -Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		// Can we do any move?
		if(move.playerMove == null)
			return null;
		// end if
		
		// Returns the best move we could make
		return move.playerMove.get(0);
	}

	// As miniMaxAB is similar to miniMinAB (except for a few differences), refer to the miniMinAB method for comments
	private turnInfo miniMaxAB(OthelloBoard pos, long currTimeLeft, boolean maxPlayer, int alpha, int beta)
	{
		if(deepEnough(currTimeLeft) || pos.won(color))
		{
			return new turnInfo(null, heuristicFunc(pos));
		}
		// end if
		
		ArrayList<OthelloMove> succ = expand(pos, maxPlayer);
		
		if(succ.size() == 0)
			return new turnInfo(null, heuristicFunc(pos));
		// end if
		
		ArrayList<OthelloMove> moves = new ArrayList<>();
		
		int bestVal = -Integer.MAX_VALUE;
		
		int succSize = succ.size();
		
		// Go through each potential move to calculate the potential value there
		for(OthelloMove s : succ)
		{
			OthelloBoard potentialNextMove = new OthelloBoard(pos);
			potentialNextMove.placeChip(s.row, s.col, color);
			
			long timeLeft = MAX_TIME - (System.currentTimeMillis() - start);
			
			long avgTimePerS = timeLeft / succSize--;
			
			turnInfo newVal = miniMinAB(potentialNextMove, avgTimePerS, !maxPlayer, alpha, beta);
			
			if(newVal.utility > bestVal)
			{
				bestVal = newVal.utility;
				
				moves.clear();
				
				moves.add(s);
			}
			else if(newVal.utility == bestVal)
				moves.add(s);
			// end if
			
			if(bestVal > beta)
				break;
			else if(bestVal > alpha)
				alpha = bestVal;
			// end if
		}
		// end for
		
		return new turnInfo(moves, bestVal);
	}
	
	// miniMinAB algorithm is similar to miniMaxAB, but it solves for the minimum instead (i.e. the opponents turn)
	private turnInfo miniMinAB(OthelloBoard pos, long currTimeLeft, boolean maxPlayer, int alpha, int beta)
	{
		// Tracks the color of the opponent
		int otherColor = -1;
		
		// Gets the color of the opponent
		if(color == OthelloBoard.BLACK)
			otherColor = OthelloBoard.WHITE;
		else
			otherColor = OthelloBoard.BLACK;
		// end if
		
		// Checks if we have time to process more turns or if the current position is a terminal state (game end)
		if(deepEnough(currTimeLeft) || pos.won(otherColor))
		{
			return new turnInfo(null, heuristicFunc(pos));
		}
		// end if
		
		// Stores all moves we can do from this particular position
		ArrayList<OthelloMove> succ = expand(pos, maxPlayer);
		
		// No moves we can do? Process heuristic
		if(succ.size() == 0)
			return new turnInfo(null, heuristicFunc(pos));
		// end if
		
		// Stores the current best move(s) we can do in this position
		ArrayList<OthelloMove> moves = new ArrayList<>();
		
		// Best heuristic value we came accross thus far
		int bestVal = Integer.MAX_VALUE;
		
		// Tracks the number of successor moves. Used in calculating the average time per successor-move.
		int succSize = succ.size();
		
		// Go through each potential move to calculate the potential value there
		for(OthelloMove s : succ)
		{
			// Since succ is simply the row/column of the move to make, we need to actually create a board with said move being made
			OthelloBoard potentialNextMove = new OthelloBoard(pos);
			potentialNextMove.placeChip(s.row, s.col, otherColor);
			
			long timeLeft = MAX_TIME - (System.currentTimeMillis() - start);
			
			// Tracks the average amount of time we can spend on each successor-move processing.
			// Notice the decrease in succSize each time, that allows for time not utilizes in previous s processings to be used in the other s processings.
			long avgTimePerS = timeLeft / succSize--;
			
			// Calculate the opponents turn
			turnInfo newVal = miniMaxAB(potentialNextMove, avgTimePerS, !maxPlayer, alpha, beta);
			
			// Did we found a better move we could make than the current one?
			if(newVal.utility < bestVal)
			{
				bestVal = newVal.utility;
				
				moves.clear();
				
				moves.add(s);
			}
			// If the expected value we found from newVal is the same as the current best, add it to the moves list
			else if(newVal.utility == bestVal)
				moves.add(s);
			// end if
			
			// Info related to alpha-betta prunning
			if(bestVal < alpha)
				break;
			else if(bestVal < beta)
				beta = bestVal;
			// end if
		}
		// end for
		
		// We processed as much as we could, send the required info back to parent node
		return new turnInfo(moves, bestVal);
	}

	// returns a list of the moves that could be done
	private ArrayList<OthelloMove> expand(OthelloBoard pos, boolean maxPlayer)
	{
		// Stores all valid moves that could be made with this particular position
		ArrayList<OthelloMove> potentialMoves = new ArrayList<>();
		
		for(int row = 1; row <= OthelloBoard.NROWS; row++)
		{
			for(int col = 1; col <= OthelloBoard.NCOLS; col++)
			{
				// Is the space we're looking at empty & a valid move?
				if(pos.getSquare(row, col) == OthelloBoard.EMPTY && ok(row, col, pos, maxPlayer))
				{
					potentialMoves.add(new OthelloMove(row, col));
				}
				// end if
			}
			// end for
		}
		// end for
		
		return potentialMoves;
	}
	
	// Code Obtained from OthelloPlayerRandom & OthelloPlayerGreedy classes
	// Only update is the maxPlayer parameter, which tracks which player this is for
	private boolean ok(int r, int c, OthelloBoard g, boolean maxPlayer)
	{
		int useColor = color;
		
		// If it's opponents turn, flip the color to utilize
		if(!maxPlayer)
		{
			if(color == OthelloBoard.BLACK)
				useColor = OthelloBoard.WHITE;
			else
				useColor = OthelloBoard.BLACK;
			// end if
		}
		// end if
		
		for(int rinc=-1; rinc<=1; rinc++) {
			for(int cinc=-1; cinc<=1; cinc++) {
				if (rinc == 0 && cinc == 0)
					continue;
				if (g.checkForFlip(r, c, rinc, cinc, useColor))
					return true;
			}
		}
		return false;
	}
	
	private boolean deepEnough(long timeLeft)
	{
		final int MILLISECONDS_LEFT = 10;
		
		// If the time we have left to calculate is less than 10 milliseconds, we should probably make our decision asap
		if(timeLeft < MILLISECONDS_LEFT)
			return true;
		else
			// We got time to spare, continue on with processing the current node
			return false;
		// end if
	}
	
	// Estimates the potential utility of a potential position via heuristics.
	private int heuristicFunc(OthelloBoard pos)
	{
		// Stores the current expected value for this particular position
		int heuristicValue = 0;

		for(int row = 1; row <= OthelloBoard.NROWS; row++)
		{
			for(int col = 1; col <= OthelloBoard.NCOLS; col++)
			{
				// Player's color
				if(pos.getSquare(row, col) == color)
				{
					// Give a higher priority to the corner positions
					if(row == 1 && col == 1 || row == 1 && col == OthelloBoard.NCOLS - 1 || row == OthelloBoard.NROWS && col == 1 || row == OthelloBoard.NROWS && col == OthelloBoard.NCOLS)
					{
						heuristicValue += 5;
					}
					// All other spaces
					else
					{
						heuristicValue += 1;
					}
					// end if
				}
				// Other player's color
				else if(pos.getSquare(row, col) != OthelloBoard.EMPTY)
				{
					// We want the corners, give a large penalty if the other player manages to get a corner
					if(row == 1 && col == 1 || row == 1 && col == OthelloBoard.NCOLS - 1 || row == OthelloBoard.NROWS && col == 1 || row == OthelloBoard.NROWS && col == OthelloBoard.NCOLS)
					{
						heuristicValue -= 20;
					}
					// All other spaces
					else
					{
						heuristicValue -= 1;
					}
					// end if
				}
				// end if
			}
			// end for
		}
		// end for
		
		return heuristicValue;
	}
	
	public void beginGame()
	{

	}
}