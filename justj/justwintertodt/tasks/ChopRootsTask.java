package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;
import scripts.justj.justwintertodt.Activity;

public class ChopRootsTask implements Task {

  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return !WintertodtUtils.isOutsideWintertodt()
        && State.getInstance().hasGameStarted()
        && State.getInstance().currentActivityIn(Activity.IDLE, Activity.WOODCUTTING, Activity.FEEDING_BRAZIER)
        && !Inventory.isFull() && Inventory.find("Bruma kindling").length == 0;
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Chopping roots");
    if (!Player.getPosition().equals(Constants.Tiles.WINTERTODT_CHOP_TILE)) {
      return Walking.walkTo(Constants.Tiles.WINTERTODT_CHOP_TILE)
          && Waiting.waitCondition(() -> Player.getPosition().equals(Constants.Tiles.WINTERTODT_CHOP_TILE), 4000);
    }

    if (Player.getAnimation() == Constants.Animations.IDLE) {
      return WintertodtUtils.getBurmaRoots()
          .map(rsObject -> RSObjectUtils.click(rsObject, "Chop"))
          .orElse(false)
          && Waiting.waitAfterWalking(() -> Player.getAnimation() != -1, General.random(642, 812));
    }

    return true;
  }
}
