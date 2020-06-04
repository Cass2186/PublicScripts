package scripts.justj.justwintertodt.gui;

public class Vars {

  private static final Vars instance = new Vars();

  public static Vars getInstance() {
    return instance;
  }

  private String foodName;
  private int foodAmount;
  private boolean guiComplete = false;

  private Vars() {

  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public int getFoodAmount() {
    return foodAmount;
  }

  public void setFoodAmount(int foodAmount) {
    this.foodAmount = foodAmount;
  }

  public boolean isGuiComplete() {
    return guiComplete;
  }

  public void setGuiComplete(boolean guiComplete) {
    this.guiComplete = guiComplete;
  }
}
