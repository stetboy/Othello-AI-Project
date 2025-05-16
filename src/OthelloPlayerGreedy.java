import java.util.Random;

public class OthelloPlayerGreedy  extends OthelloPlayer {

	private Random rnd = new Random();
	
	public void beginGame()
	{
		
	}
	
	public OthelloMove getNextMove(OthelloBoard g)
	{
		if (!g.hasMove(color))
			return null;
		int maxChips = 0;
		int maxRow=0, maxCol=0;
		for(int row=1; row<=OthelloBoard.NROWS; row++) {
			for(int col=1; col<=OthelloBoard.NCOLS; col++) {
				if(g.getSquare(row, col) == OthelloBoard.EMPTY && ok(row, col, g)) {
					OthelloBoard temp = new OthelloBoard(g);
					temp.placeChip(row, col, color);
					int numChips = temp.getChips(color);
					if (numChips > maxChips) {
						maxChips = numChips;
						maxRow = row;
						maxCol = col;
					}
				}
			}
		}
		if (maxChips == 0)
			return null;
		return new OthelloMove(maxRow, maxCol);
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
