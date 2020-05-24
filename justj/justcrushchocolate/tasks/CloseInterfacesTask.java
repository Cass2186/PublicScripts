package scripts.justj.justcrushchocolate.tasks;

import org.tribot.api2007.Banking;
import org.tribot.api2007.GrandExchange;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.BankingUtils;
import scripts.justj.justcrushchocolate.State;

public class CloseInterfacesTask implements Task {
  @Override
  public Priority priority() {
    return Priority.HIGH;
  }

  @Override
  public boolean isValid() {
    return (State.getInstance().isBuying() && Banking.isBankScreenOpen())
        || (!State.getInstance().isBuying() && GrandExchange.getWindowState() != null);
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Closing interfaces");
    return BankingUtils.closeBank() && GrandExchange.close();
  }
}
