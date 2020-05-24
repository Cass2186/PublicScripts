package scripts.justj.justcrushchocolate;

import scripts.justj.api.JustLogger;

public class State {

  private static final JustLogger LOGGER = new JustLogger(State.class);

  private static final State instance = new State();

  public static State getInstance() {
    return instance;
  }

  private boolean running = true;

  private String status = "Starting...";

  private boolean isBuying = false;

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    if (!status.equals(this.status)) {
      LOGGER.info(status);
      this.status = status;
    }
  }

  public boolean isBuying() {
    return isBuying;
  }

  public void setBuying(boolean buying) {
    isBuying = buying;
  }
}
