package scripts.justj.justwintertodt;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.justj.api.utils.RSObjectUtils;
import scripts.dax_api.walker.utils.TribotUtil;

import java.util.Arrays;
import java.util.Optional;

public class WintertodtUtils {

  public static Optional<RSObject> getBrazier() {
    return Arrays.stream(Objects.getAt(Constants.Tiles.WINTERTODT_BRAZIER_TILE))
        .findAny();
  }

  public static Optional<RSObject> getLitBrazier() {
    return getBrazier().filter(e -> RSObjectUtils.hasActionContaining(e, "Feed"));
  }

  public static Optional<RSObject> getUnlitBrazier() {
    return getBrazier().filter(e -> RSObjectUtils.hasActionContaining(e, "Light"));
  }

  public static boolean isPyroAlive() {
    return !org.tribot.api2007.NPCs.isAt(Constants.Tiles.WINTERTODT_PYRO_TILE, Constants.NPCs.INCAPACITATED_PYROMANCER);
  }


  public static Optional<RSObject> getBrokenBrazier() {
    return getBrazier().filter(e -> RSObjectUtils.hasActionContaining(e, "Fix"));
  }

  public static Optional<RSObject> getBurmaRoots() {
    return Arrays.stream(Objects.getAt(Constants.Tiles.WINTERTODT_BURMA_ROOTS_TILE))
        .findAny();
  }

  public static boolean hoverBrazier() {
    return WintertodtUtils.getBrazier().map(rsObject -> rsObject.getModel().getEnclosedArea().contains(Mouse.getPos())
        || AccurateMouse.hover(rsObject, "Feed", "Light", "Fix"))
        .orElse(true);
  }

  public static Optional<RSObject> getDoor() {
    return Arrays.stream(Objects.getAt(Constants.Tiles.WINTERTODT_BURMA_ROOTS_TILE))
        .filter(rsObject -> TribotUtil.getName(rsObject).equals("Doors of Dinh"))
        .findAny();
  }

  public static Optional<RSObject> getBank() {
    return Arrays.stream(Objects.getAt(Constants.Tiles.WINTERTODT_BANK_TILE))
        .filter(rsObject -> TribotUtil.getName(rsObject).equals("Bank chest"))
        .findAny();
  }

  public static boolean isOutsideWintertodt() {
    return Player.getPosition().getY() < Constants.Tiles.WINTERTODT_DOOR_TILE.getY();
  }


}
