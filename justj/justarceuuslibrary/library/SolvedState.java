package scripts.justj.justarceuuslibrary.library;

public enum SolvedState {

  NO_DATA("No data"),
  INCOMPLETE("Incomplete"),
  COMPLETE("Complete");

  private final String name;

  SolvedState(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
