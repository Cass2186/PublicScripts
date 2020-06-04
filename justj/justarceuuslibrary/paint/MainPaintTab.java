package scripts.justj.justarceuuslibrary.paint;

import com.google.common.collect.Lists;
import org.tribot.api2007.Skills;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.paint.PaintLine;
import scripts.justj.api.paint.PaintTab;
import scripts.justj.api.paint.TimeRanPaintLine;
import scripts.justj.api.paint.common.LevelPaintLine;
import scripts.justj.api.paint.common.XpGainedPaintLine;

import java.awt.Image;
import java.util.List;
import java.util.Optional;

public class MainPaintTab implements PaintTab {

  private final List<PaintLine> lines = Lists.newArrayList(
      new TimeRanPaintLine(),
      new LevelPaintLine(Skills.SKILLS.RUNECRAFTING),
      new XpGainedPaintLine(Skills.SKILLS.RUNECRAFTING),
      new BooksPaintLine()
  );

  private final Image image;

  public MainPaintTab() {
    this.image = Paint.getImage("https://i.imgur.com/9MY0nEZ.png").orElse(null);
  }

  @Override
  public Optional<Image> getTabImage() {
    return Optional.ofNullable(image);
  }

  @Override
  public List<PaintLine> getLines() {
    return lines;
  }
}
