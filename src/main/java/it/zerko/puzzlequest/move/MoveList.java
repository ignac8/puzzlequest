package it.zerko.puzzlequest.move;

import lombok.Getter;

import java.util.List;

@Getter
public class MoveList {
  public List<Move> moveList;
  boolean hasNextTurn;
}
