package scripts.justj.api.paint;

import java.awt.*;
import java.util.Optional;

public interface PaintLine {

  Optional<Image> getLineImage();

  String getLineText();

  void update();

}
