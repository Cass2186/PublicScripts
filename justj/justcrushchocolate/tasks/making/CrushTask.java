package scripts.justj.justcrushchocolate.tasks.making;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.justj.api.JustLogger;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.utils.InventoryUtils;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justcrushchocolate.Constants;
import scripts.justj.justcrushchocolate.State;
import scripts.justj.justcrushchocolate.Vars;

public class CrushTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(CrushTask.class);

  private static final int CRUSH_TIME = 1800;

  @Override
  public Priority priority() {
    return Priority.NONE;
  }

  @Override
  public boolean isValid() {
    return Inventory.getCount(Constants.Items.CHOCOLATE_BAR) > 0
        && Inventory.getCount(Vars.getInstance().getCrushItemName()) > 0
        && !Banking.isBankScreenOpen();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Crushing");
    int timeToCrush = Inventory.getCount(Constants.Items.CHOCOLATE_BAR) * CRUSH_TIME;
    LOGGER.debug("Time to crush: " + timeToCrush);
    ABCUtil.generateTrackers(timeToCrush, true);

    return InventoryUtils.clickItemNoUse(Vars.getInstance().getCrushItemName(), "Use")
        && InventoryUtils.clickItem(Constants.Items.CHOCOLATE_BAR)
        && Waiting.waitWithABC(() -> Player.getAnimation() != -1, General.random(1200, 1600))
        && Waiting.waitWithABC(() -> Inventory.getCount(Constants.Items.CHOCOLATE_BAR) == 0,
          General.random(timeToCrush+1000, timeToCrush+3000), 1200);
  }
}
