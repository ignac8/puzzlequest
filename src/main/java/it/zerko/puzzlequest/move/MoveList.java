package it.zerko.puzzlequest.move;

import it.zerko.puzzlequest.gem.BlueMana;
import it.zerko.puzzlequest.gem.Empty;
import it.zerko.puzzlequest.gem.Gem;
import it.zerko.puzzlequest.gem.Gold;
import it.zerko.puzzlequest.gem.GreenMana;
import it.zerko.puzzlequest.gem.RedMana;
import it.zerko.puzzlequest.gem.Skull;
import it.zerko.puzzlequest.gem.Star;
import it.zerko.puzzlequest.gem.Super;
import it.zerko.puzzlequest.gem.Unknown;
import it.zerko.puzzlequest.gem.Wildcard;
import it.zerko.puzzlequest.gem.YellowMana;
import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.window.WindowProvider;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@Setter
public class MoveList implements Comparable<MoveList> {
  private List<Move> actualMoveList = new ArrayList<>();
  private boolean hasNextTurn = true;
  private Grid grid;
  private int manaGained;
  private int restGained;
  private int damageDealt;

  public MoveList(Grid grid) {
    this.grid = grid.copy();
  }

  public MoveList copy() {
    return new MoveList(grid)
      .hasNextTurn(hasNextTurn)
      .actualMoveList(new ArrayList<>(actualMoveList))
      .manaGained(manaGained)
      .restGained(restGained)
      .damageDealt(damageDealt);
  }

  public MoveList addMove(Move move, boolean playerMove) {
    actualMoveList.add(move);
    swap(grid, move);

    boolean needsFurtherSimulation = true;
    hasNextTurn = false;

    while (needsFurtherSimulation) {
      needsFurtherSimulation = false;

      List<List<Gem>> lines = getLines(grid, 5);

      List<List<Gem>> fourLines = getLines(grid, 4);

      fourLines.removeIf(fourLine -> lines.stream().anyMatch(fiveLine -> fiveLine.containsAll(fourLine)));
      lines.addAll(fourLines);

      if (!lines.isEmpty() && playerMove) {
        hasNextTurn = true;
      }

      List<List<Gem>> threeLines = getLines(grid, 3);
      threeLines.removeIf(threeLine -> lines.stream().anyMatch(fiveLine -> fiveLine.containsAll(threeLine)));
      lines.addAll(threeLines);

      if (!lines.isEmpty()) {
        needsFurtherSimulation = true;
        addRest(lines, playerMove);
        addMana(lines, playerMove);
        addDamage(lines, playerMove);
        List<Point> supersLocations = getSupersLocations(lines);
        removeLines(lines);
        explode(supersLocations, playerMove);
        gravity();
      }
    }
    return this;
  }

  private void gravity() {
    for (int i = 0; i < grid.actualGrid().length; i++) {
      Gem[] row = grid.actualGrid()[i];
      for (int j = grid.actualGrid().length - 1; j >= 0; j--) {
        if (row[j] instanceof Empty) {
          System.arraycopy(row, 0, row, 1, j);
          row[0] = new Unknown();
        }
      }
    }
  }

  private void removeLines(List<List<Gem>> lines) {
    lines.stream()
      .flatMap(Collection::stream)
      .map(this::locateGem)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .forEach(this::removeGem);
  }

  private List<Point> getSupersLocations(List<List<Gem>> lines) {
    return lines.stream()
      .flatMap(Collection::stream)
      .filter(gem -> gem instanceof Super)
      .map(this::locateGem)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  private void explode(List<Point> supers, boolean playerMove) {
    supers.forEach(point -> explode(point, playerMove));
  }

  private void explode(Point point, boolean playerMove) {
    List<Gem> gemsToRemove = IntStream.rangeClosed(-1, 1)
      .mapToObj(i -> IntStream.rangeClosed(-1, 1)
        .mapToObj(j -> getGem(point, i, j))
        .filter(Optional::isPresent)
        .map(Optional::get))
      .flatMap(Function.identity())
      .collect(Collectors.toList());

    List<Point> nextExplosionPoints = gemsToRemove.stream()
      .filter(gemToRemove -> gemToRemove instanceof Super)
      .map(this::locateGem)
      .map(Optional::get)
      .collect(Collectors.toList());

    gemsToRemove.forEach(gem -> valueSingleGem(gem, playerMove));
    gemsToRemove.stream().map(this::locateGem).map(Optional::get).forEach(this::removeGem);
    nextExplosionPoints.forEach(nextPoint -> explode(nextPoint, playerMove));
  }

  private void valueSingleGem(Gem gem, boolean playerMove) {
    if (gem instanceof RedMana || gem instanceof BlueMana || gem instanceof GreenMana || gem instanceof YellowMana) {
      if (playerMove) {
        manaGained += 1;
      }
    } else if (gem instanceof Skull) {
      if (playerMove) {
        damageDealt += 1;
      } else {
        damageDealt -= 1;
      }
    } else if (gem instanceof Super) {
      if (playerMove) {
        damageDealt += 5;
      } else {
        damageDealt -= 5;
      }
    } else if (gem instanceof Gold || gem instanceof Star) {
      if (playerMove) {
        restGained += 1;
      }
    }
  }

  private Optional<Gem> getGem(Point point, int xOffset, int yOffset) {
    try {
      return Optional.of(grid.actualGrid()[point.x + xOffset][point.y + yOffset]);
    } catch (IndexOutOfBoundsException ignored) {
      return Optional.empty();
    }
  }

  private Optional<Point> locateGem(Gem gem) {
    return IntStream.range(0, WindowProvider.GRID_SIZE)
      .mapToObj(i -> IntStream.range(0, WindowProvider.GRID_SIZE)
        .mapToObj(j -> new Point(i, j)))
      .flatMap(Function.identity())
      .filter(point -> gem.equals(grid.actualGrid()[point.x][point.y]))
      .findFirst();
  }

  private void removeGem(Point point) {
    grid.actualGrid()[point.x][point.y] = new Empty();
  }

  private void addMana(List<List<Gem>> lines, boolean playerMove) {
    lines.stream()
      .filter(this::isMana)
      .forEach(line -> addLineMana(line, playerMove));
  }

  private void addLineMana(List<Gem> line, boolean playerMove) {
    int multiplier = line.stream()
      .filter(gem -> gem instanceof Wildcard)
      .map(gem -> (Wildcard) gem)
      .mapToInt(Wildcard::multiplier)
      .reduce(1, (a, b) -> a * b);
    if (playerMove) {
      manaGained += line.size() * multiplier;
    }
  }

  private void addRest(List<List<Gem>> lines, boolean playerMove) {
    int sum = lines.stream()
      .filter(this::isRest)
      .mapToInt(List::size)
      .sum();
    if (playerMove) {
      restGained += sum;
    }
  }

  private void addDamage(List<List<Gem>> lines, boolean playerMove) {
    int sum = lines.stream()
      .filter(this::isDamage)
      .flatMap(Collection::stream)
      .mapToInt(gem -> gem instanceof Super ? 5 : 1)
      .sum();
    if (playerMove) {
      damageDealt += sum;
    } else {
      damageDealt -= sum;
    }
  }

  private List<List<Gem>> getLines(Grid grid, int lineSize) {
    return Stream.concat(
      IntStream.rangeClosed(0, WindowProvider.GRID_SIZE - lineSize)
        .mapToObj(i -> IntStream.range(0, WindowProvider.GRID_SIZE)
          .mapToObj(j -> IntStream.range(0, lineSize)
            .mapToObj(k -> grid.actualGrid()[i + k][j])
            .collect(Collectors.toList()))),
      IntStream.range(0, WindowProvider.GRID_SIZE)
        .mapToObj(i -> IntStream.rangeClosed(0, WindowProvider.GRID_SIZE - lineSize)
          .mapToObj(j -> IntStream.range(0, lineSize)
            .mapToObj(k -> grid.actualGrid()[i][j + k])
            .collect(Collectors.toList()))))
      .flatMap(Function.identity())
      .filter(line -> isMana(line) || isDamage(line) || isRest(line))
      .collect(Collectors.toList());
  }

  private boolean isMana(List<Gem> line) {
    return lineContainsOnly(line, List.of(GreenMana.class, Wildcard.class)) ||
      lineContainsOnly(line, List.of(RedMana.class, Wildcard.class)) ||
      lineContainsOnly(line, List.of(BlueMana.class, Wildcard.class)) ||
      lineContainsOnly(line, List.of(YellowMana.class, Wildcard.class));
  }

  private boolean isDamage(List<Gem> line) {
    return lineContainsOnly(line, List.of(Skull.class, Super.class));
  }

  private boolean isRest(List<Gem> line) {
    return lineContainsOnly(line, List.of(Gold.class)) ||
      lineContainsOnly(line, List.of(Star.class));
  }

  private boolean lineContainsOnly(List<Gem> line, List<Class<? extends Gem>> classes) {
    return line.stream()
      .map(Gem::getClass)
      .allMatch(classes::contains);
  }

  private void swap(Grid grid, Move move) {
    Gem[][] actualGrid = grid.actualGrid();

    Gem firstGem = actualGrid[move.firstPoint.x][move.firstPoint.y];
    Gem secondGem = actualGrid[move.secondPoint.x][move.secondPoint.y];

    actualGrid[move.firstPoint.x][move.firstPoint.y] = secondGem;
    actualGrid[move.secondPoint.x][move.secondPoint.y] = firstGem;
  }

  public boolean isValid() {
    return damageDealt > 0 || manaGained > 0 || restGained > 0;
  }

  @Override
  public int compareTo(MoveList o) {
    return Comparator.comparing((Function<MoveList, Boolean>) MoveList::hasNextTurn)
      .thenComparingInt(MoveList::damageDealt)
      .thenComparingInt(MoveList::manaGained)
      .thenComparingInt(MoveList::restGained)
      .compare(this, o);
  }

}
