package it.zerko.puzzlequest;

import com.sun.jna.platform.WindowUtils;
import it.zerko.puzzlequest.gem.Gem;
import it.zerko.puzzlequest.gem.mana.BlueMana;
import it.zerko.puzzlequest.gem.mana.GreenMana;
import it.zerko.puzzlequest.gem.mana.RedMana;
import it.zerko.puzzlequest.gem.mana.Wildcard;
import it.zerko.puzzlequest.gem.mana.YellowMana;
import it.zerko.puzzlequest.gem.rest.Gold;
import it.zerko.puzzlequest.gem.rest.Star;
import it.zerko.puzzlequest.gem.skull.Skull;
import it.zerko.puzzlequest.gem.skull.Super;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) throws Exception {
    if (false) {
      BufferedImage screenShot = new Robot().createScreenCapture(
        WindowUtils.getAllWindows(false)
          .stream()
          .filter(desktopWindow -> desktopWindow.getTitle().equals("Puzzle Quest"))
          .findFirst()
          .get()
          .getLocAndSize());

      ImageIO.write(screenShot, "png", Paths.get("screenshot.png").toFile());
    }

    BufferedImage screenshot = ImageIO.read(Paths.get("screenshot.png").toFile());

    int gridSize = 8;

    Point startingPoint = new Point(272, 186);
    Point endingPoint = new Point(1016, 930);

    double size = (endingPoint.getX() - startingPoint.getX()) / 8;


    Gem[][] field = IntStream.range(0, gridSize)
      .mapToObj(i -> IntStream.range(0, gridSize)
        .mapToObj(j ->
          screenshot.getSubimage((int) (startingPoint.getX() + size * i), (int) (startingPoint.getY() + size * j),
            (int) size, (int) size))
        .map(ColorUtils::getAverageMiddleColor)
        .map(Main::getGem)
        .toArray(Gem[]::new))
      .toArray(Gem[][]::new);

    int debug = 1;
  }


  private static Gem getGem(Color color) {
    Wildcard wildcard = new Wildcard(color);
    return Stream.of(new BlueMana(color), new GreenMana(color), new RedMana(color), wildcard,
      new YellowMana(color), new Gold(color), new Star(color), new Skull(color), new Super(color))
      .sorted()
      .findFirst()
      .get();
  }

}
