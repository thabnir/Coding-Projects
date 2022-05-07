package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.chess.Utils;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

public class Table {

	// alpha, magnetic have broken black pawns, pixel has gaps at lower resolutions

	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;

	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;

	// TODO: add theming to all the menus
	// TODO: fix blurriness issue
	// TODO: find out why some SVGs don't center
	// TODO: make the tiles actually squares, make the board actually square
	// TODO: remove illegal move indicators
	// TODO: make it dynamically scale the pieces AS you resize the window (not on
	// click);

	// TODO: make theme menu not suck ass (it's way too big)
	// could make it scroll, could make it its own type of menu, whatever

	// TODO: add new game button
	// TODO: add transparency to the move indicator dots (or fake it)
	// TODO: add indicators for castling
	// TODO: add letters + numbers for rows + columns
	// TODO: add selectable themes for tile colors
	// The highlight colors need to be customized theme-by-theme (or it looks bad)

	private BoardDirection boardDirection;
	private boolean flipBoardOnTurnChange;
	private boolean highlightLegalMoves;
	private boolean highlightCaptures;
	private Font menuFont;
	private Font menuFontBold;

	private final Color lightTileColor;
	private final Color darkTileColor;
	private final Color attackedTileHighlightColor;
	private final Color selectedPieceHighlightColor;

	private float selectedPieceHighlightOpacity;
	private float attackedTileHighlightOpacity;
	private float pieceScale;
	private float moveIndicatorScale;

	private String pieceTheme;
	private Preferences userPreferences = Preferences.userRoot().node(this.getClass().getName());

	private static final String DEFAULT_PIECE_IMAGE_PATH = "art/pieces/";
	private static final String FRAME_ICON = "art/misc/twopieceicon.png";

	public static final String HIGHLIGHT_LEGAL_MOVES = "highlightLegalMoves";
	public static final String HIGHLIGHT_CAPTURES = "highlightCaptures";
	public static final String FLIP_BOARD_ON_TURN_CHANGE = "flipBoardOnTurnChange";
	public static final String PIECE_THEME = "pieceTheme";
	public static final String DEFAULT_THEME = "gioco";

	private static final File listOfThemes = new File("art/pieceThemes.txt");
	private static final ArrayList<String> pieceThemes = getThemes();

	static int width = 700;
	static int height = width + 23;

	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(width, height);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension((int) OUTER_FRAME_DIMENSION.getWidth(),
			(int) OUTER_FRAME_DIMENSION.getWidth());
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension((int) BOARD_PANEL_DIMENSION.getWidth() / 8,
			(int) BOARD_PANEL_DIMENSION.getHeight() / 8);

	public Table() {

		try {
			String[] keys = userPreferences.keys();
			if (keys == null || keys.length == 0) {
				resetPreferences();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPreferences();

		this.menuFont = new Font("segoe ui", Font.PLAIN, 16);
		this.menuFontBold = new Font("segoe ui", Font.BOLD, 16);

		UIManager.put("Menu.font", menuFont); // TODO : make fonts do stuff

		this.lightTileColor = new Color(240, 217, 181);
		this.darkTileColor = new Color(181, 136, 99);
		this.attackedTileHighlightColor = new Color(243, 44, 44);
		this.selectedPieceHighlightColor = new Color(97, 224, 85);
		this.selectedPieceHighlightOpacity = .3f;
		this.attackedTileHighlightOpacity = .35f;
		this.pieceScale = .95f;
		this.moveIndicatorScale = .3f;
		this.boardDirection = BoardDirection.NORMAL;

		this.chessBoard = Board.createStandardBoard();

		this.gameFrame = new JFrame("HenryChess");
		this.gameFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(FRAME_ICON));
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

		this.gameFrame.setResizable(true);
		this.gameFrame.setLocationRelativeTo(null); // centers on monitor
		this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.boardPanel = new BoardPanel();

		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
		SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard)); // resizes pieces

	}

	void resetPreferences() {
		try {
			userPreferences.clear();
			userPreferences.put(PIECE_THEME, DEFAULT_THEME);
			userPreferences.putBoolean(HIGHLIGHT_LEGAL_MOVES, true);
			userPreferences.putBoolean(HIGHLIGHT_CAPTURES, true);
			userPreferences.putBoolean(FLIP_BOARD_ON_TURN_CHANGE, false);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		setPreferences();
	}

	void setPreferences() {
		this.pieceTheme = userPreferences.get(PIECE_THEME, DEFAULT_THEME);
		this.highlightLegalMoves = userPreferences.getBoolean(HIGHLIGHT_LEGAL_MOVES, true);
		this.highlightCaptures = userPreferences.getBoolean(HIGHLIGHT_CAPTURES, true);
		this.flipBoardOnTurnChange = userPreferences.getBoolean(FLIP_BOARD_ON_TURN_CHANGE, false);
	}

	static ArrayList<String> getThemes() {
		ArrayList<String> themes = new ArrayList<>();
		try {
			Scanner input = null;
			input = new Scanner(listOfThemes);
			while (input.hasNextLine()) {
				themes.add(input.nextLine());
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return themes;
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		tableMenuBar.add(createThemeMenu());

		tableMenuBar.setBorderPainted(false);
		applyStyle(tableMenuBar);
		// tableMenuBar.setfont
		// tableMenuBar.setBackground(lightTileColor);
		// tableMenuBar.setFont(menuFontBold);

		// TODO: figure out why the commented out code doesn't work

		// Component[] items = tableMenuBar.getComponents();
		// for (int i = 0; i < items.length; i++) {
		// JMenu item = (JMenu) tableMenuBar.getComponent(i);
		// applyStyle(item);
		//
		// Component[] subMenuItems = item.getComponents();
		// for (int j = 0; j < subMenuItems.length; j++) {
		// JMenuItem menuItem = (JMenuItem) subMenuItems[j];
		// applyStyle(menuItem);
		// }
		// }

		tableMenuBar.validate();
		return tableMenuBar;
	}

	private void applyStyle(Component item) {
		item.setBackground(lightTileColor);
		item.setFont(menuFont);
	}

	private JMenu createFileMenu() {

		// TODO: apply theming

		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");

		openPGN.addActionListener(e -> System.out.println("this dont work yet")); // TODO: fix this

		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(e -> System.exit(0));
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);

		applyStyle(openPGN);
		applyStyle(exitMenuItem);
		return fileMenu;
	}

	private JMenu createPreferencesMenu() {

		// TODO: apply theming

		final JMenu preferencesMenu = new JMenu("Preferences");

		final JCheckBox flipBoardOnTurnChangeMenuItem = new JCheckBox("Active Player on Bottom",
				userPreferences.getBoolean(FLIP_BOARD_ON_TURN_CHANGE, flipBoardOnTurnChange));
		flipBoardOnTurnChangeMenuItem.addActionListener(e -> {
			flipBoardOnTurnChange = !flipBoardOnTurnChange;
			userPreferences.remove(FLIP_BOARD_ON_TURN_CHANGE);
			userPreferences.putBoolean(FLIP_BOARD_ON_TURN_CHANGE, flipBoardOnTurnChange);
			if (chessBoard.currentPlayer().getAlliance() != boardDirection.getBottomAlliance()
					|| (chessBoard.currentPlayer().getAlliance() != Alliance.WHITE
							&& !userPreferences.getBoolean(FLIP_BOARD_ON_TURN_CHANGE, flipBoardOnTurnChange))) {
				boardDirection = boardDirection.opposite();
				SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
			}
		});

		final JCheckBox highlightLegalMovesMenuItem = new JCheckBox("Highlight Legal Moves",
				userPreferences.getBoolean(HIGHLIGHT_LEGAL_MOVES, highlightLegalMoves));
		highlightLegalMovesMenuItem.addActionListener(e -> {
			highlightLegalMoves = !highlightLegalMoves;
			userPreferences.remove(HIGHLIGHT_LEGAL_MOVES);
			userPreferences.putBoolean(HIGHLIGHT_LEGAL_MOVES, highlightLegalMoves);
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});

		final JCheckBox highlightCapturesMenuItem = new JCheckBox("Highlight Captures",
				userPreferences.getBoolean(HIGHLIGHT_CAPTURES, highlightCaptures));
		highlightCapturesMenuItem.addActionListener(e -> {
			highlightCaptures = !highlightCaptures;
			userPreferences.remove(HIGHLIGHT_CAPTURES);
			userPreferences.putBoolean(HIGHLIGHT_CAPTURES, highlightCaptures);
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});

		final JCheckBox flipBoardMenuItem = new JCheckBox("Flip Board", false);
		flipBoardMenuItem.addActionListener(e -> {
			boardDirection = boardDirection.opposite();
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});

		final JMenuItem resetPreferencesMenuItem = new JMenuItem("Reset");
		resetPreferencesMenuItem.addActionListener(e -> {
			resetPreferences();
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});

		preferencesMenu.add(highlightLegalMovesMenuItem);
		preferencesMenu.add(highlightCapturesMenuItem);
		preferencesMenu.add(flipBoardOnTurnChangeMenuItem);
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.add(resetPreferencesMenuItem);

		applyStyle(highlightLegalMovesMenuItem);
		applyStyle(highlightCapturesMenuItem);
		applyStyle(flipBoardOnTurnChangeMenuItem);
		applyStyle(flipBoardMenuItem);
		applyStyle(resetPreferencesMenuItem);

		return preferencesMenu;
	}

	private JMenu createThemeMenu() {

		// TODO: for some reason, these themes don't center:
		// cburnett, chessmonk, chess7, freestaunton, horsey, icpieces, kilfiger,
		// makruk, metaltops

		// all of the others DO center
		// you can't really tell in a square tile, but it's very apparent when they're
		// rectangles

		final JMenu themeMenu = new JMenu("Theme");
		ButtonGroup radioGroup = new ButtonGroup();
		for (int i = 0; i < pieceThemes.size(); i++) {
			String label = pieceThemes.get(i).substring(0, 1).toUpperCase() + pieceThemes.get(i).substring(1);
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(label);
			radioGroup.add(item);
			item.setSelected(label.toLowerCase().equals(pieceTheme));
			applyStyle(item);
			themeMenu.add(item);

			themeMenu.getItem(i).addActionListener(e -> {
				setTheme(item.getText().toLowerCase());
				JRadioButtonMenuItem button = (JRadioButtonMenuItem) e.getSource();
				applyStyle(button);
				item.setFont(button.getText().equals(pieceTheme) ? menuFontBold : menuFont);
				userPreferences.remove(PIECE_THEME);
				userPreferences.put(PIECE_THEME, pieceTheme);
				SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
			});
		}
		return themeMenu;
	}

	void setTheme(String theme) {
		pieceTheme = theme;
	}

	private class BoardPanel extends JPanel {

		private final List<TilePanel> boardTiles;

		BoardPanel() {
			super(new GridLayout(8, 8));
			this.boardTiles = new ArrayList<>();
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			this.setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(final Board board) {
			removeAll();
			for (final TilePanel boardTile : boardDirection.traverse(boardTiles)) {
				boardTile.drawTile(board);
				add(boardTile);
			}
			validate();
			repaint();
		}
	}

	private class TilePanel extends JPanel {

		private final int tileID;

		TilePanel(final BoardPanel boardPanel, final int tileID) {
			// TODO: make the squares scale correctly (ALWAYS stay squares)

			super(new GridBagLayout());

			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);

			// TODO: refactor this garbage (me omw to write a 100 line anonymous class)
			addMouseListener(
					new MouseListener() {

						public void deselectPiece() {
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}

						public void selectPiece() {
							sourceTile = chessBoard.getTile(tileID);
							humanMovedPiece = sourceTile.getPiece();
							if (humanMovedPiece == null
									|| humanMovedPiece.getAlliance() != chessBoard.currentPlayer().getAlliance()) {
								sourceTile = null;
								humanMovedPiece = null;
							}
						}

						public void placePiece() {
							destinationTile = chessBoard.getTile(tileID);
							final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
									destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

							if (transition.getMoveStatus().isDone()) {
								chessBoard = transition.getToBoard();
								setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
								if (flipBoardOnTurnChange && chessBoard.currentPlayer().getAlliance() != boardDirection
										.getBottomAlliance()) {
									boardDirection = boardDirection.opposite();
								}
								// TODO: add the move to the move log
							}

							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}

						public boolean isFriendly() {
							return (chessBoard.getTile(tileID).isOccupied() && chessBoard.getTile(tileID).getPiece()
									.getAlliance() == chessBoard.currentPlayer().getAlliance());
						}

						public boolean isEnemy() {
							return (chessBoard.getTile(tileID).isOccupied() && chessBoard.getTile(tileID).getPiece()
									.getAlliance() != chessBoard.currentPlayer().getAlliance());
						}

						public boolean isEmpty() {
							return sourceTile == null;
						}

						@Override
						public void mousePressed(final MouseEvent e) {
							if (SwingUtilities.isRightMouseButton(e)) {
								deselectPiece();
							} else if (SwingUtilities.isLeftMouseButton(e)) {
								if (isEmpty() || isFriendly()) {
									selectPiece();
								} else {
									placePiece();
								}
							}
							SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
							// TODO: Add drag and drop functionality
						}

						@Override
						public void mouseReleased(final MouseEvent e) {
							// it only recognizes that you've released it from THIS tile
							// consequently, you have to send a global event for both the mouseReleased
							// event and isDragging event
							// if both are met, try placing the piece on the currently hovered tile
							// TODO: Add drag and drop functionality
						}

						@Override
						public void mouseEntered(final MouseEvent e) {
							if (isFriendly()) {
								setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // hand cursor
							}

							if (humanMovedPiece != null && isEnemy() && humanMovedPiece
									.calculateLegalSquares(chessBoard).contains(Integer.valueOf(tileID))) {
								setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // default cursor
							}
						}

						@Override
						public void mouseExited(final MouseEvent e) {
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							// TODO: Add highlighting during drag and drop
						}

						@Override
						public void mouseClicked(final MouseEvent e) {
							// don't need, mousePressed + mouseReleased work better
						}

					});
			validate();
		}

		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
			highlightSelectedPiece();
			validate();
			repaint();
		}

		public void highlightTile(float primaryRatio, Color highlightColor) {
			Color lightHighlightedColor = Utils.mixColors(primaryRatio, lightTileColor, highlightColor);
			Color darkHighlightedColor = Utils.mixColors(primaryRatio, darkTileColor, highlightColor);
			boolean isLight = (((tileID / 8) + tileID) % 2 == 0);
			setBackground(isLight ? lightHighlightedColor : darkHighlightedColor);
		}

		private void assignTileColor() {
			boolean isLight = (((tileID / 8) + tileID) % 2 == 0);
			setBackground(isLight ? lightTileColor : darkTileColor);
		}

		float pieceWidth = (float) (pieceScale * TILE_PANEL_DIMENSION.getHeight());
		float pieceHeight = (float) (pieceScale * TILE_PANEL_DIMENSION.getWidth());

		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if (board.getTile(this.tileID).isOccupied()) {

				if (this.getHeight() != 0 && this.getWidth() != 0) {
					pieceHeight = this.getHeight() * pieceScale;
					pieceWidth = this.getWidth() * pieceScale;
				}

				File pieceFile = new File(DEFAULT_PIECE_IMAGE_PATH + pieceTheme + "/"
						+ board.getTile(this.tileID).getPiece().getAlliance().toString().substring(0, 1)
						+ board.getTile(this.tileID).getPiece().toString() + ".svg");
				final BufferedImage image = Utils.loadImage(pieceFile, pieceWidth, pieceHeight);
				add(new JLabel(new ImageIcon(image)));
			}
		}

		float dotWidth = (float) (moveIndicatorScale * TILE_PANEL_DIMENSION.getHeight());
		float dotHeight = (float) (moveIndicatorScale * TILE_PANEL_DIMENSION.getWidth());

		private void highlightSelectedPiece() {
			if (true /* TODO: add toggle in preferences */ && humanMovedPiece != null
					&& humanMovedPiece.getPiecePosition() == this.tileID) {
				highlightTile(selectedPieceHighlightOpacity, selectedPieceHighlightColor);
			}
		}

		private void highlightLegals(final Board board) {
			// TODO: figure out why i'm doing ALL of this for EVERY SINGLE TILE
			// TODO: add castling, remove moves that would cause a check (illegal)
			if (highlightLegalMoves || highlightCaptures) {

				if (this.getHeight() != 0 && this.getWidth() != 0) {
					dotHeight = this.getHeight() * moveIndicatorScale;
					dotWidth = this.getWidth() * moveIndicatorScale;
				}

				for (final Move move : pieceLegalMoves(board)) {
					if (move.getDestinationCoordinate() == this.tileID && !move.isAttack() && highlightLegalMoves) {
						try {
							File dotFile = new File("art/misc/circle_filled.svg");
							final BufferedImage image = Utils.loadImage(dotFile, dotWidth, dotHeight);
							add(new JLabel(new ImageIcon(image)));
							// TODO: add transparency to the dots (or fake it)
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (move.getDestinationCoordinate() == this.tileID && move.isAttack() && highlightCaptures) {
						highlightTile(attackedTileHighlightOpacity, attackedTileHighlightColor);
					}
				}

			}
		}

		private Collection<Move> pieceLegalMoves(final Board board) {
			if (humanMovedPiece != null && humanMovedPiece.getAlliance() == board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}
	}

	public enum BoardDirection {
		NORMAL {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				return boardTiles;
			}

			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}

			@Override
			Alliance getBottomAlliance() {
				return Alliance.WHITE;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				List<TilePanel> reversedCopy = new ArrayList<>();
				reversedCopy.addAll(boardTiles); // clones
				Collections.reverse(reversedCopy); // reverses
				return reversedCopy;
			}

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}

			@Override
			Alliance getBottomAlliance() {
				return Alliance.BLACK;
			}
		};

		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

		abstract BoardDirection opposite();

		abstract Alliance getBottomAlliance();

	}

}
