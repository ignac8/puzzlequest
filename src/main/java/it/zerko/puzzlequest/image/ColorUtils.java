package it.zerko.puzzlequest.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColorUtils {

  private int scanSize = 10;

  public Color getAverageMiddleColor(BufferedImage image) {
    java.util.List<Color> colors = IntStream.rangeClosed(-scanSize, scanSize)
      .mapToObj(i -> IntStream.rangeClosed(-scanSize, scanSize)
        .mapToObj(j -> image.getRGB(image.getHeight() / 2 + i, image.getWidth() / 2 + j)))
      .flatMap(Function.identity())
      .map(Color::new)
      .collect(Collectors.toList());

    return new Color(getAverageColorChannel(colors, Color::getRed),
      getAverageColorChannel(colors, Color::getGreen),
      getAverageColorChannel(colors, Color::getBlue));
  }

  private int getAverageColorChannel(List<Color> colors, ToIntFunction<Color> getColor) {
    return (int) colors.stream()
      .mapToInt(getColor)
      .average()
      .getAsDouble();
  }

  public double getColorDifference(Color color1, Color color2) {
    return Math.pow(color1.getRed() - color2.getRed(), 2) +
      Math.pow(color1.getGreen() - color2.getGreen(), 2) +
      Math.pow(color1.getBlue() - color2.getBlue(), 2);
  }
}
