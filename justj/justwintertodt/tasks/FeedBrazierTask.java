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

public class FeedBrazierTask implements Task {

  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return !WintertodtUtils.isOutsideWintertodt()
        && State.getInstance().hasGameStarted()
        && State.getInstance().currentActivityIn(Activity.IDLE, Activity.FEEDING_BRAZIER, Activity.FLETCHING)
        && Inventory.find("Bruma root").length == 0 && Inventory.find("Bruma kindling").length > 0;
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Feeding");

    if (State.getInstance().wasLastTask(this)) {
      return true;
    }

    return WintertodtUtils.getLitBrazier()
        .map(rsObject -> RSObjectUtils.click(rsObject, "Feed")
            && Waiting.waitAfterWalking(() -> Player.getAnimation() != -1, General.random(1500, 2100)))
        .orElseGet(this::walkToBrazierIfRequired);
  }

  public boolean walkToBrazierIfRequired() {
    if (Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE) > 3) {
      return Walking.walkTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE)
          && Waiting.waitCondition(() -> Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE) <= 1, 2000);
    }
    return true;
  }
}
