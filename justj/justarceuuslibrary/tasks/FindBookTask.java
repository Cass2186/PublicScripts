package scripts.justj.justarceuuslibrary.tasks;

import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.JustLogger;
import scripts.justj.justarceuuslibrary.LibraryUtils;
import scripts.justj.justarceuuslibrary.State;
import scripts.justj.justarceuuslibrary.library.Book;
import scripts.justj.justarceuuslibrary.library.Bookcase;

import java.util.List;

public class FindBookTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(FindBookTask.class);

  @Override
  public Priority priority() {
    return Priority.NONE;
  }

  @Override
  public boolean isValid() {
    return State.get().getCurrentAssignment().isPresent()
        && !State.get().getCurrentBooks().contains(State.get().getCurrentAssignment().get());
  }

  @Override
  public boolean run() {
    State.get().setStatus("Looking for books");
    LOGGER.info("Looking for book");


    List<Bookcase> bookcases = State.get().getLibrary().getBookcases();

    return State.get().getCurrentAssignment()
        .map(LibraryUtils::findBook)
        .map(LibraryUtils::clickBookshelf)
        .orElseGet(() -> {
          LOGGER.error(String.format("We can't find %s",
              State.get().getCurrentAssignment().map(Book::getName).orElse("Unknown")));
          bookcases.stream()
              .filter(bookcase -> bookcase.getPossibleBooks().size() > 0)
              .forEach(bookcase -> LOGGER.debug(String.format("Bookcase: %s, books: %s", bookcase, bookcase.getPossibleBooks())));
          // force reset?
          return false;
        });
  }
}
