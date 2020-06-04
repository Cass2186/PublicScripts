package scripts.justj.justarceuuslibrary.paint;

import scripts.justj.justarceuuslibrary.State;
import scripts.justj.api.paint.PaintLine;

import java.awt.Image;
import java.util.Optional;

public class LibraryStatePaintLine implements PaintLine {

  private String currentState;

  public LibraryStatePaintLine() {
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Current library state: %s", currentState);
  }

  @Override
  public void update() {
    currentState = State.get().getLibrary().getState().toString();
  }
}
