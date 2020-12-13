package it.zerko.puzzlequest.move;

import it.zerko.puzzlequest.grid.Grid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MoveProvider {

  public MoveList getBestMoveList(Grid grid) {
    List<MoveList> moveLists = new ArrayList<>();
    moveLists.add(new MoveList());

    do {
      List<Move> moves = generateMoves(grid);

      if (false) {
        break;
      }
    } while (true);
    return null;
  }

  private List<Move> generateMoves(Grid grid) {
    return IntStream.range(0, grid.getGrid().length - 1)
      .mapToObj(i -> IntStream.range(0, grid.getGrid().length - 1)
        .mapToObj(j -> Stream.of(
          new Move(new Point(i, j), new Point(i + 1, j)),
          new Move(new Point(i, j), new Point(i, j + 1))))
        .flatMap(Function.identity()))
      .flatMap(moveStream -> moveStream)
      .collect(Collectors.toList());
  }


}
