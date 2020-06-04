package scripts.justj.justarceuuslibrary.paint;

import com.google.common.collect.Lists;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.paint.PaintLine;
import scripts.justj.api.paint.PaintTab;

import java.awt.Image;
import java.util.List;
import java.util.Optional;

public class StatsPaintTab implements PaintTab {

  private final List<PaintLine> lines = Lists.newArrayList(
      new LibraryStatePaintLine(),
      new NumberOfResetsPaintLine()
  );

  private final Image image;

  public StatsPaintTab() {
    this.image = Paint.getImage("https://i.imgur.com/8liyCXi.png").orElse(null);
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
