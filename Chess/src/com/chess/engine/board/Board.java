package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class Board {

	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;

	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;

	private Board(final Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);

		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));

			if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	public Collection<Piece> getBlackPieces() {
		return this.blackPieces;
	}

	public Collection<Piece> getWhitePieces() {
		return this.whitePieces;
	}

	public Player whitePlayer() {
		return whitePlayer;
	}

	public Player blackPlayer() {
		return blackPlayer;
	}

	private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return Collections.unmodifiableList(legalMoves);
	}

	private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();

		for (final Tile tile : gameBoard) {
			if (tile.isOccupied()) {
				final Piece piece = tile.getPiece();
				if (piece.getAlliance() == alliance) {
					activePieces.add(piece);
				}
			}
		}
		return Collections.unmodifiableList(activePieces);
	}

	public Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}

	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return Collections.unmodifiableList(Arrays.asList(tiles));
	}

	public static Board createStandardBoard() {
		return createBoardFromFen(BoardUtils.startFen);
	}

	public static Board createBoardFromFen(String fenString) {
		Builder builder = new Builder();
		String[] fenParts = fenString.split(" ");
		char[][] fen = new char[fenParts.length][];

		for (int i = 0; i < fenParts.length; i++) {
			fen[i] = fenParts[i].toCharArray();
		}

		int row = 0;
		int col = 0;

		// PLACES PIECES:
		for (char symbol : fen[0]) {
			Alliance pieceAlliance = Character.isUpperCase(symbol) ? Alliance.WHITE : Alliance.BLACK;
			final int coordinate = row * 8 + col;

			if (Character.isDigit(symbol)) {
				col += Character.getNumericValue(symbol); // skip n tiles forward
			} else {
				switch (Character.toUpperCase(symbol)) {
					case ' ':
						System.out.println("Pieces set!");
						break;
					case '/':
						col = 0;
						row++;
						break;
					case 'P':
						builder.setPiece(new Pawn(pieceAlliance, coordinate));
						col++;
						break;
					case 'B':
						builder.setPiece(new Bishop(pieceAlliance, coordinate));
						col++;
						break;
					case 'N':
						builder.setPiece(new Knight(pieceAlliance, coordinate));
						col++;
						break;
					case 'R':
						builder.setPiece(new Rook(pieceAlliance, coordinate));
						col++;
						break;
					case 'Q':
						builder.setPiece(new Queen(pieceAlliance, coordinate));
						col++;
						break;
					case 'K':
						builder.setPiece(new King(pieceAlliance, coordinate));
						col++;
						break;
					default:
						System.out.println("The switch case for setting pieces in board.java broke");
						break;
				}
			}
		}

		// READS TURN:
		if (fen[1][0] == 'w') {
			builder.setMoveMaker(Alliance.WHITE);
		} else if (fen[1][0] == 'b') {
			builder.setMoveMaker(Alliance.BLACK);
		} else {
			System.out.println("Turn is neither white nor black, somehow (invalid FEN)");
		}

		return builder.build();
	}

	public static class Builder {

		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		Move transitionMove;

		public Builder() {
			this.boardConfig = new HashMap<>(32, 1.0f);
		}

		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Builder setEnPassantPawn(final Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			return this;
		}

		public Builder setMoveTransition(final Move transitionMove) {
			this.transitionMove = transitionMove;
			return this;
		}

		public Board build() {
			return new Board(this);
		}

	}

	public Player currentPlayer() {
		return this.currentPlayer;
	}

	public Iterable<Move> getAllLegalMoves() {
		List<Move> allLegalMoves = new ArrayList<>();
		allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
		allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
		return Collections.unmodifiableList(allLegalMoves);
	}
}
