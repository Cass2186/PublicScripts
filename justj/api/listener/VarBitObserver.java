package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSVarBit;

import java.util.*;
import java.util.function.Supplier;

public class VarBitObserver extends Thread {

  private final Set<VarBitListener> listeners;
  private final Set<RSVarBit> varbits;
  private final Supplier<Boolean> shouldNotify;

  private final Map<RSVarBit, Integer> varBitValues;

  public VarBitObserver(Supplier<Boolean> shouldNotify) {
    this.listeners = new HashSet<>();
    this.varbits = new HashSet<>();
    this.varBitValues = new HashMap<>();
    this.shouldNotify = shouldNotify;
  }

  @Override
  public void run() {
    while (Login.getLoginState() != Login.STATE.INGAME) {
      General.sleep(500);
    }

    while (true) {
      General.sleep(100);
      if (Login.getLoginState() != Login.STATE.INGAME) continue;

      varbits.forEach(varBit -> {
        int currentValue = varBit.getValue();
        if (shouldNotify.get() && varBitValues.getOrDefault(varBit, -99) != currentValue) {
          listeners.forEach(e -> e.onVarbitChanged(varBit, currentValue));
        }
        varBitValues.put(varBit, currentValue);
      });
    }
  }

  public void addVarbit(RSVarBit rsVarBit) {
    varbits.add(rsVarBit);
  }

  public void addListener(VarBitListener varBitListener) {
    listeners.add(varBitListener);
  }
}