package scripts.justj.api.utils;

import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker.utils.camera.DaxCamera;

public class MouseUtils {

  public static boolean click(RSNPC rsnpc, String... clickActions) {
    if (!rsnpc.isOnScreen() || !rsnpc.isClickable()) {
      DaxCamera.focus(rsnpc);
    }

    return AccurateMouse.click(rsnpc, clickActions);
  }

  public static boolean hover(RSNPC rsnpc, String... clickActions) {
    if (!rsnpc.isOnScreen() || !rsnpc.isClickable()) {
      DaxCamera.focus(rsnpc);
    }

    return AccurateMouse.hover(rsnpc, clickActions);
  }

  public static boolean click(RSObject rsObject, String... clickActions) {
    if (!rsObject.isOnScreen() || !rsObject.isClickable()) {
      DaxCamera.focus(rsObject);
    }

    return AccurateMouse.click(rsObject, clickActions);
  }

  public static boolean hover(RSObject rsObject, String... clickActions) {
    if (!rsObject.isOnScreen() || !rsObject.isClickable()) {
      DaxCamera.focus(rsObject);
    }

    return AccurateMouse.hover(rsObject, clickActions);
  }
}
