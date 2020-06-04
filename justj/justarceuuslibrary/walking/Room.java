package scripts.justj.justarceuuslibrary.walking;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Room {

  public static void connectRoomsWalking(Room room1, Room room2) {
    room1.addConnectedRoom(room2, RoomLinkType.WALK);
    room2.addConnectedRoom(room1, RoomLinkType.WALK);
  }

  public static void connectRoomsStairs(Room lowerRoom, Room upperRoom) {
    lowerRoom.addConnectedRoom(upperRoom, RoomLinkType.UP);
    upperRoom.addConnectedRoom(lowerRoom, RoomLinkType.DOWN);
  }


  private final String name;
  private final RSArea rsArea;
  private final RSArea walkToArea;

  private final RSTile upStairs;
  private final RSTile downStairs;

  private final Map<Room, RoomLinkType> connectedRooms;

  public Room(String name, RSArea rsArea, RSTile upStairs, RSTile downStairs) {
    this.name = name;
    this.walkToArea = rsArea;
    this.rsArea = rsArea;
    this.upStairs = upStairs;
    this.downStairs = downStairs;
    this.connectedRooms = new HashMap<>();
  }

  public Room(String name, RSArea rsArea, RSArea walkToArea, RSTile upStairs, RSTile downStairs) {
    this.name = name;
    this.walkToArea = walkToArea;
    this.rsArea = rsArea;
    this.upStairs = upStairs;
    this.downStairs = downStairs;
    this.connectedRooms = new HashMap<>();
  }


  public RSArea getArea() {
    return rsArea;
  }

  public RSTile getWalkTo() {
    return walkToArea.getRandomTile();
  }

  public RSTile getUpStairs() {
    return upStairs;
  }

  public RSTile getDownStairs() {
    return downStairs;
  }

  public RoomLinkType getRoomLinkTypeToRoom(Room destRoom) {
    return connectedRooms.get(destRoom);
  }

  public Set<Room> getConnectedRooms() {
    return connectedRooms.keySet();
  }

  public void addConnectedRoom(Room room, RoomLinkType roomLinkType) {
    connectedRooms.put(room, roomLinkType);
  }

  @Override
  public String toString() {
    return name;
  }
}
