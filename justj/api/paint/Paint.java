package scripts.justj.api.paint;

import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSInterface;
import org.tribot.script.interfaces.EventBlockingOverride;
import scripts.justj.api.JustLogger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class Paint extends Thread {

  private static final JustLogger LOGGER = new JustLogger(Paint.class);

  private static final Font FONT = new Font("SansSerif", Font.PLAIN, 12);

  public static String numberFormat(double num) {
    if (num < 1000.0) {
      return Integer.toString((int) num);
    } else if (Math.round(num) / 10000.0 < 100.0) {
      return String.format("%.1fk", Math.round(num) / 1000.0);
    } else {
      return String.format("%.1fm", Math.round(num) / 1000000.0);
    }
  }

  public static Optional<Image> getImage(String url) {
    try {
      URL u = new URL(url);
      return Optional.of(ImageIO.read(u.openStream()));
    } catch(IOException e) {
      LOGGER.error("Unable to get paint image");
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public static Point getChatBoxPoint() {
    RSInterface chatBox = Interfaces.get(162, 37);
    if (chatBox != null) {
      Rectangle bounds = chatBox.getAbsoluteBounds();
      if (bounds != null) {
        return new Point(bounds.x, bounds.y - 110);
      }
    }
    return new Point(0, 228 - 110);
  }

  private final Image image;
  private final List<PaintTab> tabs;
  private final Supplier<String> getStatusFunction;

  private boolean isRunning;
  private Point p;
  private PaintTab openTab;
  private Map<PaintTab, Rectangle> tabRectangles;

  private String status;

  public Paint(Supplier<String> getStatusFunction, String paintUrl, PaintTab... paintTabs) {
    super("Script Painter Thread");
    image = getImage(paintUrl).orElse(null);
    this.getStatusFunction = getStatusFunction;
    tabs = Arrays.asList(paintTabs);
    openTab = tabs.get(0);
    status = "Starting..";
  }

  @Override
  public void run() {
    isRunning = true;
    while (isRunning) {
      General.sleep(1000);
      update();
    }
  }


  public void paint(Graphics g) {
    if (p == null) {
      if (Login.getLoginState() == Login.STATE.INGAME) {
        p = getChatBoxPoint();
        createTabRectangles();
      } else {
        return;
      }
    }

    if (image != null) {
      g.drawImage(image, p.x + 1, p.y, null);
      g.setFont(FONT.deriveFont(Font.BOLD));

      int startX = p.x+40;
      int startY = p.y+46;
      g.drawString(status, startX, startY);

      g.setFont(FONT);
      for (PaintLine line : openTab.getLines()) {
        startY += 14;
        g.drawString(line.getLineText(), startX, startY);
      }

      startX = p.x+6;
      startY = p.y+32;

      for (PaintTab tab : tabs) {
        if (tab.getTabImage().isPresent()) {
          if (tab == openTab) {
            g.setColor(Color.WHITE);
            g.drawRoundRect(startX-2, startY-2, 26, 24, 3, 3);
          }
          g.drawImage(tab.getTabImage().get(), startX, startY, null);
        }
        startY += 26;
      }

    }
  }

  public EventBlockingOverride.OVERRIDE_RETURN processEvent(MouseEvent e) {
    if (e.getID() != MouseEvent.MOUSE_CLICKED && e.getID() != MouseEvent.MOUSE_PRESSED) {
      return EventBlockingOverride.OVERRIDE_RETURN.PROCESS;
    }

    return tabRectangles.entrySet().stream()
        .filter(entry -> entry.getValue().contains(e.getPoint()))
        .map(Map.Entry::getKey)
        .map(clickedTab -> {
          openTab = clickedTab;
          return EventBlockingOverride.OVERRIDE_RETURN.DISMISS;
        })
        .findAny()
        .orElse(EventBlockingOverride.OVERRIDE_RETURN.PROCESS);
  }


  private void createTabRectangles() {
    tabRectangles = new HashMap<>();
    int startX = p.x+6;
    int startY = p.y+32;
    for (PaintTab tab : tabs) {
      if (tab.getTabImage().isPresent()) {
        tabRectangles.put(tab, new Rectangle(startX-2, startY-2, 26, 24));
        startY += 26;
      }
    }
  }

  private void update() {
    status = getStatusFunction.get();
    tabs.forEach(PaintTab::update);
  }

}
