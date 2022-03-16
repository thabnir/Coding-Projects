package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

public class Table {

	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;

	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;

	private final Color lightTileColor = new Color(235, 235, 208);
	private final Color darkTileColor = new Color(119, 148, 85);

	private static String defaultPieceImagesPath = "art/piecespng/";
	String pieceTheme = "merida/";
	String fileType = ".png";

	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(650, 650);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(450, 450);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

	public Table() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStandardBoard();
		this.gameFrame.setResizable(true);
		this.gameFrame.setLocationRelativeTo(null); // centers it upon opening
		this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.boardPanel = new BoardPanel();
		this.boardDirection = BoardDirection.NORMAL;
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open the pgn"); // TODO: Fix this
			}
		});
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}

	private JMenu createPreferencesMenu() {

		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(e -> {
			boardDirection = boardDirection.opposite();
			boardPanel.drawBoard(chessBoard);
		});
		preferencesMenu.add(flipBoardMenuItem);
		return preferencesMenu;
	}

	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;

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
			// TODO: make the squares scale correctly (ALWAYS squares)

			super(new GridBagLayout());
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);

			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {

					/*
					 * System.out.println("b:" + chessBoard.blackPlayer().getLegalMoves().size() +
					 * " w: " + chessBoard.whitePlayer().getLegalMoves().size());
					 */

					if (SwingUtilities.isRightMouseButton(e)) {
						// clear on right click
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					} else if (SwingUtilities.isLeftMouseButton(e)) {
						// first click
						if (sourceTile == null || (chessBoard.getTile(tileID).isOccupied() && chessBoard.getTile(tileID)
								.getPiece().getAlliance() == chessBoard.currentPlayer().getAlliance())) {
							sourceTile = chessBoard.getTile(tileID);
							humanMovedPiece = sourceTile.getPiece();
							if (humanMovedPiece == null) {
								sourceTile = null;
							}
						} else {
							// second click
							destinationTile = chessBoard.getTile(tileID);
							final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
									destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if (transition.getMoveStatus().isDone()) {
								chessBoard = transition.getTransitionBoard();
								// TODO: add the move to the move log
							}
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
					}

					SwingUtilities.invokeLater(() -> {
						boardPanel.drawBoard(chessBoard);
					});
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					/* TODO: Add drag and drop functionality */}

				@Override
				public void mouseReleased(final MouseEvent e) {
					/* TODO: Add drag and drop functionality */}

				@Override
				public void mouseEntered(final MouseEvent e) {
					/* TODO: Add highlighting during drag and drop */ };

				@Override
				public void mouseExited(final MouseEvent e) {
					/* TODO: Add highlighting during drag and drop */ };

			});
			validate();
		}

		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
			validate();
			repaint();
		}

		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if (board.getTile(this.tileID).isOccupied()) {
				try {
					// TODO: make this not be a blurry mess
					// TODO: support .svg files
					// TODO: make pieces scale with the squares
					final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + pieceTheme
							+ board.getTile(this.tileID).getPiece().getAlliance().toString().substring(0, 1)
							+ board.getTile(this.tileID).getPiece().toString() + fileType));
					add(new JLabel(new ImageIcon(image.getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH))));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		private void highlightLegals(final Board board) {
			if (true /* TODO: add this option in preferences */) {
				for (final Move move : pieceLegalMoves(board)) {
					if (move.getDestinationCoordinate() == this.tileID) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/dot.png")))));
							// TODO: make this not look like garbage
							// TODO: make it not shift the piece in the space to the side
						} catch (Exception e) {
							e.printStackTrace();
						}
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

		private void assignTileColor() {
			boolean isLight = (((tileID / 8) + tileID) % 2 == 0);
			setBackground(isLight ? lightTileColor : darkTileColor);
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
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				List<TilePanel> reversedCopy = new ArrayList<TilePanel>();
				reversedCopy.addAll(boardTiles); // clones
				Collections.reverse(reversedCopy); // reverses
				return reversedCopy;
			}

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};

		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

		abstract BoardDirection opposite();

	}

}
