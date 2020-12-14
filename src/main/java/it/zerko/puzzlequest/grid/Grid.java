package it.zerko.puzzlequest.grid;

import it.zerko.puzzlequest.gem.Gem;
import lombok.Value;

import java.util.Arrays;

@Value
public class Grid {

  private Gem[][] actualGrid;

  public Grid copy() {
    return new Grid(Arrays.stream(actualGrid)
      .map(row -> Arrays.stream(row).toArray(Gem[]::new))
      .toArray(Gem[][]::new));
  }
}
