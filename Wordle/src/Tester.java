import java.util.Scanner;

public class Tester {
	public static void main(String[] args) {
		Wordle w = new Wordle();
		Scanner input = new Scanner(System.in);
		String guess;

		//w.printIt(w.correctWord); System.out.println();
		System.out.println("Wordle\n");
		System.out.println("The word is " + w.correctWord.length + " letters long");
		System.out.println("There are " + w.words.size() + " " + w.correctWord.length + " letter words\n");

		int guessesLeft = Wordle.maxGuesses;
		while (true) {
			for (Character c : w.lettersLeft) {
				System.out.print(c);
			}
			System.out.println();
			guess = input.nextLine();
			guess = guess.toLowerCase();
			if (w.isValidWord(guess)) {
				guessesLeft--;
				w.makeGuess(Wordle.stringToCharArray(guess));
				w.pruneWords();
				System.out.println("\t\t\t\t\twords left now: " + w.legalWords.size());
				w.printIt(w.guesses.get(w.numGuesses-1));
				System.out.println("      " + w.getHint() + "      " + guessesLeft + " guesses left");
				
				if (w.legalWords.size() <= 10) {
					//w.printRemainingWords();
				}
				if (w.isWon) {
					System.out.println("You won on guess " + w.numGuesses + "!");
					System.exit(0);
				} else if (w.isLost) {
					System.out.println("You lose! The word was " + Wordle.charArrayToString(w.correctWord));
					System.exit(0);
				}
			} else {
				System.out.println("Invalid input");
			}
		}
	}
}
