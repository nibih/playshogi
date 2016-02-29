package com.playshogi.website.gwt.client.board;

import static com.playshogi.website.gwt.client.board.ShogiBoard.SQUARE_WIDTH;

import com.google.gwt.core.client.GWT;
import com.playshogi.library.shogi.models.Piece;

public class Komadai {

	private static final int KOMADAI_INSIDE_MARGIN = 5;
	private final int[] pieces = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * Offset, in squares, to display the pieces of the given type.
	 */
	private static final int[] sdispx = new int[] { 0, 2, 0, 2, 0, 2, 0, 0 };
	private static final int[] gdispx = new int[] { 0, 0, 2, 0, 2, 0, 2, 0 };
	private static final int[] sdispy = new int[] { 3, 2, 2, 1, 1, 0, 0, 4 };
	private static final int[] gdispy = new int[] { 0, 1, 1, 2, 2, 3, 3, 4 };
	/**
	 * Number of pieces that can be displayed without overlapping.
	 */
	private static final int[] ndispx = new int[] { 4, 2, 2, 2, 2, 2, 2, 2 };
	private final boolean sente;

	public Komadai(final boolean sente) {
		this.sente = sente;
	}

	public Point addPiece(final Piece piece) {

		GWT.log("Adding piece: " + piece.toString());

		int pieceNum = piece.getPieceType().ordinal();

		int ox;
		if (pieces[pieceNum] > ndispx[pieceNum]) {
			ox = (ShogiBoard.SQUARE_WIDTH * ndispx[pieceNum]) / pieces[pieceNum];
		} else {
			ox = SQUARE_WIDTH;
		}

		int x;
		int y;

		if (sente) {
			x = (gdispx[pieceNum] * SQUARE_WIDTH) + (pieces[pieceNum] * ox);
			y = (gdispy[pieceNum] * ShogiBoard.SQUARE_HEIGHT);
		} else {
			x = (sdispx[pieceNum] * SQUARE_WIDTH) + (pieces[pieceNum] * ox);
			y = (sdispy[pieceNum] * ShogiBoard.SQUARE_HEIGHT);
		}

		pieces[pieceNum]++;

		return new Point(x, y);
	}

	public static class Point {
		public int x;
		public int y;

		public Point(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}

}