package scripts.justj.justcrushchocolate.tasks.making;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.api.JustLogger;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.BankingUtils;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justcrushchocolate.Constants;
import scripts.justj.justcrushchocolate.State;
import scripts.justj.justcrushchocolate.Statistics;
import scripts.justj.justcrushchocolate.Vars;

public class BankTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(BankTask.class);

  @Override
  public Priority priority() {
    return Priority.NONE;
  }

  @Override
  public boolean isValid() {
    return Inventory.getCount(Constants.Items.CHOCOLATE_BAR) == 0
        || Inventory.getCount(Vars.getInstance().getCrushItemName()) == 0;
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Banking");

    if (!Banking.isInBank()) {
      LOGGER.error("We're not in a bank. Exiting");
      State.getInstance().setRunning(false);
      return false;
    }

    if (!Banking.isBankScreenOpen()) {
      return Banking.openBank() && Waiting.waitCondition(Banking::isBankLoaded, General.random(3000, 4000));
    }

    if (Inventory.getAllList().stream().anyMatch(this::isUnexpectedItem)) {
      LOGGER.info("We found an unexpected item, banking all");
      return Banking.depositAllExcept(Vars.getInstance().getCrushItemName()) > 0
          && Waiting.waitCondition(() -> Inventory.getAll().length == 0, General.random(800, 1200));
    }

    if (Inventory.getCount(Vars.getInstance().getCrushItemName()) == 0) {
      LOGGER.info(String.format("Withdrawing %s", Vars.getInstance().getCrushItemName()));
      return Banking.withdraw(1, Vars.getInstance().getCrushItemName())
          && Waiting.waitCondition(() -> Inventory.getCount(Vars.getInstance().getCrushItemName()) > 0, General.random(800, 1200));
    }

    if (Inventory.getCount(Constants.Items.CHOCOLATE_DUST) > 0) {
      return Banking.deposit(0, Constants.Items.CHOCOLATE_DUST)
          && Waiting.waitCondition(() -> Inventory.getCount(Constants.Items.CHOCOLATE_DUST) == 0, General.random(800, 1200));
    }

    if (Banking.find(Constants.Items.CHOCOLATE_BAR).length == 0) {
      LOGGER.info("We need to buy more chocolate bars");
      State.getInstance().setBuying(true);
      return BankingUtils.setNoted(true) && Banking.withdraw(0, Constants.Items.CHOCOLATE_DUST)
          && BankingUtils.closeBank();
    }

    return BankingUtils.setNoted(false) && Banking.withdraw(0, Constants.Items.CHOCOLATE_BAR)
        && BankingUtils.closeBank();
  }


  private boolean isUnexpectedItem(RSItem item) {
    String name = TribotUtil.getName(item);

    return !Constants.Items.CHOCOLATE_BAR.equals(name)
        && !Constants.Items.CHOCOLATE_DUST.equals(name)
        && !Vars.getInstance().getCrushItemName().equals(name);

  }
}
