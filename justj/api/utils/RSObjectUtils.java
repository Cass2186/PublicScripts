package scripts.justj.api.utils;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSAnimableEntity;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker.utils.TribotUtil;
import scripts.dax_api.walker.utils.camera.DaxCamera;
import scripts.justj.api.JustLogger;

import java.util.Arrays;
import java.util.Optional;

public class RSObjectUtils {

  private static final JustLogger LOGGER = new JustLogger(RSObjectUtils.class);

  public static boolean hasActionContaining(RSObject rsObject, String action) {
    return StreamUtils.streamOptional(Optional.ofNullable(rsObject)
        .flatMap(object -> Optional.ofNullable(object.getDefinition())))
        .flatMap(rsObjectDefinition -> Arrays.stream(rsObjectDefinition.getActions()))
        .anyMatch(e -> e.contains(action));
  }

  public static boolean click(RSObject rsObject, String action) {
    if (!action.contains("Use") && Game.getItemSelectionState() == 1) {
      //Unselect item
      InventoryUtils.clickItem(Game.getSelectedItemName());
    }

    if (!rsObject.isOnScreen() || !rsObject.isClickable()) {
      DaxCamera.focus(rsObject);
    }

    LOGGER.info(String.format("Clicking %s with %s", TribotUtil.getName(rsObject), action));
    return AccurateMouse.click(rsObject, action);
  }


  /**
   * Determines if an object is being interacted with by the player
   *
   * @author wastedbro
   */
  public static boolean isInteractingWithObject(RSObject object)
  {
    RSPlayer player = Player.getRSPlayer();
    return player.getAnimation() > -1 && Arrays.stream(object.getAllTiles()).anyMatch(t -> isLookingTowards(player, t, 1));
  }


  /**
   * Checks if the animable entity is looking towards a positionable
   *
   * @author wastedbro
   */
  public static boolean isLookingTowards(RSAnimableEntity animableEntity, Positionable positionable, int maxDist)
  {
    if (maxDist > 0 && positionable.getPosition().distanceTo(animableEntity) > maxDist) return false;

    final int orientation = (int) Math.round(animableEntity.getOrientation() / 256.0);
    final int dx = animableEntity.getPosition().getX() - positionable.getPosition().getX();
    final int dy = animableEntity.getPosition().getY() - positionable.getPosition().getY();
    switch (orientation)
    {
      case 0: //south
        return dx == 0 && dy > 0;
      case 1: //south - west
        return dx > 0 && dy > 0 && dx == dy;
      case 2: //west
        return dx > 0 && dy == 0;
      case 3: //north - west
        return dx > 0 && dy < 0 && Math.abs(dx) == Math.abs(dy);
      case 4: //north
        return dx == 0 && dy < 0;
      case 5: //north - east
        return dx < 0 && dy < 0 && dx == dy;
      case 6: //east
        return dx < 0 && dy == 0;
      case 7: //south-east
        return dx < 0 && dy > 0 && Math.abs(dx) == Math.abs(dy);
    }
    return false;
  }

}
