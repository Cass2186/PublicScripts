package scripts.justj.api;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;

public class Constants {

  public static class GameSettings {

    public static final int NOTED_SETTING = 115;

  }

  public static class Objects {

    public static final String GRAND_EXCHANGE_BOOTH = "Grand Exchange booth";

  }

  public enum RSInterfaces {

    //Bank

    WITHDRAW_AS_ITEM(12, 21),
    WITHDRAW_AS_NOTE(12, 23),

    //GE
    COLLECT_ALL(465, 6, 0),

    ;


    private final Integer index;
    private final Integer child;
    private final Integer component;


    RSInterfaces(Integer index, Integer child, Integer component) {
      this.index = index;
      this.child = child;
      this.component = component;
    }

    RSInterfaces(Integer index, Integer child) {
      this.index = index;
      this.child = child;
      this.component = null;
    }

    RSInterfaces(Integer index) {
      this.index = index;
      this.child = null;
      this.component = null;
    }

    public RSInterface get() {
      if (component == null) {
        if (child == null) {
          return Interfaces.get(index);
        }
        return Interfaces.get(index, child);
      }
      return Interfaces.get(index, child, component);
    }
  }


}
