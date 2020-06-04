package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.InventoryUtils;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;
import scripts.justj.justwintertodt.Activity;

public class FletchTask implements Task {

  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return !WintertodtUtils.isOutsideWintertodt()
        && State.getInstance().hasGameStarted()
        && State.getInstance().currentActivityIn(Activity.IDLE, Activity.FLETCHING, Activity.WOODCUTTING)
        && Inventory.find(Constants.Items.BURMA_ROOT).length > 0;
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Why Fletch?");

    if (Player.getAnimation() == Constants.Animations.FLETCHING_BOW_CUTTING) {
      General.sleep(200);
      return true;
    }

    return GameTab.open(GameTab.TABS.INVENTORY)
        && Inventory.findList("Knife").stream()
          .filter(RSItem::isClickable)
          .findFirst()
          .flatMap(knife -> {
            InventoryUtils.clickItemNoUse(knife, "Use");
            return Inventory.findList(Constants.Items.BURMA_ROOT).stream()
                .filter(RSItem::isClickable)
                .findFirst()
                .map(RSItem::click);
          }).orElse(false)
      && Waiting.waitWithABC(() -> Player.getAnimation() != -1, General.random(1500, 2100));
  }
}
