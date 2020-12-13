package it.zerko.puzzlequest;

import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.grid.GridProvider;
import it.zerko.puzzlequest.image.ImageProvider;

import java.awt.image.BufferedImage;

public class Main {

  private static final ImageProvider imageProvider = new ImageProvider();
  private static final GridProvider gridProvider = new GridProvider();

  public static void main(String[] args) throws Exception {
    //BufferedImage actualScreenshot = imageProvider.getScreenshot("Puzzle Quest");
    BufferedImage screenshot = imageProvider.loadImage("screenshot.png");
    Grid grid = gridProvider.getGrid(screenshot);
    int debug = 1;
  }
}
