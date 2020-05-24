package scripts.justj.api.task;


public interface Task {

  Priority priority();

  boolean isValid();

  boolean run();

}
