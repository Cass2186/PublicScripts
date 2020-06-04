package scripts.justj.justwintertodt;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSVarBit;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import scripts.justj.api.listener.*;
import scripts.justj.api.task.Task;
import scripts.justj.api.task.TaskSet;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.utils.Waiting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.justwintertodt.gui.WintertodtGui;
import scripts.justj.justwintertodt.gui.Vars;
import scripts.justj.justwintertodt.paint.MainPaintTab;
import scripts.justj.justwintertodt.tasks.*;
import scripts.justj.api.paint.Paint;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@ScriptManifest(name = "JustWintertodt", authors = "JustJ", category = "Firemaking",
    description = "Simple wintertodt script", version = 0.3)
public class JustWintertodt extends Script implements AnimationListener, MessageListening07, VarBitListener, Painting,
    Breaking {

  private static final TaskSet tasks = new TaskSet(
      new EatTask(),
      new ChopRootsTask(),
      new TravelToWintertodtTask(),
      new FletchTask(),
      new FeedBrazierTask(),
      new LightBrazierTask(),
      new FixBrazierTask(),
      new BankTask(),
      new WaitForStartTask(),
      new WaitForHealTask()
  );

  private Instant lastActionInstant;

  private final Paint paint;

  public JustWintertodt() {
    super();
    paint = new Paint(() -> State.getInstance().getStatus(), "https://i.imgur.com/rQq7kz1.png", new MainPaintTab());
    paint.start();
  }

  @Override
  public void run() {

    setUp();

    new WintertodtGui("JustWintertodt");

    while (Login.getLoginState() != Login.STATE.INGAME || !Vars.getInstance().isGuiComplete()) {
      sleep(400);
    }

    //Check for required items
    if (Inventory.getCount("Tinderbox") == 0 || Inventory.getCount("Knife") == 0
        || Inventory.getCount("Hammer") == 0
        || Inventory.find(item -> TribotUtil.getName(item).contains("axe")).length == 0) {
      System.out.println("We do not have the required items.");
      return;
    }

    waitForGameStart();

    int failCount = 0;
    while (failCount < 5) {

      Optional<Task> optionalTask = tasks.getNextTask();

      if (optionalTask.isPresent()) {
        Task task = optionalTask.get();
        if (task.run()) {
          State.getInstance().lastTask = task;
        }
        checkAnimationTimeout();
        failCount = 0;
      } else {
        System.out.println("No task available");
        failCount++;
      }
      sleep(32, 64);
    }

  }

  private void waitForGameStart() {
    //Wait for game to start (Don't join halfway)
    while (State.getInstance().hasGameStarted()) {
      State.getInstance().setStatus("Waiting for game to finish");
      if (Player.getPosition().distanceTo(Constants.Tiles.WINTERTODT_IDLE_TILE) > 3) {
        DaxWalker.walkTo(Constants.Tiles.WINTERTODT_IDLE_TILE);
      }

      Waiting.waitWithABC(() -> !State.getInstance().hasGameStarted(), 10000);
    }
  }

  public void setUp() {
    DaxWalker.setCredentials(new DaxCredentialsProvider() {
      @Override
      public DaxCredentials getDaxCredentials() {
        return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
      }
    });
    ThreadSettings.get().setClickingAPIUseDynamic(true);
    setAIAntibanState(false);
    initialiseListeners();
    General.sleep(300);
  }

  public void initialiseListeners() {
    AnimationObserver animationObserver = new AnimationObserver(() -> true);
    animationObserver.addListener(this);
    animationObserver.start();
    VarBitObserver varBitObserver = new VarBitObserver(() -> true);
    varBitObserver.addListener(this);
    varBitObserver.addVarbit(Constants.VarBits.WINTERTODT_TIMER);
    varBitObserver.start();
  }


  public void checkAnimationTimeout() {
    if (State.getInstance().currentActivityIn(Activity.BANKING, Activity.IDLE, Activity.TRAVELING)) {
      return;
    }

    int currentAnimation = Player.getAnimation();

    if (currentAnimation != Constants.Animations.IDLE || lastActionInstant == null) {
      return;
    }

    if (Duration.between(lastActionInstant, Instant.now()).compareTo(Duration.ofSeconds(3)) >= 0) {
      State.getInstance().lastTask = null;
    }

  }

  @Override
  public void onAnimationChanged(int newAnimation) {

    switch (newAnimation) {
      case Constants.Animations.WOODCUTTING_BRONZE:
      case Constants.Animations.WOODCUTTING_IRON:
      case Constants.Animations.WOODCUTTING_STEEL:
      case Constants.Animations.WOODCUTTING_BLACK:
      case Constants.Animations.WOODCUTTING_MITHRIL:
      case Constants.Animations.WOODCUTTING_ADAMANT:
      case Constants.Animations.WOODCUTTING_RUNE:
      case Constants.Animations.WOODCUTTING_GILDED:
      case Constants.Animations.WOODCUTTING_DRAGON:
      case Constants.Animations.WOODCUTTING_INFERNAL:
      case Constants.Animations.WOODCUTTING_3A_AXE:
      case Constants.Animations.WOODCUTTING_CRYSTAL:
        setCurrentActivity(Activity.WOODCUTTING);
        break;

      case Constants.Animations.FLETCHING_BOW_CUTTING:
        setCurrentActivity(Activity.FLETCHING);
        break;

      case Constants.Animations.LOOKING_INTO:
        setCurrentActivity(Activity.FEEDING_BRAZIER);
        break;
    }
  }

  private void setCurrentActivity(Activity activity) {
    State.getInstance().currentActivity = activity;
    lastActionInstant = Instant.now();
  }

  @Override
  public void serverMessageReceived(String message) {

    InterruptType interruptType;

    if (message.startsWith("The cold of")) {
      interruptType = InterruptType.COLD;
    } else if (message.startsWith("The freezing cold attack")) {
      interruptType = InterruptType.SNOWFALL;
    } else if (message.startsWith("The brazier is broken and shrapnel")) {
      interruptType = InterruptType.BRAZIER;
    } else if (message.startsWith("You have run out of bruma roots")) {
      interruptType = InterruptType.OUT_OF_ROOTS;
    } else if (message.startsWith("Your inventory is too full")) {
      interruptType = InterruptType.INVENTORY_FULL;
    } else if (message.startsWith("You fix the brazier")) {
      interruptType = InterruptType.FIXED_BRAZIER;
    } else if (message.startsWith("You light the brazier")) {
      interruptType = InterruptType.LIT_BRAZIER;
    } else if (message.startsWith("The brazier has gone out.")) {
      interruptType = InterruptType.BRAZIER_WENT_OUT;
    } else {
      return;
    }

    boolean wasInterrupted = false;

    switch (interruptType) {
      case COLD:
      case BRAZIER:
      case SNOWFALL:
        // All actions except woodcutting are interrupted when taking damage
        if (!State.getInstance().currentActivityIn(Activity.WOODCUTTING)) {
          wasInterrupted = true;
        }
        break;
      case INVENTORY_FULL:
      case OUT_OF_ROOTS:
      case BRAZIER_WENT_OUT:
      case LIT_BRAZIER:
      case FIXED_BRAZIER:
        wasInterrupted = true;
        break;
    }

    if (wasInterrupted) {
      ABCUtil.waitReactionTime(700);
      lastActionInstant = Instant.MIN;
    }
  }

  @Override
  public void onVarbitChanged(RSVarBit varbit, Integer newValue) {
    if (varbit == Constants.VarBits.WINTERTODT_TIMER) {
      State.getInstance().timer = newValue;
    }
  }

  @Override
  public void onPaint(Graphics graphics) {
    if (paint != null) {
      Graphics2D g = (Graphics2D) graphics;
      paint.paint(g);
    }
  }

  @Override
  public void onBreakEnd() {
    waitForGameStart();
  }

  @Override
  public void onBreakStart(long l) {
    DaxWalker.walkTo(Constants.Tiles.WINTERTODT_IDLE_TILE);
  }
}
