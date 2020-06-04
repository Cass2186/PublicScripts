package scripts.justj.api.listener;

public interface InventoryListener {
  void inventoryItemGained(String itemName, Long count);
  void inventoryItemLost(String itemName, Long count);
}