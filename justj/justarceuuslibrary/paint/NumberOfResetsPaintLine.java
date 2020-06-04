package scripts.justj.justarceuuslibrary.paint;

import org.tribot.api2007.Login;
import scripts.justj.justarceuuslibrary.Statistics;
import scripts.justj.api.paint.PaintLine;

import java.awt.Image;
import java.util.Optional;

public class NumberOfResetsPaintLine implements PaintLine {

  private final long startTime;
  private long runTime;
  private int numberOfResets = 0;

  public NumberOfResetsPaintLine() {
    this.startTime = System.currentTimeMillis();
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Resets: %s (%.2f/h)", numberOfResets, (numberOfResets * 3600000D / runTime));
  }

  @Override
  public void update() {
    runTime = System.currentTimeMillis() - startTime;
    if (Login.getLoginState() != Login.STATE.INGAME) {
      return;
    }
    numberOfResets = Statistics.get().getResets();
  }
}
