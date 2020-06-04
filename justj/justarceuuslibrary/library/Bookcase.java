package scripts.justj.justarceuuslibrary.library;

import org.apache.commons.lang3.NotImplementedException;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSTile;
import scripts.justj.justarceuuslibrary.Constants;
import scripts.justj.justarceuuslibrary.walking.Room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bookcase implements Positionable {
  Bookcase(RSTile location) {
    this.location = location;
    this.room = Constants.Rooms.ALL_ROOMS.stream()
        .filter(room -> room.getArea().contains(location))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Invalid bookcase"));
    this.index = new ArrayList<>();
  }

  private final RSTile location;
  private final Room room;

  private final List<Integer> index;

  private boolean isBookSet;

  /**
   * Book in this bookcase as found by the player.
   * Will be correct as long as isBookSet is true, unless the library has reset;
   */
  private Book book;

  /**
   * Books that can be in this slot. Will only be populated if library.state != SolvedState.NO_DATA
   */
  private Set<Book> possibleBooks = new HashSet<>();

  void clearBook() {
    book = null;
    isBookSet = false;
  }

  void setBook(Book book) {
    this.book = book;
    this.isBookSet = true;
  }

  String getLocationString() {
    StringBuilder b = new StringBuilder();

    // Floors 2 and 3
    boolean north = location.getY() > 3815;
    boolean west = location.getX() < 1625;

    // Floor 1 has slightly different dimensions
    if (location.getPlane() == 0) {
      north = location.getY() > 3813;
      west = location.getX() < 1627;
    }

    if (north && west) {
      b.append("Northwest");
    } else if (north) {
      b.append("Northeast");
    } else if (west) {
      b.append("Southwest");
    } else {
      b.append("Center");
    }

    b.append(" ");

    switch (location.getPlane()) {
      case 0:
        b.append("ground floor");
        break;
      case 1:
        b.append("middle floor");
        break;
      case 2:
        b.append("top floor");
        break;
    }

    return b.toString();
  }

  public Book getBook() {
    return book;
  }

  public Set<Book> getPossibleBooks() {
    return possibleBooks;
  }

  public RSTile getLocation() {
    return location;
  }

  public Room getRoom() {
    return room;
  }

  public List<Integer> getIndex() {
    return index;
  }

  public boolean isBookSet() {
    return isBookSet;
  }

  @Override
  public RSTile getAnimablePosition() {
    return location;
  }

  @Override
  public RSTile getPosition() {
    return location;
  }

  @Override
  public boolean adjustCameraTo() {
    throw new NotImplementedException("Not implemented");
  }
}
