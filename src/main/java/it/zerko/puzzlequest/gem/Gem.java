package it.zerko.puzzlequest.gem;

import it.zerko.puzzlequest.ColorUtils;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public abstract class Gem implements Comparable<Gem> {

  private double colorDifference;

  @SneakyThrows
  public Gem(Color actualColor) {
    Path path = Paths.get("graphics/%s.png".formatted(getClass().getSimpleName().toLowerCase()));
    BufferedImage image = ImageIO.read(path.toFile());
    Color expectedColor = ColorUtils.getAverageMiddleColor(image);
    colorDifference = Math.pow(expectedColor.getRed() - actualColor.getRed(), 2) +
      Math.pow(expectedColor.getGreen() - actualColor.getGreen(), 2) +
      Math.pow(expectedColor.getBlue() - actualColor.getBlue(), 2);
  }

  @Override
  public int compareTo(Gem o) {
    return Double.compare(colorDifference, o.colorDifference);
  }
}
