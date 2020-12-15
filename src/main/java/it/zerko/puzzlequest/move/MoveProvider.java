package it.zerko.puzzlequest.move;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.window.WindowProvider;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MoveProvider {

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

  public MoveList getBestMoveList(Grid grid) {
    return getMoveLists(grid)
      .stream()
      .max(Comparator.naturalOrder())
      .get();
  }

  public Optional<MoveList> getNextTurnBestMoveList(Grid grid) {
    return getMoveLists(grid)
      .stream()
      .filter(MoveList::hasNextTurn)
      .max(Comparator.naturalOrder());
  }

  private List<MoveList> getMoveLists(Grid grid) {
    List<MoveList> moveLists = possibleMoves.stream()
      .map(possibleMove -> new MoveList(grid).addMove(possibleMove, true))
      .filter(MoveList::isValid)
      .collect(Collectors.toList());

    List<MoveList> moveListsToExpand = moveLists.stream()
      .filter(moveList -> !moveList.hasNextTurn())
      .collect(Collectors.toList());

    moveLists.removeAll(moveListsToExpand);

    List<MoveList> expandedMoveLists = moveListsToExpand.stream()
      .map(moveListToExpand -> possibleMoves.stream()
        .map(possibleMove -> moveListToExpand.copy().addMove(possibleMove, false))
        .min(Comparator.naturalOrder()))
      .map(Optional::get)
      .collect(Collectors.toList());

    moveLists.addAll(expandedMoveLists);

    Collections.shuffle(moveLists);
    return moveLists;
  }
}
