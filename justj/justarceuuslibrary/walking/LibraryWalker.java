package scripts.justj.justarceuuslibrary.walking;

import com.google.common.collect.Lists;
import org.tribot.api.General;
import org.tribot.api2007.Objects;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.justj.api.utils.ABCUtil;
import scripts.justj.api.JustLogger;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.justj.api.utils.Waiting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.justj.justarceuuslibrary.LibraryUtils;
import scripts.justj.justarceuuslibrary.State;
import scripts.justj.justarceuuslibrary.library.SolvedState;

import java.util.*;

import static scripts.justj.justarceuuslibrary.Constants.Rooms.*;

/**
 * This has got to be up there with the worse code I've written. But it works.
 *
 * Rather than rely on dax walker for this, I wanted to write my own pathfinding. This is possibly
 * the worst way I could have done it, but it does seem to work okay.
 */
public class LibraryWalker {

  private static final JustLogger LOGGER = new JustLogger(LibraryWalker.class);

  private static final LibraryWalker instance = new LibraryWalker();

  public static LibraryWalker get() {
    return instance;
  }

  private LibraryWalker() {
    initialiseLinks();
  }

  public boolean walkTo(Room destRoom) {

    Room currentRoom = getCurrentRoom();

    if (currentRoom.equals(destRoom)) {
      LOGGER.debug("We're in the current room");
      return true;
    }

    List<Room> route = getRouteTo(currentRoom, destRoom);
    LOGGER.info("Current room " + currentRoom);
    LOGGER.info("Found route! " + route);


    return walkRoute(currentRoom, route);

  }


  public boolean isInRoom(Room room) {
    return room.getWalkTo().getPlane() == Game.getPlane()
        && room.getArea().contains(Player.getPosition());
  }

  private Room getCurrentRoom() {
    return ALL_ROOMS.stream()
        .filter(room -> room.getWalkTo().getPlane() == Game.getPlane())
        .filter(room -> room.getArea().contains(Player.getPosition()))
        .findFirst()
        .orElseGet(() -> {
          if (Game.getPlane() == 0) {
            return ROOM_BOTTOM_MIDDLE;
          } else if (Game.getPlane() == 2) {
            return ROOM_TOP_MIDDLE;
          } else {
            LOGGER.error("We're not sure where we are. Fail safely. Coords: " + Player.getPosition());
            DaxWalker.walkTo(ROOM_BOTTOM_MIDDLE.getWalkTo());
            return ROOM_BOTTOM_MIDDLE;
          }
        });
  }

  private boolean walkRoute(Room startRoom, List<Room> route) {

    Room currentRoom = startRoom;

    for (Room nextRoom : route) {

      ABCUtil.performRunActivation();
      int attempts = 0;

      while (attempts <= 1) {

        if (currentRoom.getRoomLinkTypeToRoom(nextRoom) == RoomLinkType.WALK) {

          WebWalking.walkTo(nextRoom.getArea().getRandomTile(), () -> isInRoom(nextRoom), 50);

          if (!Waiting.waitAfterWalking(() -> isInRoom(nextRoom),  General.random(1000, 2000))) {
            LOGGER.error("Could not walk to " + nextRoom);
            attempts++;
          } else {
            break;
          }

        } else {

          RSObject staircase;

          RSTile stairsTile;

          if (currentRoom.getRoomLinkTypeToRoom(nextRoom) == RoomLinkType.UP) {
            stairsTile = currentRoom.getUpStairs();
          } else {
            stairsTile = currentRoom.getDownStairs();
          }

          staircase = Arrays.stream(Objects.getAt(stairsTile, rsObject -> TribotUtil.getName(rsObject).equals("Stairs")))
              .findFirst().orElse(null);

          if (!clickStaircase(staircase, nextRoom)) {
            attempts++;
          } else {
            break;
          }
        }
        General.sleep(40, 70);
      }
      if (attempts > 1) {
        return false;
      } else {
        if (State.get().getLibrary().getState() == SolvedState.COMPLETE) {
          checkForBooks(nextRoom);
        }
        currentRoom = nextRoom;
      }
    }

    return true;

  }

  private void checkForBooks(Room room) {
    LOGGER.debug(String.format("Checking current room %s for books", room));

    State.get().getLibrary().getBookcasesInRoom(room).
        stream()
        .filter(LibraryUtils::bookcaseContainsNewBook)
        .forEach(bookshelf -> {
          LOGGER.info(String.format("Checking bookshelf as we believe it has the following books: %s", bookshelf.getPossibleBooks()));
          LibraryUtils.clickBookshelf(bookshelf);
        });
  }

  private boolean clickStaircase(RSObject staircase, Room nextRoom) {
    if (staircase != null) {

      if (!staircase.isOnScreen() && staircase.getPosition().distanceTo(Player.getPosition()) > General.random(5, 8)) {
        Walking.blindWalkTo(staircase);
      }

      if (!RSObjectUtils.click(staircase, "Climb")
          || !Waiting.waitAfterWalking(() -> isInRoom(nextRoom), 3000)) {

        LOGGER.error("Could not click staircase");
        return false;
      }

    } else {
      LOGGER.error("Could not find staircase");
      return false;
    }

    return true;
  }

  private void initialiseLinks() {
    Room.connectRoomsWalking(ROOM_BOTTOM_NE, ROOM_BOTTOM_NW);
    Room.connectRoomsWalking(ROOM_BOTTOM_NE, ROOM_BOTTOM_SW);
    Room.connectRoomsWalking(ROOM_BOTTOM_NW, ROOM_BOTTOM_SW);

    Room.connectRoomsWalking(ROOM_BOTTOM_NE, ROOM_BOTTOM_MIDDLE);
    Room.connectRoomsWalking(ROOM_BOTTOM_NW, ROOM_BOTTOM_MIDDLE);
    Room.connectRoomsWalking(ROOM_BOTTOM_SW, ROOM_BOTTOM_MIDDLE);

    Room.connectRoomsWalking(ROOM_TOP_NE, ROOM_TOP_NW);
    Room.connectRoomsWalking(ROOM_TOP_NE, ROOM_TOP_SW);
    Room.connectRoomsWalking(ROOM_TOP_NW, ROOM_TOP_SW);

    Room.connectRoomsWalking(ROOM_TOP_MIDDLE, ROOM_TOP_NE);
    Room.connectRoomsWalking(ROOM_TOP_MIDDLE, ROOM_TOP_NW);
    Room.connectRoomsWalking(ROOM_TOP_MIDDLE, ROOM_TOP_SW);

    Room.connectRoomsStairs(ROOM_BOTTOM_NE, ROOM_MIDDLE_NE);
    Room.connectRoomsStairs(ROOM_MIDDLE_NE, ROOM_TOP_NE);

    Room.connectRoomsStairs(ROOM_BOTTOM_NW, ROOM_MIDDLE_NW);
    Room.connectRoomsStairs(ROOM_MIDDLE_NW, ROOM_TOP_NW);

    Room.connectRoomsStairs(ROOM_BOTTOM_SW, ROOM_MIDDLE_SW);
    Room.connectRoomsStairs(ROOM_MIDDLE_SW, ROOM_TOP_SW);

    Room.connectRoomsStairs(ROOM_MIDDLE_MIDDLE, ROOM_TOP_MIDDLE);
  }

  private List<Room> getRouteTo(Room startRoom, Room destRoom) {

    if (startRoom.equals(destRoom)) {
      return Lists.newArrayList();
    }

    Queue<List<Room>> pathQueue = new ArrayDeque<>();

    Set<Room> visitedRooms = new HashSet<>();

    pathQueue.add(new ArrayList<>(Collections.singletonList(startRoom)));

    Room currentRoom;

    List<Room> currentPath;

    while (!pathQueue.isEmpty()) {
      currentPath = pathQueue.remove();

      currentRoom = currentPath.get(currentPath.size()-1);

      if (!visitedRooms.contains(currentRoom)) {
        Set<Room> neighbours = currentRoom.getConnectedRooms();
        for (Room neighbour : neighbours) {

          List<Room> newPath = new ArrayList<>(currentPath);
          newPath.add(neighbour);
          pathQueue.add(newPath);

          if (neighbour.equals(destRoom)) {
            newPath.remove(0);
            return newPath;
          }

        }
        visitedRooms.add(currentRoom);

      }
    }

    LOGGER.error(String.format("Could not find path %s to %s", startRoom.getArea(), destRoom.getArea()));
    return Lists.newArrayList();
  }


}
