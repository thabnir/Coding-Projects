package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;

public class Pawn extends Piece {

	private static final int[] CANDIDATE_MOVE_COORDS = { 8, 16, 7, 9 };

	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDS) {
			int candidateDestinationCoords = this.piecePosition
					+ (this.getAlliance().getDirection() * currentCandidateOffset);

			if (!BoardUtils.isValidTileCoord(candidateDestinationCoords)) {
				continue;
			}
			// TODO: make a working first column exclusion, bc it don't be working rn
			// if (isFirstColumnExclusion(this.piecePosition, candidateDestinationCoords)) {
			// continue;
			// }

			if (currentCandidateOffset == 8 && board.getTile(candidateDestinationCoords).isOccupied()) {
				// TODO make this a pawn move from pawn move class, deal with promotions
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoords)); // non-capture move
			} else if (currentCandidateOffset == 16 && this.isFirstMove()
					&& (BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getAlliance().isBlack())
					|| (BoardUtils.SECOND_RANK[this.piecePosition] && this.getAlliance().isWhite())) {
				final int behindCandidateDestinationCoord = this.piecePosition
						+ (this.pieceAlliance.getDirection() * 8);

				if (!board.getTile(behindCandidateDestinationCoord).isOccupied()
						&& !board.getTile(candidateDestinationCoords).isOccupied()) {
					// TODO make this a pawn move from pawn move class
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoords)); // 2-forward move
				}

			} else if (currentCandidateOffset == 7
					&& !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
							|| (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {

				if (board.getTile(candidateDestinationCoords).isOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoords).getPiece();
					if (this.pieceAlliance != pieceOnCandidate.getAlliance()) {
						// TODO more to do here
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoords)); //
					}
				}

			} else if (currentCandidateOffset == 9
					&& !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
							|| (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {

				if (board.getTile(candidateDestinationCoords).isOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoords).getPiece();
					if (this.pieceAlliance != pieceOnCandidate.getAlliance()) {
						// TODO more to do here
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoords)); //
					}
				}

			}
		}

		return Collections.unmodifiableList(legalMoves);
	}

	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
	}

	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getMovedPiece().getAlliance(), move.getDestinationCoordinate());
	}

}
