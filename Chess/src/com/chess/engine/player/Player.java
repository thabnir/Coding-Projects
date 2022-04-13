package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

public abstract class Player {

	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	protected final boolean isInCheck;

	Player(final Board board, final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
		this.board = board;
		this.playerKing = establishKing();
		this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
		
		Collection<Move> legalCopy = new ArrayList<>();
		legalCopy.addAll(playerLegals);
		legalCopy.addAll(calculateKingCastles(playerLegals, opponentLegals));
		this.legalMoves = Collections.unmodifiableCollection(legalCopy);
	}

	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for (final Move move : moves) {
			if (piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return Collections.unmodifiableList(attackMoves);
	}

	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("No kings (invalid board), shouldn't reach here");
	}

	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	public boolean isInCheck() {
		return this.isInCheck;
	}

	public boolean isInCheckmate() {
		return this.isInCheck && !hasEscapeMoves(); // checked + no escape
	}

	// TODO: Implement these methods

	public boolean isInStalemante() {
		return !this.isInCheck && !hasEscapeMoves(); // not checked + no moves
	}

	protected boolean hasEscapeMoves() {
		for (final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public boolean isCastled() {
		return false;
	}

	public MoveTransition makeMove(final Move move) {
		if (!this.legalMoves.contains(move)) {
			return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		final Board transitionedBoard = move.execute();
		return transitionedBoard.currentPlayer().getOpponent().isInCheck() ?
				new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK) :
					new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
	}

	protected boolean hasCastleOpportunities() {
		// TODO: the functions this relies on (isInCheck(), isCastled(), etc.) are stubs
		return !this.isInCheck && !this.playerKing.isCastled() &&
				(this.playerKing.isKingSideCastleCapable() || this.playerKing.isQueenSideCastleCapable());
	}

	public King getPlayerKing() {
		return this.playerKing;
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	public abstract Collection<Piece> getActivePieces();

	public abstract Alliance getAlliance();

	public abstract Player getOpponent();

	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
			Collection<Move> opponentsLegals);

}
