import java.util.Random;

public class OthelloPlayerRandom extends OthelloPlayer {

	private Random rnd = new Random();
	
	public void beginGame()
	{
		
	}
	
	public OthelloMove getNextMove(OthelloBoard g)
	{
		if (!g.hasMove(color))
			return null;
		int row = rnd.nextInt(OthelloBoard.NROWS) + 1;
		int col = rnd.nextInt(OthelloBoard.NCOLS) + 1;
		while (!(g.getSquare(row, col) == OthelloBoard.EMPTY) || !ok(row, col, g)) {
			row = rnd.nextInt(OthelloBoard.NROWS) + 1;
			col = rnd.nextInt(OthelloBoard.NCOLS) + 1;
			}
		return new OthelloMove(row, col);
	}
	
	private boolean ok(int r, int c, OthelloBoard g)
	{
		for(int rinc=-1; rinc<=1; rinc++) {
			for(int cinc=-1; cinc<=1; cinc++) {
				if (rinc == 0 && cinc == 0)
					continue;
				if (g.checkForFlip(r, c, rinc, cinc, color))
					return true;
			}
		}
		return false;
	}
}
