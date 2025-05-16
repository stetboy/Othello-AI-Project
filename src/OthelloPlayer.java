
public abstract class OthelloPlayer {

	protected int color;
	
	public abstract OthelloMove getNextMove(OthelloBoard g);
	
	public abstract void beginGame();
	
	public void setColor(int c)
	{
		color = c;
	}
}
