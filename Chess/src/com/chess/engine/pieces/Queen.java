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

public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDS = { -9, -8, -7, -1, 1, 7, 8, 9, };

	public Queen(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.QUEEN, piecePosition, pieceAlliance);
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

					if (!candidateDestinationTile.isOccupied()) {
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
		return Piece.PieceType.QUEEN.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
	}

	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition]
				&& (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
	}

	@Override
	public Queen movePiece(Move move) {
		return new Queen(move.getMovedPiece().getAlliance(), move.getDestinationCoordinate());
	}

}
