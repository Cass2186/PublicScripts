package scripts.justj.api.utils;

import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.dax_api.shared.helpers.RSItemHelper;
import scripts.dax_api.walker.utils.TribotUtil;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InventoryUtils {

  public static boolean clickItemNoUse(String itemName, String option) {
    return Optional.ofNullable(getClosestToMouse(Inventory.findList(itemName)))
        .map(rsItem -> InventoryUtils.clickItemNoUse(rsItem, option))
        .orElse(false);
  }

  public static boolean clickItemNoUse(RSItem item, String option) {
    if (Game.getItemSelectionState() == 1) {
      item.click();
    }

    return item.click(String.format("%s %s", option, TribotUtil.getName(item)));
  }

  public static boolean clickItem(String itemName, String option) {
    return Optional.ofNullable(getClosestToMouse(Inventory.findList(itemName)))
        .map(rsItem -> rsItem.click(String.format("%s %s", option, itemName)))
        .orElse(false);
  }

  public static boolean clickItem(String itemName) {
    return Optional.ofNullable(getClosestToMouse(Inventory.findList(itemName)))
        .map(rsItem -> rsItem.click())
        .orElse(false);
  }

  /**
   * @author dax
   */
  public static RSItem getClosestToMouse(List<RSItem> rsItems){
    Point mouse = Mouse.getPos();
    rsItems.sort(Comparator.comparingInt(o -> (int) getCenter(o.getArea()).distance(mouse)));
    return rsItems.size() > 0 ? rsItems.get(0) : null;
  }

  private static Point getCenter(Rectangle rectangle){
    return new Point(rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2);
  }

}
