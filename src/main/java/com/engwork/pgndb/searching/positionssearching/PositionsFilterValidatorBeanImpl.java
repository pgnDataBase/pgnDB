package com.engwork.pgndb.searching.positionssearching;

import com.engwork.pgndb.searching.exceptions.EmptyPositionsListException;
import com.engwork.pgndb.searching.exceptions.FilteringByStartFenNotSupportedException;
import com.engwork.pgndb.searching.exceptions.MissingPositionsListException;
import com.engwork.pgndb.searching.requestmodel.PositionsFilter;
import java.util.List;

class PositionsFilterValidatorBeanImpl implements PositionsFilterValidatorBean {

  @Override
  public void validate(PositionsFilter positionsFilter) {
    List<String> fens = positionsFilter.getPositions();
    if (fens == null) {
      throw new MissingPositionsListException();
    }
    if (fens.isEmpty()) {
      throw new EmptyPositionsListException(positionsFilter);
    }
    if (fensContainsStartingFen(positionsFilter.getPositions())) {
      throw new FilteringByStartFenNotSupportedException();
    }
  }

  private boolean fensContainsStartingFen(List<String> fens) {
    String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    for (String fen : fens) {
      if (fen.startsWith(START_FEN)) {
        return true;
      }
    }
    return false;
  }
}
