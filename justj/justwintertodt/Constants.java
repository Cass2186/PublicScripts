package scripts.justj.justwintertodt;

import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.types.RSVarBit;

public class Constants {

  public static class Animations {

    public static final int IDLE = -1;
    public static final int WOODCUTTING_BRONZE = 879;
    public static final int WOODCUTTING_IRON = 877;
    public static final int WOODCUTTING_STEEL = 875;
    public static final int WOODCUTTING_BLACK = 873;
    public static final int WOODCUTTING_MITHRIL = 871;
    public static final int WOODCUTTING_ADAMANT = 869;
    public static final int WOODCUTTING_RUNE = 867;
    public static final int WOODCUTTING_GILDED = 8303;
    public static final int WOODCUTTING_DRAGON = 2846;
    public static final int WOODCUTTING_INFERNAL = 2117;
    public static final int WOODCUTTING_3A_AXE = 7264;
    public static final int WOODCUTTING_CRYSTAL = 8324;
    public static final int FLETCHING_BOW_CUTTING = 1248;
    public static final int LOOKING_INTO = 832;

  }

  public static class Items {

    public static final String BURMA_ROOT = "Bruma root";

  }


  public static class NPCs {

    public static final String INCAPACITATED_PYROMANCER = "Incapacitated pyromancer";

  }

  public static class Tiles {

    // Wintertodt
    public static final RSTile WINTERTODT_BRAZIER_TILE = new RSTile(1638, 3997, 0);
    public static final RSTile WINTERTODT_STAND_BRAZIER_TILE = new RSTile(1638, 3996, 0);
    public static final RSTile WINTERTODT_CHOP_TILE = new RSTile(1638, 3988, 0);
    public static final RSTile WINTERTODT_PYRO_TILE = new RSTile(1641, 3996, 0);
    public static final RSTile WINTERTODT_BURMA_ROOTS_TILE = new RSTile(1639, 3988, 0);
    public static final RSTile WINTERTODT_DOOR_TILE = new RSTile(1631, 3967, 0);
    public static final RSTile WINTERTODT_IDLE_TILE = new RSTile(1630, 3982, 0);
    public static final RSTile WINTERTODT_BANK_TILE = new RSTile(1641, 3944, 0);
  }

  public static class VarBits {

    public static final RSVarBit WINTERTODT_TIMER = RSVarBit.get(7980);
  }

  public static class Paths {

    public static final RSTile[] FROM_BANK_TO_BRAZIER = new RSTile[] {
        new RSTile(1640, 3944, 0),
        new RSTile(1639, 3944, 0),
        new RSTile(1638, 3944, 0),
        new RSTile(1637, 3944, 0),
        new RSTile(1636, 3945, 0),
        new RSTile(1635, 3946, 0),
        new RSTile(1634, 3947, 0),
        new RSTile(1633, 3948, 0),
        new RSTile(1633, 3949, 0),
        new RSTile(1633, 3950, 0),
        new RSTile(1632, 3951, 0),
        new RSTile(1631, 3952, 0),
        new RSTile(1630, 3953, 0),
        new RSTile(1630, 3954, 0),
        new RSTile(1630, 3955, 0),
        new RSTile(1630, 3956, 0),
        new RSTile(1630, 3957, 0),
        new RSTile(1630, 3958, 0),
        new RSTile(1630, 3959, 0),
        new RSTile(1630, 3960, 0),
        new RSTile(1630, 3961, 0),
        new RSTile(1630, 3962, 0),
        new RSTile(1630, 3963, 0),
        new RSTile(1630, 3968, 0),
        new RSTile(1630, 3969, 0),
        new RSTile(1630, 3970, 0),
        new RSTile(1630, 3971, 0),
        new RSTile(1630, 3972, 0),
        new RSTile(1630, 3973, 0),
        new RSTile(1630, 3974, 0),
        new RSTile(1630, 3975, 0),
        new RSTile(1630, 3976, 0),
        new RSTile(1630, 3977, 0),
        new RSTile(1630, 3978, 0),
        new RSTile(1630, 3979, 0),
        new RSTile(1630, 3980, 0),
        new RSTile(1630, 3981, 0),
        new RSTile(1630, 3982, 0),
        new RSTile(1630, 3983, 0),
        new RSTile(1631, 3984, 0),
        new RSTile(1632, 3985, 0),
        new RSTile(1632, 3986, 0),
        new RSTile(1632, 3987, 0),
        new RSTile(1632, 3988, 0),
        new RSTile(1633, 3989, 0),
        new RSTile(1634, 3990, 0),
        new RSTile(1635, 3991, 0),
        new RSTile(1636, 3992, 0),
        new RSTile(1637, 3993, 0),
        new RSTile(1638, 3994, 0),
        new RSTile(1638, 3995, 0),
        new RSTile(1638, 3996, 0)
    };


  }
}
