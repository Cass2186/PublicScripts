package scripts.justj.justwintertodt.tasks;

import org.tribot.api2007.Player;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.justj.justwintertodt.State;

public class TravelToWintertodtTask implements Task {

  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return Player.getPosition().distanceTo(RunescapeBank.WINTERTODT.getPosition()) > 100;
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Traveling");
    return DaxWalker.walkToBank(RunescapeBank.WINTERTODT);
  }
}
