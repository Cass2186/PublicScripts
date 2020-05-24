package scripts.justj.api.utils;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import scripts.justj.api.Constants;

import java.util.Optional;

public class BankingUtils {

  public static boolean isNoted() {
    return Game.getSetting(Constants.GameSettings.NOTED_SETTING) == 1;
  }

  public static boolean setNoted(boolean noted) {
    if (noted && isNoted() || !noted && !isNoted()) {
      return true;
    }

    return Optional.ofNullable(noted ? Constants.RSInterfaces.WITHDRAW_AS_NOTE.get() :
        Constants.RSInterfaces.WITHDRAW_AS_ITEM.get())
        .map(rsInterfaceChild -> rsInterfaceChild.click(noted ? "Note" : "Item")
          && Waiting.waitCondition(() -> noted == isNoted(), General.random(800, 1200)))
        .orElse(false);
  }

  public static boolean closeBank() {
    return Banking.close() && Waiting.waitCondition(() -> !Banking.isBankScreenOpen(), General.random(800, 1200));
  }

}
