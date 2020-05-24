package scripts.justj.api.paint;

import org.tribot.api.Timing;

import java.awt.Image;
import java.util.Optional;

public class TimeRanPaintLine implements PaintLine {

  private final long startTime;
  private String runTime;

  public TimeRanPaintLine() {
    this.startTime = System.currentTimeMillis();
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Time Ran: %s", runTime);
  }

  @Override
  public void update() {
    runTime = Timing.msToString(System.currentTimeMillis() - startTime);
  }
}
