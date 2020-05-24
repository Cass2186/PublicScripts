package scripts.justj.justcrushchocolate;

public class Vars {

  private static final Vars instance = new Vars();

  public static Vars getInstance() {
    return instance;
  }

  private String crushItemName = Constants.Items.KNIFE;

  private int buyModifier = 100;
  private int sellModifier = 100;

  private int geWaitTime = 10;

  public String getCrushItemName() {
    return crushItemName;
  }

  public void setCrushItemName(String crushItemName) {
    this.crushItemName = crushItemName;
  }

  public int getBuyModifier() {
    return buyModifier;
  }

  public void setBuyModifier(int buyModifier) {
    this.buyModifier = buyModifier;
  }

  public int getSellModifier() {
    return sellModifier;
  }

  public void setSellModifier(int sellModifier) {
    this.sellModifier = sellModifier;
  }

  public int getGeWaitTime() {
    return geWaitTime;
  }

  public void setGeWaitTime(int geWaitTime) {
    this.geWaitTime = geWaitTime;
  }
}
