package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.utils.InventoryUtils;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;

import java.util.Arrays;
import java.util.Optional;

public class EatTask implements Task {

  private static final int MIN_EAT_THRESHOLD = 7;

  @Override
  public Priority priority() {
    return Priority.HIGH;
  }

  @Override
  public boolean isValid() {
    return State.getInstance().hasGameStarted()
      && (ABCUtil.shouldEat() || Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS) <= MIN_EAT_THRESHOLD);
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Eating");

    Optional<RSItem> optionalCake = Inventory
        .findList(rsItem -> rsItem.getDefinition().getActions() != null
          && Arrays.stream(rsItem.getDefinition().getActions()).anyMatch(action -> action.contains("Eat"))).stream()
        .filter(RSItem::isClickable)
        .findFirst();

    if (GameTab.open(GameTab.TABS.INVENTORY) && optionalCake.isPresent()
        && InventoryUtils.clickItemNoUse(optionalCake.get(), "Eat")
        && Waiting.waitWithABC(() -> !isValid(), General.random(700, 1400))) {
      ABCUtil.generateEatAtPercentage();
      return true;
    } else {
      State.getInstance().setStatus("Out of food");
      return Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE) <= 3 ||
          (Walking.blindWalkTo(Constants.Tiles.WINTERTODT_IDLE_TILE)
              && Timing.waitCondition(() -> !State.getInstance().hasGameStarted(), 1000));
    }

  }

}
