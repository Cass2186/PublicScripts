package scripts.justj.justcrushchocolate.paint;

import org.tribot.api2007.Login;
import scripts.justj.api.paint.PaintLine;
import scripts.justj.justcrushchocolate.Statistics;

import java.awt.Image;
import java.util.Optional;

public class ChocolatePaintLine implements PaintLine {

  private final long startTime;
  private long runTime;
  private int chocolateCrushed = 0;

  public ChocolatePaintLine() {
    this.startTime = System.currentTimeMillis();
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Chocolate Crushed: %s (%.2f/h)", chocolateCrushed, (chocolateCrushed * 3600000D / runTime));
  }

  @Override
  public void update() {
    runTime = System.currentTimeMillis() - startTime;
    if (Login.getLoginState() != Login.STATE.INGAME) {
      return;
    }
    chocolateCrushed = Statistics.getInstance().getChocolateCrushed();
  }
}
