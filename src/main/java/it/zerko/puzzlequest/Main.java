package it.zerko.puzzlequest;

import it.zerko.puzzlequest.executor.MoveExecutor;
import it.zerko.puzzlequest.grid.Grid;
import it.zerko.puzzlequest.grid.GridProvider;
import it.zerko.puzzlequest.image.ImageProvider;
import it.zerko.puzzlequest.move.MoveList;
import it.zerko.puzzlequest.move.MoveProvider;
import it.zerko.puzzlequest.window.WindowProvider;
import lombok.SneakyThrows;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements NativeKeyListener {

  private boolean comboMode = false;
  private ImageProvider imageProvider = new ImageProvider();
  private GridProvider gridProvider = new GridProvider();
  private MoveProvider moveProvider = new MoveProvider(comboMode);
  private MoveExecutor moveExecutor = new MoveExecutor();
  private WindowProvider windowProvider = new WindowProvider();

  public static void main(String[] args) {
    new Main().main();
  }

  @SneakyThrows
  private void main() {
    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.WARNING);
    logger.setUseParentHandlers(false);
    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(this);
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F3) {
      try {
        Rectangle windowLocationAndSize = windowProvider.getWindowLocationAndSize("Puzzle Quest");
        BufferedImage screenshot = imageProvider.getScreenshot(windowLocationAndSize);
        imageProvider.saveImage(screenshot, "screenshot.png");
        Grid grid = gridProvider.getGrid(screenshot);
        MoveList bestMoveList = moveProvider.getBestMoveList(grid);
        moveExecutor.executeMove(bestMoveList, windowLocationAndSize);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

  }
}




