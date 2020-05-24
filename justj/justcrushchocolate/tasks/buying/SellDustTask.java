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

public class SellDustTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(SellDustTask.class);

  private final Script script;

  public SellDustTask(Script script) {
    this.script = script;
  }

  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return Inventory.getCount(Constants.Items.CHOCOLATE_DUST) > 0 || hasDustInExchange();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Selling Chocolate Dust");
    if (!GEUtils.openGE()) {
      LOGGER.error("Could not open GE");
      return false;
    }

    List<RSGEOffer> dustOffers = Arrays.stream(GrandExchange.getOffers())
        .filter(rsgeOffer -> Constants.Items.CHOCOLATE_DUST.equals(TribotUtil.getName(rsgeOffer)))
        .collect(toList());

    if (dustOffers.size() > 0) {
      State.getInstance().setStatus("Waiting for Chocolate dust to sell");
      return GEUtils.collectOrWait(Constants.Items.CHOCOLATE_DUST, GrandExchange.COLLECT_METHOD.ITEMS, script,
          Vars.getInstance().getGeWaitTime()*60*1000) > 0;
    }

    return sellDust();
  }

  private boolean hasDustInExchange() {
    return Arrays.stream(GrandExchange.getOffers())
        .filter(rsgeOffer -> Constants.Items.CHOCOLATE_DUST.equals(TribotUtil.getName(rsgeOffer))).count() > 0;
  }

  private boolean sellDust() {
    int price = (int) (scripts.wastedbro.api.rsitem_services.GrandExchange.getPrice(Constants.Items.CHOCOLATE_DUST_ID)
        *  (Vars.getInstance().getSellModifier() / 100.0));

    return GrandExchange.offer(Constants.Items.CHOCOLATE_DUST, price, -1, true)
        && GrandExchange.confirmOffer();
  }

}
