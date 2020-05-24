package scripts.justj.api.paint.common;

import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import scripts.justj.api.paint.PaintLine;

import java.awt.Image;
import java.util.Optional;

public class LevelPaintLine implements PaintLine {

  private final Skills.SKILLS skill;

  private Integer startLevel = 0;
  private Integer currentLevel = 0;

  public LevelPaintLine(Skills.SKILLS skill) {
    this.skill = skill;
  }


  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("Level: %s (Gained: %s)", currentLevel, currentLevel - startLevel);
  }

  @Override
  public void update() {
    if (Login.getLoginState() != Login.STATE.INGAME) {
      return;
    }

    currentLevel = Skills.getActualLevel(skill);
    if (startLevel == 0) {
      startLevel = currentLevel;
    }
  }
}
