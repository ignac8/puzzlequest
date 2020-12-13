package it.zerko.puzzlequest;

import it.zerko.puzzlequest.gem.Gem;

import java.awt.image.BufferedImage;

public class Main {

  private static final ImageProvider imageProvider = new ImageProvider();
  private static final GridProvider gridProvider = new GridProvider();

  public static void main(String[] args) throws Exception {
    //BufferedImage actualScreenshot = imageProvider.getScreenshot("Puzzle Quest");
    BufferedImage screenshot = imageProvider.loadImage("screenshot.png");
    Gem[][] grid = gridProvider.getGrid(screenshot);
    int debug = 1;
  }
}
