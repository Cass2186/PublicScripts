package scripts.justj.api.paint;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface PaintTab {

  Optional<Image> getTabImage();

  List<PaintLine> getLines();

  default void update() {
    getLines().forEach(PaintLine::update);
  }

}
