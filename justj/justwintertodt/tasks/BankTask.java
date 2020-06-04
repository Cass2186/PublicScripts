package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.justj.api.utils.Waiting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;
import scripts.justj.justwintertodt.gui.Vars;

import java.util.Arrays;

public class BankTask implements Task {

  @Override
  public Priority priority() {
    return Priority.MEDIUM;
  }

  @Override
  public boolean isValid() {
    return !State.getInstance().hasGameStarted()
        && Inventory.findList(rsItem -> rsItem.getDefinition().getActions() != null
          && Arrays.stream(rsItem.getDefinition().getActions()).anyMatch(action -> action.contains("Eat"))).isEmpty();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Banking");
    if (Banking.isBankScreenOpen()) {
      return Banking.deposit(0, "Supply crate")
          && Banking.withdraw(Vars.getInstance().getFoodAmount(), Vars.getInstance().getFoodName())
          && Timing.waitCondition(() -> !isValid(), General.random(521, 1235));
    }
    ABCUtil.performRunActivation();

    if (!isCloseToBank()) {
      DaxWalker.walkToBank(RunescapeBank.WINTERTODT);
      return Waiting.waitWithABC(this::isCloseToBank, General.random(1231, 3242));
    }

    WintertodtUtils.getBank().ifPresent(rsObject -> RSObjectUtils.click(rsObject, "Bank"));
    return Waiting.waitAfterWalking(Banking::isBankScreenOpen, General.random(231, 352));
  }

  private boolean isCloseToBank() {
    return Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_BANK_TILE) < General.random(5, 9);
  }
}
