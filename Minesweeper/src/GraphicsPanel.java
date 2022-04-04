import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel implements MouseListener {

	// TODO: make the numbers images, otherwise they make it so the board can't
	// scale
	// and having the numbers as text strings is pretty awful to work with

	boolean hasDividingLines = true;

	int NUM_ROWS;
	int NUM_COLS;
	double THIK = 20; // line thickness constant (lower is thicker, tictactoe was 10)
	double tileSize; // to determine the size of the squares
	double lineWidth; // to determine the width of the divider lines
	double pieceSize;
	final double PIECE_SCALING = .75; // .75 is pretty good
	double lineLength;
	int xOffset;
	int yOffset; // to center the board in the window
	int width;
	int height;

	int rowClicked;
	int colClicked;
	boolean isClicked;
	boolean isLeftClick;
	boolean isRightClick;
	boolean isOtherClick;
	boolean hasLost;
	boolean hasWon;
	String winMessage = "pog";
	String loseMessage = "yikes";

	BufferedImage bomb = null;
	BufferedImage flag = null;
	BufferedImage tile = null;
	BufferedImage[] nums = null;

	Cell[][] board;
	SoundPlayer sound = new SoundPlayer();
	String[] sfx = new String[] { "Vine Boom.wav", "Bonk.wav", "Metal pipe falling.wav", "Wet fart.wav" };
	// should get some actual sounds
	// or at least new funnies

	// Color bgColor = new Color(248,220,180); // creamy white
	Color bgColor = Color.gray;
	Color susRed = new Color(215, 30, 34); // amogus red
	Color flagRed = new Color(248, 52, 4); // same red as flag
	Color susCyan = new Color(68, 255, 247); // amogus cyan
	Color susTie = new Color(255, 255, 255); // white

	Color lightSquareColor = new Color(62, 195, 182);
	Color darkSquareColor = lightSquareColor.darker();
	Color lightRevealedColor = new Color(27, 45, 81);
	Color darkRevealedColor = lightRevealedColor.darker();

	Color[] numColors = new Color[] { new Color(168, 168, 168), new Color(17, 107, 171), new Color(224, 64, 25),
			new Color(120, 16, 16), new Color(250, 155, 40), new Color(250, 236, 40), new Color(250, 40, 229) };
	int FONT_SIZE = 30;
	Font winFont = new Font("Comic Sans MS", Font.BOLD, FONT_SIZE);
	Font numFont;

	public GraphicsPanel(Dimension size, Cell[][] b) {
		this.setPreferredSize(size);
		this.setBackground(bgColor);
		this.setFocusable(true);
		this.addMouseListener(this);
		board = b.clone();

		NUM_ROWS = board.length;
		NUM_COLS = board[0].length;
		// for non square boards
		resize();

		numFont = new Font("Fira Code", Font.BOLD, (int) (pieceSize * 100));

		try {
			bomb = ImageIO.read(this.getClass().getResource("bomb.png"));
			flag = ImageIO.read(this.getClass().getResource("Flag.png"));
			// for (int n = 0; n < nums.length; n++) {
			// nums[n] = ImageIO.read(this.getClass().getResource(n+".png"));
			// }
			// tile = ImageIO.read(this.getClass().getResource("mine craft smile.jpg"));
		} catch (IOException ex) {
			System.out.println(ex);
		}

	}

	public void resize() {
		// note: only works for square boards
		height = this.getHeight();
		width = this.getWidth();
		if (this.getWidth() > this.getHeight()) {
			tileSize = this.getHeight() / NUM_ROWS;
			lineWidth = this.getHeight() / (NUM_ROWS * THIK);
			lineLength = this.getHeight();
			yOffset = 0;
			xOffset = (this.getWidth() - this.getHeight()) / 2;
		} else {
			tileSize = 1 + this.getWidth() / NUM_ROWS;
			lineWidth = 1 + this.getWidth() / (NUM_ROWS * THIK);
			lineLength = this.getWidth();
			xOffset = 0;
			yOffset = (this.getHeight() - this.getWidth()) / 2;
		}
		pieceSize = tileSize * PIECE_SCALING;
	}

	public void setPanelSize(int i) {
		height = i;
		width = i;
	}

	public void refresh(Cell[][] b) {
		for (int r = 0; r < b.length; r++) {
			for (int c = 0; c < b[r].length; c++) {
				board[r][c] = b[r][c];
			}
		}
		if (this.getWidth() != width || this.getHeight() != height) {
			resize();
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Integer cellVal; // -3 is flag, -2 is unrevealed, -1 is bomb
		if (board[0][0].isLost) {
			hasLost = true;
		} else if (board[0][0].isWon) {
			hasWon = true;
		}
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				cellVal = Integer.valueOf(board[r][c].show());

				// shades tiles
				if (cellVal > -2 || hasLost) {
					if ((r + c) % 2 == 0) {
						g.setColor(lightRevealedColor);
					} else {
						g.setColor(darkRevealedColor);
					}
				} else {
					if ((r + c) % 2 == 0) {
						g.setColor(lightSquareColor);
					} else {
						g.setColor(darkSquareColor);
					}
				}
				g.fillRect((int) ((c * tileSize) + xOffset), (int) ((r * tileSize) + yOffset), (int) tileSize,
						(int) tileSize);
				// tiles shaded

				if (cellVal == -3) {
					g.drawImage(flag, (int) ((c * tileSize) + (tileSize / 2) - pieceSize / 2) + xOffset,
							(int) ((r * tileSize) + (tileSize / 2) - pieceSize / 2) + yOffset, (int) pieceSize,
							(int) pieceSize, this);
				} else if (cellVal == -1) {
					g.setColor(flagRed);
					g.drawImage(bomb, (int) ((c * tileSize) + (tileSize / 2) - pieceSize / 2) + xOffset,
							(int) ((r * tileSize) + (tileSize / 2) - pieceSize / 2) + yOffset, (int) pieceSize,
							(int) pieceSize, this);
				} else if (cellVal > 0) {
					// need to make it an image, making it a font doesn't work, unfortunately
					g.setFont(numFont);
					g.setColor(numColors[cellVal - 1]);
					g.drawString(cellVal.toString(), (int) (((c) * tileSize) + xOffset + tileSize / 4),
							(int) (((r + 1) * tileSize) + yOffset - tileSize / 5.5));
				}
			}
		}

		// dividing lines
		if (hasDividingLines) {
			g.setColor(Color.black);
			for (int i = 0; i < NUM_ROWS + 1; i++) {
				g.fillRect((int) (i * tileSize - lineWidth / 2) + xOffset, yOffset, (int) lineWidth, (int) lineLength);
			}
			for (int i = 0; i < NUM_COLS + 1; i++) {
				g.fillRect(xOffset, (int) (i * tileSize - lineWidth / 2) + yOffset, (int) lineLength, (int) lineWidth);
			}
		}
		// done w/ dividing lines
		if (hasLost || hasWon) {
			g.setColor(Color.BLACK);
			g.fillRect(0, this.getHeight() / 2 - (int) (FONT_SIZE * 1.5), this.getWidth(), (int) (FONT_SIZE * 1.5));
			g.setFont(winFont);
			if (hasLost) {
				sound.play("vine boom.wav");
				g.setColor(susRed);
				g.drawString(loseMessage, this.getWidth() / 2 - g.getFontMetrics().stringWidth(loseMessage) / 2,
						this.getHeight() / 2 - FONT_SIZE / 4);
			} else {
				sound.play("congratulations.wav");
				g.setColor(susCyan);
				g.drawString(winMessage, this.getWidth() / 2 - g.getFontMetrics().stringWidth(winMessage) / 2,
						this.getHeight() / 2 - FONT_SIZE / 4);
			}
		}
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean b) {
		isClicked = b;
	}

	public int getRowClicked() {
		if (rowClicked >= 0 && rowClicked < NUM_ROWS) {
			return rowClicked;
		}
		return -1;
	}

	public int getColClicked() {
		if (colClicked >= 0 && colClicked < NUM_COLS) {
			return colClicked;
		}
		return -1;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		colClicked = (e.getX() - xOffset) / (int) tileSize;
		rowClicked = (e.getY() - yOffset) / (int) tileSize;
		isClicked = true;
		if (e.getButton() == 1) {
			isLeftClick = true;
		} else if (e.getButton() == 3) {
			isRightClick = true;
		} else {
			isOtherClick = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
