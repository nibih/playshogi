package com.playshogi.library.shogi.rules.movements;

import java.util.ArrayList;
import java.util.List;

import com.playshogi.library.models.Square;
import com.playshogi.library.shogi.models.position.ShogiBoardState;

public class LanceMovement implements PieceMovement {

	@Override
	public List<Square> getPossibleMoves(final ShogiBoardState boardState, final Square from) {
		List<Square> result = new ArrayList<Square>();
		PieceMovementsUtils.addSquaresAlongDirection(boardState, from, 0, -1, result);
		return result;
	}

	@Override
	public boolean isMoveDxDyValid(final ShogiBoardState boardState, final Square from, final Square to) {
		if (to.getColumn() != from.getColumn() || to.getRow() >= from.getRow()) {
			return false;
		}

		for (int row = from.getRow() - 1; row > to.getRow(); row--) {
			if (boardState.getPieceAt(from.getColumn(), row) != null) {
				return false;
			}
		}

		return true;

	}

	@Override
	public boolean isDropValid(final ShogiBoardState boardState, final Square to) {
		return to.getRow() != ShogiBoardState.FIRST_ROW;
	}

}