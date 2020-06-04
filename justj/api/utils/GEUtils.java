package scripts.justj.api.utils;

import org.tribot.api.General;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Login;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSInterface;
import org.tribot.script.Script;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.api.Constants;
import scripts.justj.api.JustLogger;
import scripts.justj.justcrushchocolate.tasks.buying.SellDustTask;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.toList;

public class GEUtils {

  private static final JustLogger LOGGER = new JustLogger(GEUtils.class);

  public static boolean openGE() {

    return GrandExchange.getWindowState() != null || Arrays.stream(Objects.findNearest(5, rsObject ->
        TribotUtil.getName(rsObject).equals(Constants.Objects.GRAND_EXCHANGE_BOOTH)
            && TribotUtil.getActions(rsObject).contains("Exchange")))
        .findFirst()
        .map(rsObject -> MouseUtils.click(rsObject, "Exchange")
            && Waiting.waitCondition(() -> GrandExchange.getWindowState() != null, General.random(3000, 4000)))
        .orElse(false);
  }

  public static boolean collectAll(GrandExchange.COLLECT_METHOD collectMethod) {

    String collectString = collectMethod == GrandExchange.COLLECT_METHOD.ITEMS ?
        "Collect to inventory" : "Collect to bank";

    return Optional.ofNullable(Constants.RSInterfaces.COLLECT_ALL.get())
        .map(rsInterface -> rsInterface.click(collectString)
        && Waiting.waitCondition(() -> {
          RSInterface collectInterface = Constants.RSInterfaces.COLLECT_ALL.get();
          return collectInterface == null || !collectInterface.isClickable() || collectInterface.isHidden();
        } , General.random(4000, 6000)))
        .orElse(false);
  }

  public static int collectOrWait(String itemName, GrandExchange.COLLECT_METHOD collectMethod, Script script,
                                  long averageWait) {

    List<RSGEOffer> offers = Arrays.stream(GrandExchange.getOffers())
        .filter(rsgeOffer -> itemName.equals(TribotUtil.getName(rsgeOffer)))
        .collect(toList());


    if (offers.size() > 0) {
      if (offers.stream().map(RSGEOffer::getStatus).allMatch(RSGEOffer.STATUS.COMPLETED::equals)) {
        LOGGER.info("All sell offers complete, collecting");

        if (GEUtils.collectAll(collectMethod)) {
          return offers.stream().map(offer -> offer.getPrice() * offer.getQuantity()).reduce(0, Integer::sum);
        } else {
          return -1;
        }
      }

      //Wait for 30 seconds, if they don't sell, go to sleep for a while

      if (!Waiting.waitCondition(() -> Arrays.stream(GrandExchange.getOffers())
          .filter(rsgeOffer -> itemName.equals(TribotUtil.getName(rsgeOffer)))
          .allMatch(rsgeOffer -> rsgeOffer.getStatus() == RSGEOffer.STATUS.COMPLETED), General.random(25000, 35000), 1000)) {

        long timeSleep = averageWait + General.random(-120000, 120000);

        LOGGER.info(String.format("Sleeping for %sms", timeSleep));

        script.setLoginBotState(false);
        Login.logout();
        General.sleep(timeSleep);
        script.setLoginBotState(true);

      }

    }
    return -1;
  }

}
