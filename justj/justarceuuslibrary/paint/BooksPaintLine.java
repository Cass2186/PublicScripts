package scripts.justj.justarceuuslibrary.paint;

import org.tribot.api2007.Login;
import scripts.justj.justarceuuslibrary.Statistics;
import scripts.justj.api.paint.PaintLine;

import java.awt.Image;
import java.util.Optional;

public class BooksPaintLine implements PaintLine {

  private final long startTime;
  private long runTime;
  private int booksGained = 0;

  public BooksPaintLine() {
    this.startTime = System.currentTimeMillis();
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Books Gained: %s (%.2f/h)", booksGained, (booksGained * 3600000D / runTime));
  }

  @Override
  public void update() {
    runTime = System.currentTimeMillis() - startTime;
    if (Login.getLoginState() != Login.STATE.INGAME) {
      return;
    }
    booksGained = Statistics.get().getBooksGained();
  }
}
