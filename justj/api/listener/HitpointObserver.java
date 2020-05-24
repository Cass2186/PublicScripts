package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class HitpointObserver extends Thread {

  private final Set<HitpointListener> listeners;
  private final Supplier<Boolean> shouldNotify;

  private int lastHealth;

  public HitpointObserver(Supplier<Boolean> shouldNotify) {
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

      int currentHealth = Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS);

      if (shouldNotify.get() && lastHealth != currentHealth) {
        listeners.forEach(e -> e.onHitpointsChanged(currentHealth));
      }
      lastHealth = currentHealth;
    }
  }

  public void addListener(HitpointListener hitpointListener) {
    listeners.add(hitpointListener);
  }
}