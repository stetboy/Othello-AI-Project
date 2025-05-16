import java.util.Scanner;

public class Othello {

	public static void main(String [] args)
	{
		Scanner in = new Scanner(System.in);
		OthelloPanel gui = new OthelloPanel();
		gui.setVisible(true);
		OthelloBoard game = new OthelloBoard(gui);
		char ans;
		do {
			System.out.println("Choose 1:\n\t1 - user vs. agent");
			System.out.print("\t2 -- agent vs. agent\n\n--> ");
			ans = in.next().charAt(0);
		} while (ans != '1' && ans != '2');

		//OthelloPlayer p1 = new OthelloPlayerRandom();
		OthelloPlayer p1 = new OthelloPlayerBoy();
		
		int compColor, userColor;
		if (ans == '1') {
			System.out.print("\nDo you wish to go first (y/n)? --> ");
			ans = in.next().charAt(0);
			if (ans == 'y') {
				userColor = OthelloBoard.BLACK;
				compColor = OthelloBoard.WHITE;
			}
			else {
				compColor = OthelloBoard.BLACK;
				userColor = OthelloBoard.WHITE;
			}
			p1.setColor(compColor);
			p1.beginGame();

			int currColor = OthelloBoard.BLACK;
			game.printGame(-1, -1);
			int movesMade = 4;
			while (!(game.won(OthelloBoard.BLACK) || game.won(OthelloBoard.WHITE))) {
				OthelloMove m;
				if (currColor == compColor) {
					m = p1.getNextMove(game);
					if (m == null)
						System.out.println("Agent has no move");
					else {
						game.placeChip(m.row, m.col, compColor);
						game.printGame(m.row, m.col);
						System.out.println("Agent plays " + m.row + ',' + m.col);
						movesMade++;
					}
				}
				else {
					int row, col;
					do {
						System.out.print("Enter row and col for your next play --> ");
						row = in.nextInt();
						col = in.nextInt();
					} while(!game.placeChip(row, col, userColor));
					game.printGame(row, col);
					movesMade++;
				}
				currColor = OthelloBoard.WHITE+OthelloBoard.BLACK-currColor;
			}
			if (game.won(compColor)) {
				System.out.println("\nAgent wins!!!");
			}
			else if (game.won(userColor)) {
				System.out.println("\nUser wins!!!");
			}
			else {
				System.out.println("\nDraw");
			}
		}
		else {
			OthelloPlayer p2 = new OthelloPlayerGreedy();
			
			p1.setColor(OthelloBoard.BLACK);
			p2.setColor(OthelloBoard.WHITE);
			p1.beginGame();
			p2.beginGame();
			int movesMade=4;
			while (!(game.won(OthelloBoard.BLACK) || game.won(OthelloBoard.WHITE))) 
			{
				OthelloMove m = p1.getNextMove(game);
				
				if (m == null  || m.row <= 0 || m.col <= 0)
					System.out.println("Agent 1 has no move");
				else {
					game.placeChip(m.row, m.col, OthelloBoard.BLACK);
					game.printGame(m.row, m.col);
					game.printChips();
					System.out.println("Agent 1 plays " + m.row + ',' + m.col);
					movesMade++;
				}
				if (movesMade == OthelloBoard.NCOLS*OthelloBoard.NROWS)
					break;
				m = p2.getNextMove(game);
				if (m == null || m.row <= 0 || m.col <= 0)
					System.out.println("Agent 2 has no move");
				else {
					game.placeChip(m.row, m.col, OthelloBoard.WHITE);
					game.printGame(m.row, m.col);
					game.printChips();
					System.out.println("Agent 2 plays " + m.row + ',' + m.col);
					movesMade++;
				}
			}
			if (game.won(OthelloBoard.BLACK)) {
				System.out.println("\nAgent 1 wins!!!");
			}
			else if (game.won(OthelloBoard.WHITE)) {
				System.out.println("\nAgent 2 wins!!!");
			}
			else {
				System.out.println("\nDraw");
			}
		}
	}

}
