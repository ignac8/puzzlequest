package it.zerko.puzzlequest.move;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.grid.GridProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MoveProvider {

  private boolean comboMode;

  public MoveProvider(boolean comboMode) {
    this.comboMode = comboMode;
  }

  private List<Move> possibleMoves = IntStream.range(0, GridProvider.GRID_SIZE - 1)
    .mapToObj(i -> IntStream.range(0, GridProvider.GRID_SIZE - 1)
      .mapToObj(j -> Stream.of(
        new Move(new Point(i, j), new Point(i + 1, j)),
        new Move(new Point(i, j), new Point(i, j + 1))))
      .flatMap(Function.identity()))
    .flatMap(moveStream -> moveStream)
    .collect(Collectors.toList());

  public MoveList getBestMoveList(Grid grid) {
    List<MoveList> moveLists = new ArrayList<>(List.of(new MoveList(grid)));

    do {
      List<MoveList> moveListsToExpand = moveLists.stream()
        .filter(MoveList::needsExpansion)
        .collect(Collectors.toList());
      moveLists.removeAll(moveListsToExpand);
      List<MoveList> expandedMoveLists = moveListsToExpand.stream()
        .flatMap(moveListToExpand -> possibleMoves.stream()
          .map(possibleMove -> moveListToExpand.copy().addMove(possibleMove)))
        .collect(Collectors.toList());
      moveLists.addAll(expandedMoveLists);
    } while (moveLists.stream().anyMatch(MoveList::needsExpansion) && comboMode);

    return moveLists.stream()
      .max(Comparator.naturalOrder())
      .get();
  }

}
