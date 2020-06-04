package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;

public class WaitForHealTask implements Task {
  @Override
  public Priority priority() {
    return Priority.NONE;
  }

  @Override
  public boolean isValid() {
    return !WintertodtUtils.isOutsideWintertodt()
        && State.getInstance().hasGameStarted()
        && !WintertodtUtils.isPyroAlive();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Waiting for pyro to revive");
    General.sleep(100);


    return true;
  }
}
