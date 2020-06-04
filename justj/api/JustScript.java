package scripts.justj.api;

import org.junit.platform.commons.util.StringUtils;
import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dax_api.walker_engine.WalkingCondition;
import scripts.justj.api.gui.GUI;
import scripts.justj.api.listener.MuleListener;
import scripts.justj.api.listener.MuleObserver;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.task.Task;
import scripts.justj.api.task.TaskSet;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.justmortmyre.State;
import scripts.justj.justmortmyre.Vars;
import scripts.justj.justmortmyre.paint.MainPaintTab;
import scripts.justj.justmortmyre.paint.SuppliesPaintTab;
import scripts.justj.justmortmyre.tasks.*;
import scripts.justj.justmortmyre.tasks.mule.MuleBankTask;
import scripts.justj.justmortmyre.tasks.mule.MuleTradeTask;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;


public abstract class JustScript extends Script implements Starting, Painting, EventBlockingOverride, MuleListener {

  private static final JustLogger LOGGER = new JustLogger(JustScript.class);

  private Paint paint;

  @Override
  public void onStart() {
    //Using a free dax walker key
    DaxWalker.setCredentials(new DaxCredentialsProvider() {
      @Override
      public DaxCredentials getDaxCredentials() {
        return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
      }
    });

    DaxWalker.setGlobalWalkingCondition(() -> {
        ABCUtil.performRunActivation();
        return WalkingCondition.State.CONTINUE_WALKER;
    });

    setUpGUI();
    paint = getPaint();
    paint.start();
  }

  @Override
  public void run() {

    int attempts = 0;
    while (isRunning() && attempts < 5) {

      Optional<Task> optionalTask = getTasks().getNextTask();

      if (optionalTask.isPresent()) {
        Task task = optionalTask.get();
        if (!task.getStatus().equals(getStatus())) {
          setStatus(task.getStatus());
          LOGGER.info(task.getStatus());
        }
        if (task.run()) {
          attempts = 0;
        } else {
          LOGGER.error(String.format("Unable to perform %s", task.getStatus()));
          attempts++;
          General.sleep(1000);
        }
      } else {
        LOGGER.error("No task available");
        attempts++;
        sleep(1000);
      }
      sleep(32, 64);
    }

  }

  protected abstract Paint getPaint();

  protected abstract String getGui();

  protected abstract TaskSet getTasks();

  protected abstract boolean isRunning();

  protected abstract void setRunning(boolean running);

  protected abstract String getStatus();

  protected abstract void setStatus(String status);

  @Override
  public void onPaint(Graphics graphics) {
    if (paint != null) {
      Graphics2D g = (Graphics2D) graphics;
      paint.paint(g);
    }
  }

  @Override
  public OVERRIDE_RETURN overrideKeyEvent(KeyEvent keyEvent) {
    return OVERRIDE_RETURN.PROCESS;
  }

  @Override
  public OVERRIDE_RETURN overrideMouseEvent(MouseEvent mouseEvent) {
    if (paint != null) {
      return paint.processEvent(mouseEvent);
    }
    return OVERRIDE_RETURN.PROCESS;
  }

  //TODO better way then dumping the file in here?
  private void setUpGUI() {
    String guiString = getGui();
    if (guiString != null) {
      GUI gui = new GUI(guiString);
      gui.show();
      while (gui.isOpen()) {
        sleep(500);
      }
    }

  }

  @Override
  public void onMuleNearby(String muleName) {

  }

  @Override
  public void onMuleLeave(String muleName) {

  }
}
