import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicsPanel extends JPanel implements KeyListener {
	int guessesLeft = Wordle.maxGuesses;
	ArrayList<Character> guess = new ArrayList<Character>();
	double fontSize;
	Wordle w;
	Font guessFont;
	double width;
	double height;
	boolean keyTyped = false;
	boolean wordEntered = false;
	boolean validWordEntered = false;
	boolean invalidWordEntered = false;

	// STYLE ELEMENTS:
	boolean hasAntiAliasing = true;

	double roundness = .3; // how round the boxes are, the higher the rounder

	final double centerCovered = .7; // higher = more screen covered
	final double boxCovered = .85; // percentage of the letter's box taken up by the letter

	double boxSize;
	double vertGapPercent = .25; // as a percentage of box size
	double horizGapPercent = .15; // as a percentage of box size
	double vertGap;
	double horizGap;

	double leftOffsetPercent = .4; // as a percentage of screen size
	double topOffsetPercent = .05; // as a percentage of screen size
	double leftOffset;
	double topOffset;

	Color boxColor = new Color(220, 220, 220); // Color boxColor = new Color(245, 235, 125);
	Color greenBox = new Color(41, 171, 56);
	Color yellowBox = new Color(212, 178, 42);
	Color blackBox = new Color(69, 69, 69);
	Color wrongWord = new Color(170, 50, 50);

	Color prevGuessColor = new Color(241, 241, 241);
	Color currentGuessColor = new Color(50, 50, 50);

	// END STYLE ELEMENTS

	GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(this);
		w = new Wordle();
		// wIt(w.correctWord); System.out.println();
		resize();
		System.setProperty("awt.useSystemAAFontSettings", "on");
	}

	void resize() {
		if (width != this.getWidth() || height != this.getHeight()) {
			width = this.getWidth();
			height = this.getHeight();
			boxSize = (centerCovered * width) / (w.numLetters); // perhaps make something for vertical size? or not tbh
			fontSize = (boxCovered * boxSize); // need to convert this from pixels to font size

			vertGap = boxSize * vertGapPercent;
			horizGap = boxSize * horizGapPercent;
			leftOffset = width / 2 - (boxSize + horizGap) * w.correctWord.length / 2 + horizGap / 2; // wrong
			topOffset = height / 2 - (boxSize + vertGap) * Wordle.maxGuesses / 2 + vertGap / 2;

			guessFont = new Font("Arial", Font.BOLD, (int) fontSize);
		}
	}

	void refresh() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		if (hasAntiAliasing) {
			Graphics2D g2d = (Graphics2D) g;
			// RenderingHints rhints = g2d.getRenderingHints();
			// boolean antialiasOn =
			// rhints.containsValue(RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		super.paintComponent(g);

		g.setFont(guessFont);
		for (int i = 0; i < Wordle.maxGuesses; i++) {
			for (int j = 0; j < w.numLetters; j++) {

				// color of box
				if (w.guesses.size() > i) {
					if (w.guesses.get(i)[j] == '+') {
						g.setColor(greenBox);
					} else if (w.guesses.get(i)[j] == '~') {
						g.setColor(yellowBox);
					} else {
						g.setColor(blackBox);
					}
				} else {
					g.setColor(boxColor);
				}
				// box
				g.fillRoundRect(
						(int) (leftOffset +
								j * (boxSize + horizGap)),
						(int) (topOffset +
								i * (boxSize + vertGap)),
						(int) boxSize, (int) boxSize,
						(int) (boxSize * roundness),
						(int) (boxSize * roundness));
				if (w.prevWords.size() > i) {
					Point p = fontStats(w.prevWords.get(i)[j].toString());
					p.x = (int) (boxSize / 2 - p.x / 2);
					p.y = (int) (boxSize / 2 - p.y / 2);

					g.setColor(prevGuessColor); // show the previous guesses
					g.drawString(
							w.prevWords.get(i)[j].toString(),
							(int) (p.x + leftOffset + j * (boxSize + horizGap)),
							(int) (p.y + topOffset + fontSize + i * (boxSize + vertGap)));
				}
				if (i == w.numGuesses && guess.size() > j) {
					Point p = fontStats(guess.get(j).toString());
					p.x = (int) (boxSize / 2 - p.x / 2);
					p.y = (int) (boxSize / 2 - p.y / 2);

					g.setColor(currentGuessColor); // shows current/in progress guess
					g.drawString(
							guess.get(j).toString(),
							(int) (p.x + leftOffset + j * (boxSize + horizGap)),
							(int) (p.y + topOffset + fontSize + i * (boxSize + vertGap)));
				}
			}
		}

		if (w.isWon) {
			g.setColor(greenBox);
			g.fillRect(0,
					this.getHeight() / 2 - (int) (fontSize * 1.5),
					this.getWidth(),
					(int) (fontSize * 1.4));

			String winMessage = "Congratulations!";
			g.setColor(currentGuessColor);
			g.setFont(guessFont);
			g.drawString(winMessage,
					this.getWidth() / 2 - g.getFontMetrics().stringWidth(winMessage) / 2,
					(int) (this.getHeight() / 2 - fontSize / 2.4));
		} else if (w.isLost) {
			g.setColor(wrongWord);
			g.fillRect(0,
					this.getHeight() / 2 - (int) (fontSize * 1.5),
					this.getWidth(),
					(int) (fontSize * 1.5));

			String loseMessage = "You lose!";
			g.setFont(guessFont);
			g.setColor(prevGuessColor);
			g.drawString(loseMessage,
					this.getWidth() / 2 - g.getFontMetrics().stringWidth(loseMessage) / 2,
					(int) (this.getHeight() / 2 - fontSize / 4));

			// String loseWord = Wordle.charArrayToString(w.correctWord).toUpperCase();
			// w.prevWords.remove(w.guesses.size()-1);
			// w.prevWords.add(w.correctWord);
			// g.setColor(wrongWord);
			// g.drawString(loseWord, this.getWidth() / 2 +
			// g.getFontMetrics().stringWidth(loseMessage) / 2, (int) (this.getHeight() / 2
			// + fontSize / 4));
		}
	}

	public void wordEntered() {
		if (w.isValidWord(guess)) {
			validWordEntered = true;
			w.makeGuess(Wordle.arrayListToArray(guess));
			guess.clear();
		} else {
			invalidWordEntered = true;
		}
	}

	void invalidAnimation() {
		// do some shit to indicate that the input is invalid
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			wordEntered = true;
			wordEntered();
			keyTyped = true;
		} else if (e.getKeyCode() == 8) {
			if (guess.size() > 0) {
				keyTyped = true;
				guess.remove(guess.size() - 1);
			}
		} else if (guess.size() < w.correctWord.length && ((e.getKeyCode() >= 65 && e.getKeyCode() <= 90)
				|| e.getKeyCode() == 45)) {
			keyTyped = true;
			guess.add(Character.toUpperCase(e.getKeyChar()));
		}
		// System.out.println(guess);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	static String arrayListToString(ArrayList<Character> al) {
		String converted = "";
		for (int i = 0; i < al.size(); i++)
			converted += al.get(i);
		return converted;
	}

	Point fontStats(String letter) {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
		int textWidth = (int) (guessFont.getStringBounds(letter, frc).getWidth());
		int textHeight = (int) (guessFont.getStringBounds(letter, frc).getHeight());

		return new Point(textWidth, textHeight);
	}
}
