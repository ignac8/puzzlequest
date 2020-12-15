package it.zerko.puzzlequest.move;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.window.WindowProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MoveProvider {

  private boolean comboMode;
  private List<Move> possibleMoves = Stream.concat(
    IntStream.range(0, WindowProvider.GRID_SIZE - 1)
      .mapToObj(i -> IntStream.range(0, WindowProvider.GRID_SIZE)
        .mapToObj(j -> Stream.of(new Move(new Point(i, j), new Point(i + 1, j))))),
    IntStream.range(0, WindowProvider.GRID_SIZE)
      .mapToObj(i -> IntStream.range(0, WindowProvider.GRID_SIZE - 1)
        .mapToObj(j -> Stream.of(new Move(new Point(i, j), new Point(i, j + 1))))))
    .flatMap(Function.identity())
    .flatMap(Function.identity())
    .collect(Collectors.toList());


  public MoveProvider(boolean comboMode) {
    this.comboMode = comboMode;
  }

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

    Collections.shuffle(moveLists);
    return moveLists.stream()
      .max(Comparator.naturalOrder())
      .get();
  }

}
