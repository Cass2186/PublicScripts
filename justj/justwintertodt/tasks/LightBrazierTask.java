package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;

public class LightBrazierTask implements Task {

  @Override
  public Priority priority() {
    return Priority.MEDIUM;
  }

  @Override
  public boolean isValid() {
    return State.getInstance().hasGameStarted()
        && Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE) < 3
        && WintertodtUtils.getUnlitBrazier().isPresent()
        && WintertodtUtils.isPyroAlive();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Lighting");
    int xp = Skills.getXP(Skills.SKILLS.FIREMAKING);

    return WintertodtUtils.getUnlitBrazier().map(rsObject -> RSObjectUtils.click(rsObject, "Light")).orElse(false)
     && Timing.waitCondition(() -> Skills.getXP(Skills.SKILLS.FIREMAKING) != xp, General.random(1500, 2100));
  }
}
