
public class OthelloBoard {

	public static final int EMPTY = -1;
	public static final int BLACK = 0;
	public static final int WHITE = 1;

	public static final int NROWS = 8;
	public static final int NCOLS = 8;

	private int[][] board = new int[NROWS+2][NCOLS+2];
	private int[]  numChips = new int[2];
	
	private int speed = 1;

	OthelloPanel panel;

	public OthelloBoard(OthelloPanel panel)
	{
		this.panel = panel;
		init();
	}

	public OthelloBoard(OthelloBoard other)
	{
		for(int j=0; j<=NCOLS+1; j++) {
			for(int i=0; i<=NROWS+1; i++)
				board[i][j] = other.board[i][j];
		}
		numChips[BLACK] = other.numChips[BLACK];
		numChips[WHITE] = other.numChips[WHITE];
	}

	public void init()
	{
		for(int j=0; j<=NCOLS+1; j++) {
			for(int i=0; i<=NROWS+1; i++)
				board[i][j] = EMPTY;
		}
		board[4][4] = board[5][5] = BLACK;
		board[4][5] = board[5][4] = WHITE;
		numChips[BLACK] = numChips[WHITE] = 2;
	}

	public boolean placeChip(int row, int col, int color)
	{
		if(row < 1 || row > 8 || col < 1 || col > 8 || board[row][col] != EMPTY)
			return false;

		board[row][col] = color;
		boolean flipFlag = false;
		for(int i=-1; i<=1; i++)
			for(int j=-1; j<=1; j++) {
				if (flip(row, col, i, j, color))
					flipFlag = true;
			}
		if (flipFlag) {
			numChips[color]++;
			return true;
		}
		else {
			board[row][col] = EMPTY;
			return false;
		}
	}

	public boolean flip(int row, int col, int rowinc, int colinc, int color)
	{
		int r = row+rowinc;
		int c = col+colinc;
		int otherColor = BLACK+WHITE-color;
		if (board[r][c] != otherColor)
			return false;
		while (board[r][c] == otherColor) {
			r += rowinc;
			c += colinc;
		}
		if (board[r][c] == color) {
			r = row+rowinc;
			c = col+colinc;
			while (board[r][c] == otherColor) {
				board[r][c] = color;
				numChips[color]++;
				numChips[otherColor]--;
				r += rowinc;
				c += colinc;
			}
			return true;
		}
		else
			return false;
	}

	public boolean won(int color)
	{
		int othercolor = BLACK+WHITE-color;
		if (!hasMove(othercolor) && !hasMove(color)
				&& numChips[color] > numChips[othercolor])
			return true;
		return (numChips[BLACK]+numChips[WHITE] == NROWS*NCOLS
				&& numChips[color] > numChips[othercolor]);
	}

	public boolean hasMove(int color)
	{
		for(int row=1; row<=NCOLS; row++)
			for(int col=1; col<=NCOLS; col++) {
				if (board[row][col] != EMPTY)
					continue;
				for(int i=-1; i<=1; i++)
					for(int j=-1; j<=1; j++)
						if (checkForFlip(row, col, i, j, color))
							return true;
			}
		return false;
	}

	public boolean checkForFlip(int row, int col, int rowinc, int colinc, int color)
	{
		int r = row+rowinc;
		int c = col+colinc;
		int otherColor = BLACK+WHITE-color;
		if (board[r][c] != otherColor)
			return false;
		while (board[r][c] == otherColor) {
			r += rowinc;
			c += colinc;
		}
		if (board[r][c] == color) {
			return true;
		}
		else
			return false;
	}

	public int getSquare(int row, int col)
	{
		if (row<1 || row >NROWS || col<1 || col>NCOLS)
			return EMPTY;
		else
			return board[row][col];
	}
	
	public int getChips(int color)
	{
		return numChips[color];
	}

	public void printChips()
	{
		System.out.println("BLACK has " + numChips[BLACK] + ", WHITE has " + numChips[WHITE]);
	}
	
	public void setSpeed(int s)
	{
		if (s >= 0)
			speed = s;
	}

	public void printGame()
	{
		System.out.println("\n  12345678\n +--------+");
		for(int r=1; r<=NROWS; r++) {
			System.out.print(r + "|");
			for(int c=1; c<=NCOLS; c++) {
				if (board[r][c] == BLACK) {
					System.out.print("X");
				}
				else if (board[r][c] == WHITE) {
					System.out.print("O");
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println("|" + r);
		}
		System.out.println(" +--------+\n  12345678");
	}

	public void printGame(int row, int col)
	{
		if (row != -1) {
			if (board[row][col] == BLACK)
				panel.setSquare(row,col, BLACK);
			else
				panel.setSquare(row, col, WHITE);
			try {
				Thread.currentThread().sleep(speed*100);
			} catch (InterruptedException e) {
			}
		}
		for(int r=1; r<=NROWS; r++) {
			for(int c=1; c<=NCOLS; c++) {
				if (board[r][c] == BLACK) {
					panel.setSquare(r, c, BLACK);
				}
				else if (board[r][c] == WHITE) {
					panel.setSquare(r, c, WHITE);
				}
			}
		}
		try {
			Thread.currentThread().sleep(speed*100);
		} catch (InterruptedException e) {
		}
	}
}
