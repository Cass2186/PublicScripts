package scripts.justj.api.paint.common;

import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.paint.PaintLine;

import java.awt.Image;
import java.util.Optional;

public class XpGainedPaintLine implements PaintLine {

  private final Skills.SKILLS skill;

  private final long startTime;
  private long runTime;
  private Integer startXp;
  private Integer xpGained;

  public XpGainedPaintLine(Skills.SKILLS skill) {
    this.skill = skill;
    this.startTime = System.currentTimeMillis();
    update();
  }

  @Override
  public Optional<Image> getLineImage() {
    return Optional.empty();
  }

  @Override
  public String getLineText() {
    return String.format("XP Gained: %s (%s/h)", Paint.numberFormat(xpGained),
        Paint.numberFormat((xpGained * 3600000D / runTime)));
  }

  @Override
  public void update() {
    runTime = System.currentTimeMillis() - startTime;
    if (Login.getLoginState() != Login.STATE.INGAME) {
      return;
    }
    if (startXp == null) {
      startXp = Skills.getXP(skill);
    }
    xpGained = Skills.getXP(skill) - startXp;
  }
}
