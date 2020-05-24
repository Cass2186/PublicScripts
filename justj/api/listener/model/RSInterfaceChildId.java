package scripts.justj.api.listener.model;

public class RSInterfaceChildId {

  private final int index;
  private final int child;

  public RSInterfaceChildId(int index, int child) {
    this.index = index;
    this.child = child;
  }

  public int getIndex() {
    return index;
  }

  public int getChild() {
    return child;
  }
}
