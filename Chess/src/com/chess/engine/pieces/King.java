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

public class King extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDS = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public King(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KING, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDS) {
			if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
					|| isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
			final int candidateDestinationCoord = this.piecePosition + currentCandidateOffset;
			if (BoardUtils.isValidTileCoord(candidateDestinationCoord)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoord);
				if (!candidateDestinationTile.isOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoord)); // non-capture move
				} else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getAlliance();
					if (this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new AttackMove(board, this, candidateDestinationCoord, pieceAtDestination)); // capture

					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}

	@Override
	public String toString() {
		return Piece.PieceType.KING.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
	}

	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition]
				&& (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
	}

	@Override
	public King movePiece(Move move) {
		return new King(move.getMovedPiece().getAlliance(), move.getDestinationCoordinate());
	}

}
