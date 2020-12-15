package it.zerko.puzzlequest.image;

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

  @SneakyThrows
  public void saveImage(BufferedImage image, String path) {
    ImageIO.write(image, "png", Paths.get(path).toFile());
  }

  public BufferedImage getScreenshot(Rectangle windowLocationAndSize) {
    return robot.createScreenCapture(windowLocationAndSize);
  }

}
