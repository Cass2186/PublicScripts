package scripts.justj.api.listener;

import org.tribot.api2007.types.RSItem;

import java.util.List;

public interface InventoryListener {
  void onInventoryChanged(List<RSItem> inventory);
}
