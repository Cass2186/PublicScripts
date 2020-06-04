package scripts.justj.justwintertodt.tasks;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.utils.Waiting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.justj.api.utils.WalkingHandler;
import scripts.justj.justwintertodt.Constants;
import scripts.justj.justwintertodt.State;
import scripts.justj.justwintertodt.WintertodtUtils;
import scripts.justj.justwintertodt.Activity;

public class WaitForStartTask implements Task {
  @Override
  public Priority priority() {
    return Priority.LOW;
  }

  @Override
  public boolean isValid() {
    return !State.getInstance().hasGameStarted() || WintertodtUtils.isOutsideWintertodt();
  }

  @Override
  public boolean run() {
    State.getInstance().setStatus("Waiting for game to start");

    if (!isCloseToBrazier()) {
      ABCUtil.performRunActivation();
      return WalkingHandler.walkPath(Constants.Paths.FROM_BANK_TO_BRAZIER)
          && Waiting.waitWithABC(this::isCloseToBrazier, General.random(1231, 3242));
    }

    if (!Player.getPosition().equals(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE)) {
      return Walking.walkTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE)
          && Waiting.waitCondition(() -> Player.getPosition().equals(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE), 3000);
    }


    State.getInstance().currentActivity = Activity.IDLE;
    ABCUtil.performActions();

    return WintertodtUtils.hoverBrazier();
  }

  private boolean isCloseToBrazier() {
    return Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_STAND_BRAZIER_TILE) <= 5;
  }


}
