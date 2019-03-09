package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.chessgamesconverter.model.Move;

import java.util.List;

interface MovesToTreeLoadingBean {
  void load(List<Move> moves, Boolean includeVariants);
}
