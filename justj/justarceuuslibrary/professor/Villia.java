package scripts.justj.justarceuuslibrary.professor;

import org.tribot.api2007.types.RSTile;

public class Villia implements Professor {

  @Override
  public String getName() {
    return "Villia";
  }

  @Override
  public RSTile getPosition() {
    return new RSTile(1625, 3815, 0);
  }
}
