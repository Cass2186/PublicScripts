package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class InventoryObserver extends Thread {

  private final Set<InventoryListener> listeners;
  private final Supplier<Boolean> shouldNotify;

  private List<RSItem> lastInventory;

  public InventoryObserver(Supplier<Boolean> shouldNotify) {
    this.listeners = new HashSet<>();
    this.shouldNotify = shouldNotify;
    this.lastInventory = new ArrayList<>();
  }

  @Override
  public void run() {
    while (Login.getLoginState() != Login.STATE.INGAME) {
      General.sleep(500);
    }

    while (true) {
      General.sleep(100);
      if (Login.getLoginState() != Login.STATE.INGAME) continue;

      List<RSItem> currentInventory = Inventory.getAllList();
      if (shouldNotify.get() && !lastInventory.equals(currentInventory)) {
        listeners.forEach(e -> e.onInventoryChanged(currentInventory));
      }
      lastInventory = currentInventory;
    }
  }

  public void addListener(InventoryListener inventoryListener) {
    listeners.add(inventoryListener);
  }
}