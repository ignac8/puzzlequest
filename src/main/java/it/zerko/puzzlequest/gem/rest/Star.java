package it.zerko.puzzlequest.gem.rest;

import it.zerko.puzzlequest.gem.Gem;
import lombok.Getter;

import java.awt.*;

@Getter
public class Star extends Gem {

  public Star(Color actualColor) {
    super(actualColor);
  }
}
