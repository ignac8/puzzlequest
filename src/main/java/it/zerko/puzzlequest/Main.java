package it.zerko.puzzlequest;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.grid.GridProvider;
import it.zerko.puzzlequest.image.ImageProvider;
import it.zerko.puzzlequest.move.MoveList;
import it.zerko.puzzlequest.move.MoveProvider;

import java.awt.image.BufferedImage;

public class Main {

  private boolean comboMode = false;
  private ImageProvider imageProvider = new ImageProvider();
  private GridProvider gridProvider = new GridProvider();
  private MoveProvider moveProvider = new MoveProvider(comboMode);


  public static void main(String[] args) {
    new Main().main();
  }

  private void main()
  {
    //BufferedImage actualScreenshot = imageProvider.getScreenshot("Puzzle Quest");
    BufferedImage screenshot = imageProvider.loadImage("screenshot.png");
    Grid grid = gridProvider.getGrid(screenshot);
    MoveList bestMoveList = moveProvider.getBestMoveList(grid);
    int debug = 1;
  }

}
