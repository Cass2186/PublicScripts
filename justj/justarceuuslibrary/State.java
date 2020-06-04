package scripts.justj.justarceuuslibrary;

import com.google.common.collect.Sets;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import scripts.justj.api.JustLogger;
import scripts.justj.justarceuuslibrary.library.Book;
import scripts.justj.justarceuuslibrary.library.Library;
import scripts.justj.justarceuuslibrary.professor.Professor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class State {

  private static final JustLogger LOGGER = new JustLogger(State.class);

  public static final State instance = new State();

  public static State get() {
    return instance;
  }


  public static void setCurrentBooksFromInventory(List<RSItem> inventory) {
    State.get().setCurrentBooks(inventory.stream().map(rsItem -> Book.byId(rsItem.getID())).collect(toList()));
  }

  private String status = "Starting...";

  private final Library library;
  private RSTile lastBookcaseTile;
  private List<Book> currentBooks;
  private Book currentAssignment;
  private Professor currentProfessor;

  public State() {
    this.library = new Library();
  }

  public Library getLibrary() {
    return library;
  }

  public Optional<RSTile> getLastBookcaseTile() {
    return Optional.ofNullable(lastBookcaseTile);
  }

  public void setLastBookcaseTile(RSTile lastBookcaseTile) {
    this.lastBookcaseTile = lastBookcaseTile;
  }

  public List<Book> getCurrentBooks() {
    return currentBooks;
  }

  public void setCurrentBooks(List<Book> currentBooks) {
    this.currentBooks = currentBooks;
  }

  public Optional<Book> getCurrentAssignment() {
    return Optional.ofNullable(currentAssignment);
  }

  public void setCurrentAssignment(Book currentAssignment) {
    this.currentAssignment = currentAssignment;
  }

  public Professor getCurrentProfessor() {
    return Optional.ofNullable(currentProfessor).orElse(Constants.Professors.GRACKLEBONE);
  }

  //TODO not really state, remove from here
  public void swapProfessors() {
    currentProfessor = getPossibleProfessors().stream()
        .filter(professor -> professor != getCurrentProfessor())
        .findAny()
        .orElse(Constants.Professors.GRACKLEBONE);
  }

  private Set<Professor> getPossibleProfessors() {
    return Sets.newHashSet(Constants.Professors.GRACKLEBONE, Constants.Professors.VILLIA);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    if (!status.equals(this.status)) {
      LOGGER.info(status);
      this.status = status;
    }
  }
}
