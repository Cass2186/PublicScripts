package scripts.justj.api.listener;

import org.apache.commons.lang3.StringUtils;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import scripts.dax_api.walker.utils.TribotUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InventoryObserver extends Thread {
  private final List<InventoryListener> listeners;
  private final BooleanSupplier supplier;

  public InventoryObserver(BooleanSupplier supplier) {
    this.listeners = new ArrayList<>();
    this.supplier = supplier;
  }

  @Override
  public void run() {
    while (Login.getLoginState() != Login.STATE.INGAME) {
      General.sleep(500);
    }
    Map<String, Long> map = inventoryHashMap();
    while (true) {
      General.sleep(100);
      if (Login.getLoginState() != Login.STATE.INGAME) continue;
      if (!supplier.getAsBoolean()) {
        map = inventoryHashMap();
        continue;
      }
      Map<String, Long> updatedMap = inventoryHashMap();
      for (String i : updatedMap.keySet()) {
        Long countInitial = map.getOrDefault(i, 0L), countFinal = updatedMap.get(i);
        if (countFinal > countInitial) {
          addTrigger(i, countFinal - countInitial);
        } else if (countFinal < countInitial) {
          subtractedTrigger(i, countInitial - countFinal);
        }
        map.remove(i);
      }
      for (String i : map.keySet()) if (!updatedMap.containsKey(i)) subtractedTrigger(i, map.get(i));
      map = updatedMap;
    }
  }

  public Map<String, Long> inventoryHashMap() {
    return Inventory.getAllList().stream()
        .map(item -> StringUtils.defaultString(TribotUtil.getName(item)))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  public void addListener(InventoryListener inventoryListener) {
    listeners.add(inventoryListener);
  }

  public void addTrigger(String itemName, Long count) {
    for (InventoryListener l : listeners) l.inventoryItemGained(itemName, count);
  }

  public void subtractedTrigger(String itemName, Long count) {
    for (InventoryListener l : listeners) l.inventoryItemLost(itemName, count);
  }
}