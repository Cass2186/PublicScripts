package scripts.justj.justarceuuslibrary.tasks;

import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.JustLogger;
import scripts.justj.api.utils.Waiting;
import scripts.justj.justarceuuslibrary.LibraryUtils;
import scripts.justj.justarceuuslibrary.State;
import scripts.justj.justarceuuslibrary.professor.Professor;

public class GetNewAssignmentTask implements Task {

  private static final JustLogger LOGGER = new JustLogger(GetNewAssignmentTask.class);

  @Override
  public Priority priority() {
    return Priority.MEDIUM;
  }

  @Override
  public boolean isValid() {
    return !State.get().getCurrentAssignment().isPresent();
  }

  @Override
  public boolean run() {

    State.get().setStatus("Getting new task");
    LOGGER.info("Getting new task");
    Professor nextProfessor = State.get().getCurrentProfessor();

    return LibraryUtils.helpProfessor(nextProfessor)
        && Waiting.waitCondition(() -> State.get().getCurrentAssignment().isPresent()
          || nextProfessor != State.get().getCurrentProfessor(), 7000);
  }
}
