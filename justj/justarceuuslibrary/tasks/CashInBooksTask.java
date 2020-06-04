package scripts.justj.justarceuuslibrary.tasks;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;
import scripts.justj.api.task.Priority;
import scripts.justj.api.task.Task;
import scripts.justj.api.utils.Waiting;
import scripts.dax_api.shared.helpers.RSItemHelper;
import scripts.justj.justarceuuslibrary.State;

import static scripts.justj.justarceuuslibrary.Constants.Items.ARCANE_KNOWLEDGE;

public class CashInBooksTask implements Task {
  @Override
  public Priority priority() {
    return Priority.CRITICAL;
  }

  @Override
  public boolean isValid() {
    return Inventory.isFull();
  }

  @Override
  public boolean run() {
    return Inventory.findList(ARCANE_KNOWLEDGE).stream().allMatch(this::cashInBook);
  }


  public boolean cashInBook(RSItem book) {
    State.get().setStatus("Cashing in books");
    int currentXp = Skills.getXP(Skills.SKILLS.RUNECRAFTING);
    return RSItemHelper.click(book, "Read")
        && Waiting.waitCondition(() -> Interfaces.get(219, 1, 2), 2000)
          .map(rsInterface -> rsInterface.click("Continue")).orElse(false)
        && Waiting.waitCondition(() -> Skills.getXP(Skills.SKILLS.RUNECRAFTING) > currentXp, 2000);
  }

}
