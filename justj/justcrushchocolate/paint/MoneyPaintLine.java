package scripts.justj.justcrushchocolate.paint;

import org.tribot.api2007.Login;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.paint.PaintLine;
import scripts.justj.justcrushchocolate.Constants;
import scripts.justj.justcrushchocolate.Statistics;
import scripts.wastedbro.api.rsitem_services.GrandExchange;

import java.awt.Image;
import java.util.Optional;

/**
 * TODO
 * - Make this track buy/sell price in GE
 */
public class MoneyPaintLine implements PaintLine {

  private final long startTime;
  private final int chocolateProfit;

  private long runTime;
  private int chocolateCrushed = 0;

  public MoneyPaintLine() {
    this.startTime = System.currentTimeMillis();

    int cost =  GrandExchange.getPrice(Constants.Items.CHOCOLATE_DUST_ID)
        - GrandExchange.getPrice(Constants.Items.CHOCOLATE_BAR_ID);
    if (cost == 0) {
      //Default the profit
      this.chocolateProfit = 30;
    } else {
      this.chocolateProfit = cost;
    }

    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Money gained: %s (%s/h)", Paint.numberFormat(chocolateCrushed* chocolateProfit),
        Paint.numberFormat(((chocolateCrushed* chocolateProfit) * 3600000D / runTime)));
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
