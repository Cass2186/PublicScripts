package scripts.justj.api.utils;

import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSTile;
import scripts.justj.api.utils.ABCUtil;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.WalkerEngine;
import scripts.dax_api.walker_engine.WalkingCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WalkingHandler {

  public static boolean walkPath(RSTile[] path) {

    ArrayList<RSTile> p = Arrays.stream(Walking.randomizePath(path, 0, 0))
        .collect(Collectors.toCollection(ArrayList::new));

    return WalkerEngine.getInstance().walkPath(p, () -> {
      ABCUtil.performRunActivation();
      return WalkingCondition.State.CONTINUE_WALKER;
    }) || DaxWalker.walkTo(p.get(p.size()-1));
  }

}
