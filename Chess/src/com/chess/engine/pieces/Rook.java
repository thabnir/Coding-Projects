package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

public class Rook extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDS = { -8, -1, 1, 8 };

	public Rook(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.ROOK, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordOffset : CANDIDATE_MOVE_VECTOR_COORDS) {
			int candidateDestinationCoord = this.piecePosition; // checking each of the 4 directions/offsets

			while (BoardUtils.isValidTileCoord(candidateDestinationCoord)) {
				candidateDestinationCoord += candidateCoordOffset; // sliding

				if (!BoardUtils.isValidTileCoord(candidateDestinationCoord))
					continue;

				if (isFirstColumnExclusion(candidateDestinationCoord, candidateCoordOffset)
						|| isEighthColumnExclusion(candidateDestinationCoord, candidateCoordOffset)) {
					break; // if it's on one of the edges, the rule doesn't apply, so break
				}

				if (BoardUtils.isValidTileCoord(candidateDestinationCoord)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoord); // limiting to only
																									// moves actually on
																									// the board

					if (!candidateDestinationTile.isOccupied()) { // this is wrong
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoord)); // non-capture move, keep
																								// sliding
					} else {
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getAlliance();

						if (this.pieceAlliance != pieceAlliance) {
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoord, pieceAtDestination)); // capture
																														// move,
																														// stop
																														// sliding
						}
						break;
					}
				}

			}

		}
		return Collections.unmodifiableList(legalMoves);
	}

	@Override
	public String toString() {
		return Piece.PieceType.ROOK.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}

	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}

	@Override
	public Rook movePiece(Move move) {
		return new Rook(move.getMovedPiece().getAlliance(), move.getDestinationCoordinate());
	}
}
