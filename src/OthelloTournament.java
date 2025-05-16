import java.util.Scanner;

public class OthelloTournament {

	public static int numGames = 4;
	public static Scanner in = new Scanner(System.in);

	public static void main(String [] args)
	{
		Scanner in = new Scanner(System.in);
		OthelloPlayer p1 = new OthelloPlayerRandom();
		//OthelloPlayer p1 = new OthelloPlayerBoy();
		String p1Name = "Random";
		//String p1Name = "Steel";
		//OthelloPlayer p2 = new OthelloPlayerGreedy();
		OthelloPlayer p2 = new OthelloPlayerBoy();
		//String p2Name = "Greedy";
		String p2Name = "Steel";
		
		OthelloPlayer pFirst, pSecond;
		String pFirstName, pSecondName;
		double p1Score = 0, p2Score = 0;
		for(int iGame=1; iGame <=numGames; iGame++) {
			OthelloPanel gui = new OthelloPanel();
			gui.setVisible(true);
			OthelloBoard game = new OthelloBoard(gui);
			if (iGame%2 == 1) {
				p1.setColor(OthelloBoard.BLACK);
				pFirst = p1;
				pFirstName = p1Name;
				p2.setColor(OthelloBoard.WHITE);
				pSecond = p2;
				pSecondName = p2Name;
			}
			else {
				p1.setColor(OthelloBoard.WHITE);
				pSecond = p1;
				pSecondName = p1Name;
				p2.setColor(OthelloBoard.BLACK);
				pFirst = p2;
				pFirstName = p2Name;
			}
			System.out.println("Game " + iGame + ":");
			System.out.println("  Black: " + pFirstName);
			System.out.println("  White: " + pSecondName);
			p1.beginGame();
			p2.beginGame();
			int movesMade=4;
			while (!(game.won(OthelloBoard.BLACK) || game.won(OthelloBoard.WHITE))) {
				OthelloMove m = pFirst.getNextMove(game);
				if (m == null  || m.row <= 0 || m.col <= 0)
					System.out.println(pFirstName + " has no move");
				else {
					game.placeChip(m.row, m.col, OthelloBoard.BLACK);
					game.printGame(m.row, m.col);
					movesMade++;
				}
				if (movesMade == OthelloBoard.NCOLS*OthelloBoard.NROWS)
					break;
				m = pSecond.getNextMove(game);
				if (m == null || m.row <= 0 || m.col <= 0)
					System.out.println(pSecondName + " has no move");
				else {
					game.placeChip(m.row, m.col, OthelloBoard.WHITE);
					game.printGame(m.row, m.col);
					movesMade++;
				}
			}
			System.out.println();
			game.printChips();
			if (game.won(OthelloBoard.BLACK)) {
				System.out.println(pFirstName + " wins!!!");
				if (pFirstName.equals(p1Name))
					p1Score += 1;
				else
					p2Score += 1;
			}
			else if (game.won(OthelloBoard.WHITE)) {
				System.out.println(pSecondName + " wins!!!");
				if (pSecondName.equals(p1Name))
					p1Score += 1;
				else
					p2Score += 1;
			}
			else {
				System.out.println("\nDraw");
				p1Score += 0.5;
				p2Score += 0.5;
			}
			if (iGame < numGames) {
				System.out.println("\nHit return for next game");
				//in.nextLine();
				gui.setVisible(false);
			}
		}
		System.out.println("Final Score: ");
		System.out.printf("  %10s: %3.1f\n", p1Name, p1Score);
		System.out.printf("  %10s: %3.1f\n", p2Name, p2Score);
	}

}