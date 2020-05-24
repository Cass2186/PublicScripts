package scripts.justj.justcrushchocolate;

public class Statistics {

  private static final Statistics instance = new Statistics();

  public static Statistics getInstance() {
    return instance;
  }

  private int chocolateCrushed = 0;

  public int getChocolateCrushed() {
    return chocolateCrushed;
  }

  public void incrementChocolateCrushed() {
    chocolateCrushed += 1;
  }
}
