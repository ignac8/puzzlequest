package it.zerko.puzzlequest.grid;

import it.zerko.puzzlequest.gem.Anvil;
import it.zerko.puzzlequest.gem.BlueMana;
import it.zerko.puzzlequest.gem.Gem;
import it.zerko.puzzlequest.gem.Gold;
import it.zerko.puzzlequest.gem.GreenMana;
import it.zerko.puzzlequest.gem.RedMana;
import it.zerko.puzzlequest.gem.Scroll;
import it.zerko.puzzlequest.gem.Skull;
import it.zerko.puzzlequest.gem.Star;
import it.zerko.puzzlequest.gem.Super;
import it.zerko.puzzlequest.gem.Wildcard;
import it.zerko.puzzlequest.gem.YellowMana;
import it.zerko.puzzlequest.image.ColorUtils;
import it.zerko.puzzlequest.image.ImageProvider;
import it.zerko.puzzlequest.window.WindowProvider;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GridProvider {

  private ColorUtils colorUtils = new ColorUtils();
  private ImageProvider imageProvider = new ImageProvider();
  private Map<Class<? extends Gem>, Color> gemColors = Stream.of(BlueMana.class, GreenMana.class, RedMana.class,
    Wildcard.class, YellowMana.class, Gold.class, Star.class, Skull.class, Super.class, Anvil.class, Scroll.class)
    .collect(Collectors.toMap(cls -> cls, this::getColor));

  private Color getColor(Class<? extends Gem> cls) {
    String path = "graphics/%s.png".formatted(cls.getSimpleName().toLowerCase());
    BufferedImage image = imageProvider.loadImage(path);
    return colorUtils.getAverageMiddleColor(image);
  }

  public Grid getGrid(BufferedImage screenshot) {
    return new Grid(
      IntStream.range(0, WindowProvider.GRID_SIZE)
        .mapToObj(i -> IntStream.range(0, WindowProvider.GRID_SIZE)
          .mapToObj(j ->
            screenshot.getSubimage((int) (WindowProvider.STARTING_POINT.getX() + WindowProvider.SIZE * i),
              (int) (WindowProvider.STARTING_POINT.getY() + WindowProvider.SIZE * j),
              (int) WindowProvider.SIZE, (int) WindowProvider.SIZE))
          .map(colorUtils::getAverageMiddleColor)
          .map(this::getGem)
          .toArray(Gem[]::new))
        .toArray(Gem[][]::new));
  }

  @SneakyThrows
  private Gem getGem(Color color) {
    return gemColors.entrySet()
      .stream()
      .min(Comparator.comparingDouble(o -> colorUtils.getColorDifference(o.getValue(), color)))
      .get()
      .getKey()
      .getDeclaredConstructor()
      .newInstance();
  }
}
