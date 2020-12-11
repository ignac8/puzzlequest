package it.zerko.puzzlequest;

import com.sun.jna.platform.WindowUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

  public static void main(String[] args) throws Exception {

    BufferedImage screenShot = new Robot().createScreenCapture(
      WindowUtils.getAllWindows(false)
        .stream()
        .filter(desktopWindow -> desktopWindow.getTitle().equals("Puzzle Quest"))
        .findFirst()
        .get()
        .getLocAndSize());

    //ImageIO.write(screenShot, "png", Paths.get("screenshot.png").toFile());


    int debug = 1;
  }

}
