package it.zerko.puzzlequest.image;

import com.sun.jna.platform.WindowUtils;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;

public class ImageProvider {

  private Robot robot;

  @SneakyThrows
  public ImageProvider() {
    robot = new Robot();
  }

  @SneakyThrows
  public BufferedImage loadImage(String path) {
    return ImageIO.read(Paths.get(path).toFile());
  }

  public BufferedImage getScreenshot(String windowTitle) {
    return robot.createScreenCapture(
      WindowUtils.getAllWindows(false)
        .stream()
        .filter(desktopWindow -> desktopWindow.getTitle().equals(windowTitle))
        .findFirst()
        .get()
        .getLocAndSize());
  }


}
