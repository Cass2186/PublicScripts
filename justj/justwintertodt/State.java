package scripts.justj.justwintertodt;

import scripts.justj.api.task.Task;

import java.util.Arrays;

public class State {

  private static final State instance = new State();

  public static State getInstance() {
    return instance;
  }

  public String status = "Starting...";

  public Activity currentActivity = Activity.IDLE;
  public Task lastTask;
  public int timer = Integer.MAX_VALUE;

  private State() {
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean currentActivityIn(Activity... activities) {
    return Arrays.asList(activities).contains(currentActivity);
  }

  public boolean wasLastTask(Task task) {
    return lastTask != null && lastTask.equals(task);
  }

  public boolean hasGameStarted() {
    return timer <= 2;
  }
}
