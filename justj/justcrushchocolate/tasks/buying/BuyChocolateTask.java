package scripts.justj.justcrushchocolate.tasks.buying;

import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.script.Script;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.api.JustLogger;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.GEUtils;
import scripts.justj.justcrushchocolate.Constants;
import scripts.justj.justcrushchocolate.State;
import scripts.justj.justcrushchocolate.Vars;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BuyChocolateTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(BuyChocolateTask.class);

  private final Script script;

  public BuyChocolateTask(Script script) {
    this.script = script;
  }

  @Override
  public Priority priority() {
    return Priority.NONE;
  }

  @Override
  public boolean isValid() {
    return Inventory.getCount(Constants.Items.COINS) > 0 || hasBarsInExchange();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Buying Chocolate");
    if (!GEUtils.openGE()) {
      LOGGER.error("Could not open GE");
      return false;
    }

    if (hasBarsInExchange()) {
      State.getInstance().setStatus("Waiting for chocolate to buy");
      int boughtFor = GEUtils.collectOrWait(Constants.Items.CHOCOLATE_BAR, GrandExchange.COLLECT_METHOD.BANK, script,
          Vars.getInstance().getGeWaitTime()*60*1000);
      if (boughtFor > 0) {

        LOGGER.info("Go back to crushing");
        State.getInstance().setBuying(false);
        return GrandExchange.close();
      }
      return false;
    }

    return buyBars();
  }

  private boolean hasBarsInExchange() {
    return Arrays.stream(GrandExchange.getOffers())
        .filter(rsgeOffer -> Constants.Items.CHOCOLATE_BAR.equals(TribotUtil.getName(rsgeOffer))).count() > 0;
  }

  private boolean buyBars() {
    int price = (int) (scripts.wastedbro.api.rsitem_services.GrandExchange.getPrice(Constants.Items.CHOCOLATE_BAR_ID) *  (Vars.getInstance().getBuyModifier() / 100.0));

    int coinCount = Inventory.getCount(Constants.Items.COINS);

    int numberToBuy = coinCount / price;

    return GrandExchange.offer(Constants.Items.CHOCOLATE_BAR, price, numberToBuy, false)
        && GrandExchange.confirmOffer();

  }

}
