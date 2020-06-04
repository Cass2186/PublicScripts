package scripts.justj.api.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Waiting {

  public static boolean waitAfterWalking(BooleanSupplier supplier, long timeout) {
    return waitAfterWalking(supplier, timeout, 50);
  }

  public static boolean waitAfterWalking(BooleanSupplier supplier, long timeout, long checkEvery) {
    ABCUtil.performActions();
    if (waitCondition(Player::isMoving, 1000)) {
      waitCondition(() -> !Player.isMoving(), General.random(8153, 12321));
    }
    return waitWithABC(supplier, timeout, checkEvery);
  }

  public static boolean waitWithABC(BooleanSupplier supplier, long timeout) {
    ABCUtil.performActions();
    return waitCondition(supplier, timeout);
  }

  public static boolean waitWithABC(BooleanSupplier supplier, long timeout, long checkEvery) {
    ABCUtil.performActions();
    return waitCondition(supplier, timeout, checkEvery);
  }

  public static boolean waitCondition(BooleanSupplier supplier, long timeout) {
    return waitCondition(supplier, timeout, 50);
  }

  public static boolean waitCondition(BooleanSupplier supplier, long timeout, long checkEvery) {
    return Timing.waitCondition(() -> {
      General.sleep(checkEvery);
      return supplier.getAsBoolean();
    }, timeout);
  }

  public static <T> Optional<T> waitAfterWalking(Supplier<T> supplier, long timeout) {
    ABCUtil.performActions();
    if (waitCondition(Player::isMoving, 1000)) {
      waitCondition(() -> !Player.isMoving(), General.random(8153, 12321));
    }
    return waitCondition(supplier, timeout);
  }

  public static <T> Optional<T> waitCondition(Supplier<T> supplier, long timeout) {
    if (waitCondition(() -> supplier.get() != null, timeout)) {
      return Optional.ofNullable(supplier.get());
    }
    return Optional.empty();
  }

}
