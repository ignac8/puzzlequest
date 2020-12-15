package it.zerko.puzzlequest.window;

import com.sun.jna.platform.WindowUtils;

import java.awt.*;

public class WindowProvider {

  public static final int GRID_SIZE = 8;
  public static final Point STARTING_POINT = new Point(272, 186);
  public static final Point ENDING_POINT = new Point(1016, 930);
  public static final double SIZE = (ENDING_POINT.getX() - STARTING_POINT.getX()) / 8;

  public Rectangle getWindowLocationAndSize(String windowTitle) {
    return WindowUtils.getAllWindows(false)
      .stream()
      .filter(desktopWindow -> desktopWindow.getTitle().equals(windowTitle))
      .findFirst()
      .get()
      .getLocAndSize();
  }
}
