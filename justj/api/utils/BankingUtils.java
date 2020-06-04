package scripts.justj.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.tribot.api.General;
import org.tribot.api2007.*;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.api.Constants;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class BankingUtils {

  private static final String CHARGED_GLORY = "Amulet of glory(";
  private static final String UNCHARGED_GLORY = "Amulet of glory";

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

  public static boolean openBank() {
    return Banking.openBank() && Waiting.waitCondition(Banking::isBankLoaded, General.random(4000, 5000));
  }

  public static boolean withdrawAndEquip(Set<String> itemsToEquip) {

    Set<String> itemsNotWorn = getItemsNotWorn(itemsToEquip);

    boolean withdrawn = itemsNotWorn.stream()
        .filter(item -> Inventory.getCount(item) == 0)
        .allMatch(item -> Banking.withdraw(1, item));

    if (withdrawn
        && Waiting.waitCondition(() -> Inventory.getAllList().stream()
        .map(TribotUtil::getName)
        .collect(toSet()).containsAll(itemsNotWorn), General.random(2000, 3000))) {

      boolean worn = Inventory.findList(rsItem -> itemsNotWorn.contains(TribotUtil.getName(rsItem))).stream()
          .allMatch(rsItem -> AccurateMouse.click(rsItem, "Wear", "Equip"));

      if (worn && Waiting.waitCondition(() -> getItemsNotWorn(itemsToEquip).size() == 0, General.random(1000, 2000))) {
        ABCUtil.waitReactionTime(100);
        return true;
      }
    }

    return false;

  }

  public static boolean withdrawGloryIfRequired() {
    if (Optional.ofNullable(Equipment.getItem(Equipment.SLOTS.AMULET))
        .map(TribotUtil::getNameOrDefault)
        .filter(itemName -> itemName.contains(CHARGED_GLORY))
        .isPresent()) {
      //We have a glory already
      return true;
    }

    return Arrays.stream(Banking.find(rsItem -> TribotUtil.getNameOrDefault(rsItem).contains(CHARGED_GLORY)))
        .map(TribotUtil::getNameOrDefault)
        .filter(StringUtils::isNotBlank)
        .sorted()
        .findFirst()
        .map(Collections::singleton)
        .map(BankingUtils::withdrawAndEquip)
        .orElse(false)
        && Banking.deposit(0, UNCHARGED_GLORY);
  }

  private static Set<String> getItemsNotWorn(Set<String> items) {
    return items.stream()
        .filter(item -> !Equipment.isEquipped(item))
        .collect(toSet());
  }

}
