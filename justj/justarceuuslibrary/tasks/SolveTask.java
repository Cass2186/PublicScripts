package scripts.justj.justarceuuslibrary.tasks;

import org.tribot.api.util.Sorting;
import org.tribot.api2007.Player;
import org.tribot.api2007.WorldHopper;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.JustLogger;
import scripts.dax_api.shared.helpers.WorldHelper;
import scripts.justj.justarceuuslibrary.LibraryUtils;
import scripts.justj.justarceuuslibrary.State;
import scripts.justj.justarceuuslibrary.library.Bookcase;
import scripts.justj.justarceuuslibrary.library.SolvedState;

import java.util.Arrays;

public class SolveTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(SolveTask.class);

  /**
   * TODO
   * - make it not check both sides of a sticking out bit, it's weird
   * - Needs trade in books for xp task
   * -  wtf it resets the library so often :(
   * - Add painting of tiles
   * - Rounds books per/h
   * - Fix sleep after misclicking
   * - Stop cpu getting crushed (Maybe background thread for books in rooms? log to see how often it's happening)
   *    - only check once per room entry
   *
   * - Before clicking for book, check we didn't just do it?
   * - Walk to actual tile, not to random one
   *
   */

  @Override
  public Priority priority() {
    return Priority.HIGH;
  }

  @Override
  public boolean isValid() {
    return State.get().getLibrary().getState() == SolvedState.NO_DATA;
  }

  @Override
  public boolean run() {
    State.get().setStatus("Solving the library");
    LOGGER.info("Solving!");

    Bookcase[] bookcasesArray = State.get().getLibrary().getBookcasesOnLevel(Player.getPosition().getPlane())
        .stream()
        .filter(bookcase -> !bookcase.isBookSet())
        .filter(bookcase ->
            bookcase.getPossibleBooks().stream().anyMatch(book -> !book.isDarkManuscript()) || State.get().getLibrary().getState() == SolvedState.NO_DATA)
        .toArray(Bookcase[]::new);

    if (bookcasesArray.length == 0) {
      bookcasesArray =  State.get().getLibrary().getBookcases()
          .stream()
          .filter(bookcase -> !bookcase.isBookSet())
          .filter(bookcase ->
              bookcase.getPossibleBooks().stream().anyMatch(book -> !book.isDarkManuscript()) || State.get().getLibrary().getState() == SolvedState.NO_DATA)
          .toArray(Bookcase[]::new);
      LOGGER.info(String.format("We are unsure on %s bookcases total", bookcasesArray.length));

      if (bookcasesArray.length == 0) {
        LOGGER.error("We're confused. Hop to reset");

        int world = WorldHopper.getRandomWorld(true, false);
        if (!WorldHelper.isPvp(world)) {
          WorldHopper.changeWorld(world);
        }
      }
    } else {

      LOGGER.info(String.format("We are unsure on %s bookcases this floor", bookcasesArray.length));
    }


    Sorting.sortByDistance(bookcasesArray, Player.getPosition(), true);

    return Arrays.stream(bookcasesArray).findFirst()
        .map(LibraryUtils::clickBookshelf)
        .orElse(false);
  }
}
