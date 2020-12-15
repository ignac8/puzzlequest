package it.zerko.puzzlequest;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.grid.GridProvider;
import it.zerko.puzzlequest.image.ImageProvider;
import it.zerko.puzzlequest.move.MoveList;
import it.zerko.puzzlequest.move.MoveProvider;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;

public class Debug {

  private ImageProvider imageProvider = new ImageProvider();
  private GridProvider gridProvider = new GridProvider();
  private MoveProvider moveProvider = new MoveProvider();

  public static void main(String[] args) {
    new Debug().main();
  }

  @SneakyThrows
  private void main() {
    BufferedImage screenshot = imageProvider.loadImage("screenshot.png");
    Grid grid = gridProvider.getGrid(screenshot);
    MoveList bestMoveList = moveProvider.getBestMoveList(grid);
    int debug = 1;
  }

}
