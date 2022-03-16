import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Wordle {

	// make ai that guesses words based on the list (only after everything else
	// works, though)
	// find out if the creation of the dictionary is working correctly; it takes
	// forever
	// make a "new word" method so that you don't have to close and re-open every
	// time
	// should probably make a match check algorithm that's not associated with the
	// guess method
	// actually i should 100% have that, the guess method should just call my
	// matchcheck/comparewords method

	static File textFile = new File("F:\\Books\\wordles.txt");
	static File dictFile = new File("F:\\Books\\SortedDictionary.txt");
	final static int maxGuesses = 6;

	ArrayList<Character[]> dictionary = new ArrayList<Character[]>();
	ArrayList<Character[]> words = new ArrayList<Character[]>();
	ArrayList<Character[]> legalWords = new ArrayList<Character[]>();
	ArrayList<Character[]> guesses = new ArrayList<Character[]>();
	ArrayList<Character[]> prevWords = new ArrayList<Character[]>();

	Set<Character> lettersLeft = new HashSet<Character>(); // sets are like lists but contain no duplicates
	Set<Character> lettersUsed = new HashSet<Character>();
	Set<Character> lettersWrong = new HashSet<Character>();

	Set<IndexLetter> lettersMisplaced = new HashSet<IndexLetter>(); // but for some reason they don't work on these ones
	Set<IndexLetter> lettersCorrect = new HashSet<IndexLetter>(); // something to do with the fact it's my own data type

	Character[] correctWord;
	Character[] hint; // - means incorrect, ~ means wrong location, + means correct

	boolean isWon = false;
	boolean isLost = false;
	int numGuesses = 0;
	public int numLetters;

	Wordle() {
		Scanner input = null;

		try {
			input = new Scanner(textFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (input.hasNextLine())
			dictionary.add(stringToCharArray(input.nextLine().toLowerCase()));

		// System.out.println(dictionary.size());
		correctWord = dictionary.get((int) (Math.random() * dictionary.size())); // random word
		// correctWord = dictionary.get(455941);i

		while (input.hasNextLine())
			dictionary.add(stringToCharArray(input.nextLine().toLowerCase()));
		numLetters = correctWord.length;

		// printIt(correctWord);

		input = null;
		try {
			input = new Scanner(dictFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String next;
		while (input.hasNextLine()) {
			next = input.nextLine();
			if (next.length() == correctWord.length)
				words.add(stringToCharArray(next.toLowerCase()));
		}

		for (Character[] c : words)
			legalWords.add(c);

		hint = new Character[correctWord.length];

		for (int i = 0; i < hint.length; i++)
			hint[i] = '-';
	}

	void printRemainingWords() {
		for (int i = 0; i < legalWords.size(); i++) {
			printIt(legalWords.get(i));
			System.out.println();
		}
	}

	static Character[] arrayListToArray(ArrayList<Character> unconvertedWord) {
		Character[] convertedWord = new Character[unconvertedWord.size()];
		for (int i = 0; i < unconvertedWord.size(); i++) {
			convertedWord[i] = unconvertedWord.get(i);
		}
		return convertedWord;
	}

	boolean isValidWord(String unconvertedWord) {
		unconvertedWord = unconvertedWord.toLowerCase();
		Character[] input = stringToCharArray(unconvertedWord);
		for (Character[] word : words) {
			if (Arrays.equals(input, word))
				return true;
		}
		return false;
	}

	boolean isValidWord(ArrayList<Character> unconvertedWord) {
		Character[] convertedWord = Wordle.toLowerCase(arrayListToArray(unconvertedWord));
		for (Character[] word : words) {
			if (Arrays.equals(convertedWord, word))
				return true;
		}
		return false;
	}

	boolean checkCorrects(Character[] theWord) {
		for (IndexLetter c : lettersCorrect) {
			if (theWord[c.index] != c.letter) {
				return false;
			}
		}
		return true;
	}

	boolean checkWrongs(Character[] theWord) {
		for (Character c : lettersWrong) {
			if (charArrayToString(theWord).indexOf(c) != -1) {
				return false;
			}
		}
		return true;
	}

	boolean checkMisplaceds(Character[] theWord) {
		for (IndexLetter c : lettersMisplaced) {
			if (theWord[c.index] == c.letter) {
				return false;
			}
			if (charArrayToString(theWord).indexOf(c.letter) == -1) {
				return false;
			}
		}
		return true;
	}

	void pruneWords() {
		Iterator<Character[]> itr = legalWords.iterator();
		while (itr.hasNext()) {
			Character[] theWord = itr.next();
			if (!checkCorrects(theWord))
				itr.remove();
			else if (!checkWrongs(theWord))
				itr.remove();
			else if (!checkMisplaceds(theWord))
				itr.remove();
		}
	}

	static Character[] toLowerCase(Character[] g) {
		g = Wordle.stringToCharArray(Wordle.charArrayToString(g).toLowerCase());
		return g;
	}

	void makeGuess(Character[] g) {
		prevWords.add(g);
		g = Wordle.toLowerCase(g);
		if (Arrays.equals(correctWord, g))
			isWon = true;

		ArrayList<Character> lets = new ArrayList<Character>();

		for (int i = 0; i < correctWord.length; i++) {
			lets.add(correctWord[i]);
		}

		Character[] thisHint = new Character[g.length];
		Arrays.fill(thisHint, 'x');

		// x means incorrect, ~ means wrong location, + means correct
		for (int i = 0; i < g.length; i++) {
			if (lettersLeft.contains(g[i])) {
				lettersLeft.remove(g[i]);
				lettersUsed.add(g[i]);
			}

			if (g[i] == correctWord[i]) {
				hint[i] = g[i];
				thisHint[i] = '+';
				lettersCorrect.add(new IndexLetter(i, g[i]));
				lets.remove(g[i]);
			}
		}
		for (int i = 0; i < g.length; i++) {
			if (g[i] != correctWord[i] && lets.contains(g[i])) {
				thisHint[i] = '~';
				lettersMisplaced.add(new IndexLetter(i, g[i]));
				lets.remove(g[i]);
			} else {
				lettersWrong.add(g[i]);
			}
		}

		numGuesses++;
		if (numGuesses >= maxGuesses && !isWon)
			isLost = true;
		guesses.add(thisHint);
	}

	String getHint() {
		return charArrayToString(hint);
	}

	String getCorrectWord() {
		return charArrayToString(correctWord);
	}

	static Character[] stringToCharArray(String s) {
		Character[] c = new Character[s.length()];
		for (int i = 0; i < c.length; i++)
			c[i] = s.charAt(i);
		return c;
	}

	void printIt(Character[] word) {
		for (Character letter : word)
			System.out.print(letter);
	}

	static String charArrayToString(Character[] c) {
		String s = "";
		for (int i = 0; i < c.length; i++)
			s += c[i];
		return s;
	}

	public class IndexLetter {
		int index;
		Character letter;

		IndexLetter(int index, Character letter) {
			this.index = index;
			this.letter = letter;
		}
	}
}
