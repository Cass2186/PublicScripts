package scripts.justj.api.listener;

public interface MuleListener {
  void onMuleNearby(String muleName);

  void onMuleLeave(String muleName);
}
