package scripts.justj.justarceuuslibrary;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import scripts.justj.api.listener.*;
import scripts.justj.api.task.Task;
import scripts.justj.api.task.TaskSet;
import scripts.justj.api.JustLogger;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.justj.justarceuuslibrary.library.Book;
import scripts.justj.justarceuuslibrary.library.Bookcase;
import scripts.justj.justarceuuslibrary.paint.MainPaintTab;
import scripts.justj.justarceuuslibrary.paint.StatsPaintTab;
import scripts.justj.justarceuuslibrary.tasks.*;
import scripts.justj.api.paint.Paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ScriptManifest(name = "JustArceuusLibrary", authors = "JustJ", category = "Runecrafting",
    description = "Simple runecrafting training script", version = 0.1)
public class JustArceuusLibrary extends Script implements Painting, EventBlockingOverride, MessageListening07,
    InventoryListener, InterfaceListener {


  private static final JustLogger LOGGER = new JustLogger(JustArceuusLibrary.class);

  private static final Pattern NPC_BOOK_EXTRACTOR = Pattern.compile("'<col=0000ff>(.*)</col>'");
  private static final Pattern BOOKCASE_BOOK_EXTRACTOR = Pattern.compile("<col=00007f>(.*)</col>");
  private static final Pattern TAG_MATCHER = Pattern.compile("(<[^>]*>)");

  private static final TaskSet tasks = new TaskSet(
      new SolveTask(),
      new GetNewAssignmentTask(),
      new FindBookTask(),
      new DeliverBookTask(),
      new CashInBooksTask()
  );

  private final Paint paint;

  public JustArceuusLibrary() {
    super();
    paint = new Paint(() -> State.get().getStatus(), "https://i.imgur.com/Akpautc.png", new MainPaintTab(), new StatsPaintTab());
    paint.start();
  }

  @Override
  public void run() {

    DaxWalker.setCredentials(new DaxCredentialsProvider() {
      @Override
      public DaxCredentials getDaxCredentials() {
        return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
      }
    });
    setAIAntibanState(false);

    while (Login.getLoginState() != Login.STATE.INGAME) {
      General.sleep(100);
    }

    initialiseListeners();

    int failCount = 0;
    while (failCount < 5) {

      Optional<Task> optionalTask = tasks.getNextTask();

      if (optionalTask.isPresent()) {
        Task task = optionalTask.get();
        task.run();

        failCount = 0;
      } else {
        System.out.println("No task available");
        failCount++;
      }
      sleep(100);
    }
  }

  public void initialiseListeners() {
    InventoryObserver inventoryObserver = new InventoryObserver(() -> true);
    inventoryObserver.addListener(this);
    inventoryObserver.start();

    InterfaceObserver interfaceObserver = new InterfaceObserver(() -> true);
    interfaceObserver.addListener(this);
    interfaceObserver.addRSInterfaceChild(193, 2);
    interfaceObserver.addRSInterfaceChild(231, 4);
    interfaceObserver.start();
  }

  @Override
  public void serverMessageReceived(String message) {
    LOGGER.debug(String.format("Server message received: '%s'", message));
    if (message.equals("You don't find anything useful here.")) {
      State.get().getLastBookcaseTile().ifPresent(tile -> {
        if (RSObjectUtils.isLookingTowards(Player.getRSPlayer(), tile, 1)) {
          State.get().getLibrary().mark(tile, null);
          State.get().setLastBookcaseTile(null);
        } else {
          LOGGER.info("Misclicked bookcase");
          State.get().setLastBookcaseTile(null);
        }
      });
    }
  }

  //The below is done a bit crappy as I changed the inventory listener implementation
  @Override
  public void inventoryItemGained(String itemName, Long count) {
    State.setCurrentBooksFromInventory(Inventory.getAllList());
  }

  @Override
  public void inventoryItemLost(String itemName, Long count) {
    State.setCurrentBooksFromInventory(Inventory.getAllList());
  }

  @Override
  public void onAppear(RSInterfaceChild rsInterfaceChild) {
    String message = rsInterfaceChild.getText();
    if (message == null) {
      return;
    }

    LOGGER.debug(String.format("Received message: '%s'", message));

    if (message.startsWith("You find:")) {
      Matcher m = BOOKCASE_BOOK_EXTRACTOR.matcher(message);

      System.out.println(message);
      if (m.find()) {
        getBookFromMatcher(m).ifPresent(book -> {
          State.get().getLastBookcaseTile().ifPresent(tile -> State.get().getLibrary().mark(tile, book));
          State.get().setLastBookcaseTile(null);
        });

      }
      return;
    }

    if (message.startsWith("Thanks, human") || message.startsWith("Thank you very much")) {
      State.get().swapProfessors();
      State.get().setCurrentAssignment(null);
      Statistics.get().incrementBooksGained();
      return;
    }

    if (message.startsWith("I believe you are currently")
        || message.startsWith("Thanks for finding my book")
        || message.startsWith("Thank you for finding my book")
        || message.startsWith("Aren't you helping someone")) {
      State.get().swapProfessors();
      return;
    }

    Matcher m = NPC_BOOK_EXTRACTOR.matcher(message);

    if (m.find()) {
      getBookFromMatcher(m).ifPresent(book -> {
        LOGGER.info(String.format("Next book: %s", book.getName()));
        State.get().setCurrentAssignment(book);
      });
      return;
    }

    LOGGER.debug(String.format("Not recognised: '%s'", message));

  }

  private Optional<Book> getBookFromMatcher(Matcher m) {
    String bookName = TAG_MATCHER.matcher(m.group(1).replace("<br>", " ")).replaceAll("");
    Book book = Book.byName(bookName);

    if (book == null) {
      LOGGER.error(String.format("Book %s is not recognised", bookName));
    }

    return Optional.ofNullable(book);
  }

  @Override
  public void onPaint(Graphics graphics) {
    if (paint != null) {
      Graphics2D g = (Graphics2D) graphics;
      paint.paint(g);
      drawBooks(g);
    }
  }

  private void drawBooks(Graphics2D g) {
    g.setColor(Color.GREEN);
    State.get().getLibrary().getBookcases().stream()
        .filter(bookcase -> bookcase.getPossibleBooks().stream().anyMatch(book -> !book.isDarkManuscript())
            || Optional.ofNullable(bookcase.getBook()).filter(book -> !book.isDarkManuscript()).isPresent())
        .map(Bookcase::getPosition)
        .filter(RSTile::isOnScreen)
        .forEach(tile -> g.draw(Projection.getTileBoundsPoly(tile, 0)));


    g.setColor(Color.RED);
    State.get().getLastBookcaseTile()
        .filter(RSTile::isOnScreen)
        .ifPresent(tile -> g.draw(Projection.getTileBoundsPoly(tile, 0)));
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
}
