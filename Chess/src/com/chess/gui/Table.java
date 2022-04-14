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

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
	private BoardDirection boardDirection;
	private boolean flipBoardOnTurnChange;
	private boolean highlightLegalMoves;
	private boolean highlightCaptures;
	Font menuFont;

	// TODO: make selectable themes for tile colors
	// NOTE: The highlight colors need to be customized theme-by-theme (or it'll look ugly)
	private final Color lightTileColor = new Color(240, 217, 181);
	private final Color darkTileColor = new Color(181, 136, 99);
	private final Color attackedPieceHighlightColor = new Color(243, 44, 44); // red
	private final Color selectedPieceHighlightColor = new Color(97, 224, 85); // green

	private static final float SELECTED_PIECE_HIGHLIGHT_OPACITY = .3f;
	private static final float ATTACKED_PIECE_HIGHLIGHT_OPACITY = .35f;
	private static final float PIECE_SCALE = .95f; // each piece is scaled to 95% of its square
	private static final float MOVE_INDICATOR_SCALE = .3f; // each dot is scaled to 30% of its square
	private static final String DEFAULT_PIECE_IMAGE_PATH = "art/pieces/";
	private static final String FRAME_ICON = "art/misc/twopieceicon.png";
	File listOfThemes = new File("art/pieceThemes.txt");
	ArrayList<String> pieceThemes = new ArrayList<>();
	private static String pieceTheme = "merida"; // default theme

	// TODO: make it dynamically scale the pieces AS you resize the window (not on click);
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(1000, 1000);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension((int) OUTER_FRAME_DIMENSION.getWidth(),
			(int) OUTER_FRAME_DIMENSION.getHeight());
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension((int) OUTER_FRAME_DIMENSION.getWidth() / 8,
			(int) OUTER_FRAME_DIMENSION.getHeight() / 8);

	public Table() {

		try {
			Scanner input = null;
			input = new Scanner(listOfThemes);
			while (input.hasNextLine()) {
				pieceThemes.add(input.nextLine());
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.gameFrame = new JFrame("HenryChess");
		this.gameFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(FRAME_ICON));
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

		this.chessBoard = Board.createStandardBoard();
		this.gameFrame.setResizable(true);
		this.gameFrame.setLocationRelativeTo(null); // centers on monitor
		this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.boardPanel = new BoardPanel();
		this.boardDirection = BoardDirection.NORMAL;
		this.flipBoardOnTurnChange = false;
		this.highlightLegalMoves = true;
		this.highlightCaptures = true;
		this.menuFont = new Font("segoe ui", Font.PLAIN, 16);

		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
		SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard)); // resizes pieces
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		tableMenuBar.add(createThemeMenu());

		tableMenuBar.setBorderPainted(false);
		tableMenuBar.setBackground(lightTileColor);
		// tableMenuBar.setFont(menuFont);
		tableMenuBar.validate();

		Component[] items = tableMenuBar.getComponents();

		for (int i = 0; i < items.length; i++) {
			JMenu item = (JMenu) tableMenuBar.getComponent(i);
			applyStyle(item);

			Component[] subMenuItems = item.getComponents();
			for (int j = 0; j < subMenuItems.length; j++) {
				JMenuItem menuItem = (JMenuItem) subMenuItems[j];
				applyStyle(menuItem);
			}
		}

		return tableMenuBar;
	}

	void applyStyle(Component item) {
		item.setBackground(lightTileColor);
		item.setFont(menuFont);
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");

		openPGN.addActionListener(e -> System.out.println("this dont work yet")); // TODO: fix this

		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(e -> System.exit(0));
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}

	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(e -> {
			boardDirection = boardDirection.opposite();
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});
		final JMenuItem flipBoardOnTurnChangeMenuItem = new JMenuItem("Active Player on Bottom");
		flipBoardOnTurnChangeMenuItem.addActionListener(e -> {
			flipBoardOnTurnChange = !flipBoardOnTurnChange;
			if (chessBoard.currentPlayer().getAlliance() != boardDirection.getBottomAlliance()) {
				boardDirection = boardDirection.opposite();
				SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
			}
		});
		final JCheckBox highlightLegalMovesMenuItem = new JCheckBox("Highlight legal moves", true);
		highlightLegalMovesMenuItem.addActionListener(e -> {
			highlightLegalMoves = !highlightLegalMoves;
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});
		final JCheckBox highlightCapturesMenuItem = new JCheckBox("Highlight captures", true);
		highlightLegalMovesMenuItem.addActionListener(e -> {
			highlightCaptures = !highlightCaptures;
			SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
		});
		preferencesMenu.add(highlightLegalMovesMenuItem);
		preferencesMenu.add(highlightCapturesMenuItem);
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.add(flipBoardOnTurnChangeMenuItem);
		return preferencesMenu;
	}

	private JMenu createThemeMenu() {
		// TODO: for some reason, these themes don't center:
		// cburnett, chessmonk, chess7, freestaunton, horsey, icpieces, kilfiger,
		// makruk, metaltops

		// all of the others DO center
		// you can't really tell in a square tile, but it's apparent when they're
		// rectangles

		final JMenu themeMenu = new JMenu("Theme");
		for (int i = 0; i < pieceThemes.size(); i++) {
			String label = pieceThemes.get(i).substring(0,1).toUpperCase() + pieceThemes.get(i).substring(1);
			JMenuItem item = new JMenuItem(label);
			themeMenu.add(item);
			themeMenu.getItem(i).addActionListener(e -> {
				setTheme(item.getText().toLowerCase());
				SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
			});
		}
		return themeMenu;
	}

	private static void setTheme(String theme) {
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
			setPreferredSize(BOARD_PANEL_DIMENSION);
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

			addMouseListener(new MouseListener() {

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
						if (flipBoardOnTurnChange
								&& chessBoard.currentPlayer().getAlliance() != boardDirection.getBottomAlliance()) {
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

				public boolean isFriendlyOrEmpty() {
					return (sourceTile == null || (chessBoard.getTile(tileID).isOccupied() && chessBoard.getTile(tileID)
							.getPiece().getAlliance() == chessBoard.currentPlayer().getAlliance()));
				}

				public boolean isEnemyOrEmpty() {
					return (sourceTile == null || (chessBoard.getTile(tileID).isOccupied() && chessBoard.getTile(tileID)
							.getPiece().getAlliance() != chessBoard.currentPlayer().getAlliance()));
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						deselectPiece();
					} else if (SwingUtilities.isLeftMouseButton(e)) {
						if (isFriendlyOrEmpty()) {
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
					// consequently, you have to send a global boolean for both the mouseReleased
					// event and isDragging event
					// if both are met, try placing the piece on the currently hovered tile
					// TODO: Add drag and drop functionality
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					if (isFriendly()) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // hand cursor
					}

					if (humanMovedPiece != null && isEnemy()
							&& humanMovedPiece.calculateLegalSquares(chessBoard).contains(Integer.valueOf(tileID))) {
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

		public void highlightTile(Color highlightColor) {
			Color lightHighlightedColor = Utils.mixColors(lightTileColor, highlightColor);
			Color darkHighlightedColor = Utils.mixColors(darkTileColor, highlightColor);
			boolean isLight = (((tileID / 8) + tileID) % 2 == 0);
			setBackground(isLight ? lightHighlightedColor : darkHighlightedColor);
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

		float pieceWidth = (float) (PIECE_SCALE * TILE_PANEL_DIMENSION.getHeight());
		float pieceHeight = (float) (PIECE_SCALE * TILE_PANEL_DIMENSION.getWidth());

		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if (board.getTile(this.tileID).isOccupied()) {

				if (this.getHeight() != 0 && this.getWidth() != 0) {
					pieceHeight = this.getHeight() * PIECE_SCALE;
					pieceWidth = this.getWidth() * PIECE_SCALE;
				}

				File pieceFile = new File(DEFAULT_PIECE_IMAGE_PATH + pieceTheme + "/"
						+ board.getTile(this.tileID).getPiece().getAlliance().toString().substring(0, 1)
						+ board.getTile(this.tileID).getPiece().toString() + ".svg");
				final BufferedImage image = Utils.loadImage(pieceFile, pieceWidth, pieceHeight);
				add(new JLabel(new ImageIcon(image)));
			}

		}

		float dotWidth = (float) (MOVE_INDICATOR_SCALE * OUTER_FRAME_DIMENSION.getHeight() / 8);
		float dotHeight = (float) (MOVE_INDICATOR_SCALE * OUTER_FRAME_DIMENSION.getWidth() / 8);

		private void highlightSelectedPiece() {
			if (true /* TODO: add toggle in preferences */ && humanMovedPiece != null
					&& humanMovedPiece.getPiecePosition() == this.tileID) {
				highlightTile(SELECTED_PIECE_HIGHLIGHT_OPACITY, selectedPieceHighlightColor);
			}
		}

		private void highlightLegals(final Board board) {
			// TODO: add castling, remove moves that would cause a check (illegal)

			if (highlightLegalMoves) {
				for (final Move move : pieceLegalMoves(board)) {
					if (move.getDestinationCoordinate() == this.tileID && !move.isAttack()) {
						try {
							File dotFile = new File("art/misc/circle_filled.svg");
							final BufferedImage image = Utils.loadImage(dotFile, dotWidth, dotHeight);
							add(new JLabel(new ImageIcon(image)));
							// TODO: add transparency to the dots (or fake it)
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (move.getDestinationCoordinate() == this.tileID && move.isAttack()
							&& highlightCaptures) {
						highlightTile(ATTACKED_PIECE_HIGHLIGHT_OPACITY, attackedPieceHighlightColor);
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
