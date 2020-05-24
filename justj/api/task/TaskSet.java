package scripts.justj.api.task;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public class TaskSet extends TreeSet<Task> {

  public TaskSet() {
    super(Comparator.comparing(Task::priority).thenComparing(task -> task.getClass().getName()));
  }

  public TaskSet(Task... tasks) {
    this();
    this.addAll(Arrays.asList(tasks));
  }


  public Optional<Task> getNextTask() {
    return this.stream()
        .filter(Task::isValid)
        .findFirst();
  }

  public void addTask(Task task) {
    this.add(task);
  }
}
