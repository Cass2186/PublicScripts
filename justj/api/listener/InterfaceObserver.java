package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSInterfaceChild;
import scripts.justj.api.listener.model.RSInterfaceChildId;

import java.util.*;
import java.util.function.Supplier;

public class InterfaceObserver extends Thread {

  private final Set<InterfaceListener> listeners;
  private final Map<RSInterfaceChildId, String> rsInterfaceChildMap;
  private final Supplier<Boolean> shouldNotify;

  private RSInterfaceChild lastClickContinue = null;

  public InterfaceObserver(Supplier<Boolean> shouldNotify) {
    this.rsInterfaceChildMap = new HashMap<>();
    this.listeners = new HashSet<>();
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

      for (RSInterfaceChildId rsInterfaceChildId : rsInterfaceChildMap.keySet()) {

        RSInterfaceChild currentInterface = Interfaces.get(rsInterfaceChildId.getIndex(), rsInterfaceChildId.getChild());

        String lastInterfaceText = rsInterfaceChildMap.get(rsInterfaceChildId);
        if (currentInterface != null
            && currentInterface.getText() != null
            && !currentInterface.getText().equals(lastInterfaceText)) {
          listeners.forEach(e -> e.onAppear(currentInterface));
        }
        rsInterfaceChildMap.put(rsInterfaceChildId, Optional.ofNullable(currentInterface).map(RSInterfaceChild::getText).orElse(null));
      }
    }
  }

  public void addListener(InterfaceListener interfaceListener) {
    listeners.add(interfaceListener);
  }

  public void addRSInterfaceChild(int index, int child) {
    rsInterfaceChildMap.put(new RSInterfaceChildId(index, child), null);
  }
}