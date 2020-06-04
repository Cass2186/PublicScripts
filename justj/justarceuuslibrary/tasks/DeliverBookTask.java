package scripts.justj.justarceuuslibrary.tasks;

import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.JustLogger;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justarceuuslibrary.LibraryUtils;
import scripts.justj.justarceuuslibrary.State;

public class DeliverBookTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(DeliverBookTask.class);

  @Override
  public Priority priority() {
    return Priority.MEDIUM;
  }

  @Override
  public boolean isValid() {
    return State.get().getCurrentAssignment().isPresent()
        && State.get().getCurrentBooks().contains(State.get().getCurrentAssignment().get());
  }

  @Override
  public boolean run() {
    LOGGER.info("Delivering task");
    State.get().setStatus("Delivering book");
    return LibraryUtils.helpProfessor(State.get().getCurrentProfessor())
        && Waiting.waitAfterWalking(() -> !State.get().getCurrentAssignment().isPresent(), 1000);
  }
}
