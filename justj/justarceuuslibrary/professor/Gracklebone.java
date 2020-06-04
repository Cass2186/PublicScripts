package scripts.justj.justarceuuslibrary.professor;

import org.tribot.api2007.types.RSTile;

public class Gracklebone implements Professor {

  @Override
  public String getName() {
    return "Professor Gracklebone";
  }

  @Override
  public RSTile getPosition() {
    return new RSTile(1625, 3800, 0);
  }
}
