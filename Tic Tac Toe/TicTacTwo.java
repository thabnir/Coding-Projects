import java.util.*;
import java.awt.*;
public class TicTacTwo {
	public static void main(String[] args) {
		BoardLogic b = new BoardLogic(3); // paramater equals the dimensions of the board
		Scanner input = new Scanner(System.in);
		int nextRow; int nextColumn;
		boolean twoPlayer = true;
		boolean validInput  = false;
		char winner = 'n';
		Random gen = new Random();

		System.out.print("Enter 1 to play vs AI or 2 to play with 2 players (default is 2): ");
		while (!validInput) {
			if(input.hasNextInt()) {
				int inputPlayerCount = input.nextInt();
				if (inputPlayerCount==1) {
					System.out.println("1 Player mode selected\n");
					twoPlayer = false;
				} else if (inputPlayerCount== 2) {
					System.out.println("2 Player mode selected\n");
					twoPlayer = true;
				} else {
					System.out.println("Defaulting to 2 player mode\n");
				}
				validInput = true;
			} else {
				System.out.println("\nMake sure it's an integer\n");
				System.out.print("Enter 1 to play alone or 2 to play with 2 players (default is 2): ");
				input.next();
			}
		}

		// todo: give it a gui. ideally clickable
		// if clickable is too much, then can keep input on console, display board in graphics panel

		// GAME LOOP
		while (true) {
			b.printBoardArray();
			b.setValidMove(false);
			while (!b.getValidMove()) {
				if (twoPlayer || b.getCurrentTurn() == b.getP1Symbol()) {
					System.out.print("Enter the row you want your next piece to go: ");
					nextRow = input.nextInt()-1;
					System.out.print("Enter the column you want your next piece to go: ");
					nextColumn = input.nextInt()-1;
				} else {
					b.findWinningMove(b, b.getP2Symbol());
					if (b.hasWinningMove()) {
						nextRow = b.getAIRow();
						nextColumn = b.getAIColumn();
						System.out.println("Winning move found");
					} else {
						nextRow = gen.nextInt(b.getSize());
						nextColumn = gen.nextInt(b.getSize());
						System.out.println("No winning move found");
					}
				}
				b.setPieceOn( nextRow,nextColumn, b.getCurrentTurn() );
				if (!b.getValidMove()) {
					b.printBoardArray();
					System.out.println("INVALID MOVE");
				}
			}
			winner = b.staticWinCheck(b.getBoardState(), b.getCurrentTurn());
			if (winner != 'n') {
				b.printBoardArray();
				if (winner == b.getCurrentTurn() )
					System.out.println(b.getPlayerName() + " wins!");
				else
					System.out.println("Tie!");
				// System.out.println("Streak: " +b.longestStreak + " | " +b.lastRow + ", " + b.lastCol);
				return;
			}
			b.changeTurns();
		}
		
	}
}