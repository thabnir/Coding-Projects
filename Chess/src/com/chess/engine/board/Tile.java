package com.chess.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;

public abstract class Tile {

	protected final int tileCoordinate; // 0 - 63

	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

	private Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	// return 1 < 2 ? all is right : the end of days

	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}

		return Collections.unmodifiableMap(emptyTileMap);
	}

	public abstract boolean isOccupied();

	public abstract Piece getPiece();

	public static final class EmptyTile extends Tile {

		private EmptyTile(final int coordinate) {
			super(coordinate);
		}

		@Override
		public String toString() {
			return "-";
		}

		@Override
		public boolean isOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}

	}

	public static final class OccupiedTile extends Tile {

		private final Piece pieceOnTile;

		private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public String toString() {
			return getPiece().getAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
		}

		@Override
		public boolean isOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return pieceOnTile;
		}
	}

	public int getTileCoordinate() {
		return this.tileCoordinate;
	}

}
