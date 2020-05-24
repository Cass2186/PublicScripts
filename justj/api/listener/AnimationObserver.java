package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class AnimationObserver extends Thread {

  private final Set<AnimationListener> listeners;
  private final Supplier<Boolean> shouldNotify;

  private int lastAnimation = -1;

  public AnimationObserver(Supplier<Boolean> shouldNotify) {
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

      int currentAnimation = Player.getAnimation();
      if (shouldNotify.get() && lastAnimation != currentAnimation) {
        listeners.forEach(e -> e.onAnimationChanged(currentAnimation));
      }
      lastAnimation = currentAnimation;
    }
  }

  public void addListener(AnimationListener animationListener) {
    listeners.add(animationListener);
  }
}