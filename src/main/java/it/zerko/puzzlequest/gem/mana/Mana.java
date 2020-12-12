package it.zerko.puzzlequest.gem.mana;

import it.zerko.puzzlequest.gem.Gem;
import lombok.Getter;

import java.awt.*;

@Getter
public abstract class Mana extends Gem {

  public Mana(Color actualColor) {
    super(actualColor);
  }
}
