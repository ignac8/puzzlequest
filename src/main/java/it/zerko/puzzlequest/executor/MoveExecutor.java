package it.zerko.puzzlequest.executor;

import it.zerko.puzzlequest.move.Move;
import it.zerko.puzzlequest.move.MoveList;
import it.zerko.puzzlequest.window.WindowProvider;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class MoveExecutor {

  private Robot robot;

  @SneakyThrows
  public MoveExecutor() {
    robot = new Robot();
  }

  @SneakyThrows
  public void executeMove(MoveList moveList, Rectangle windowSizeAndLocation) {
    Move move = moveList.actualMoveList().get(0);
    Point from = move.firstPoint();
    Point to = move.secondPoint();

    int mouseButton = InputEvent.getMaskForButton(MouseEvent.BUTTON1);

    robot.mouseMove((int) (windowSizeAndLocation.getX() + WindowProvider.STARTING_POINT.getX() +
        WindowProvider.SIZE / 2 + WindowProvider.SIZE * from.getX()),
      (int) (windowSizeAndLocation.getY() + WindowProvider.STARTING_POINT.getY() +
        WindowProvider.SIZE / 2 + WindowProvider.SIZE * from.getY()));

    robot.mousePress(mouseButton);
    Thread.sleep(100);
    robot.mouseRelease(mouseButton);

    robot.mouseMove((int) (windowSizeAndLocation.getX() + WindowProvider.STARTING_POINT.getX() +
        WindowProvider.SIZE / 2 + WindowProvider.SIZE * to.getX()),
      (int) (windowSizeAndLocation.getY() + WindowProvider.STARTING_POINT.getY() +
        WindowProvider.SIZE / 2 + WindowProvider.SIZE * to.getY()));

    robot.mousePress(mouseButton);
    Thread.sleep(100);
    robot.mouseRelease(mouseButton);
  }

}
