package scripts.justj.justcrushchocolate.gui;

import com.allatori.annotations.DoNotRename;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import scripts.justj.api.gui.AbstractGUIController;
import scripts.justj.justcrushchocolate.Constants;
import scripts.justj.justcrushchocolate.Vars;

import java.net.URL;
import java.util.ResourceBundle;

@DoNotRename
public class GUIController extends AbstractGUIController {

  @FXML
  @DoNotRename
  private ComboBox<String> crushingItem;

  @FXML
  @DoNotRename
  private Slider averageWaitTime;

  @FXML
  @DoNotRename
  private Slider sellPrice;

  @FXML
  @DoNotRename
  private Slider buyPrice;

  @FXML
  @DoNotRename
  private Button startScriptButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    crushingItem.getItems().addAll(Constants.Items.KNIFE, Constants.Items.PESTLE_AND_MORTAR);
    crushingItem.getSelectionModel().selectFirst();
  }

  @FXML
  @DoNotRename
  public void startScriptPressed() {
    Vars.getInstance().setCrushItemName(crushingItem.getValue());
    Vars.getInstance().setGeWaitTime((int) averageWaitTime.getValue());
    Vars.getInstance().setSellModifier((int) sellPrice.getValue());
    Vars.getInstance().setBuyModifier((int) buyPrice.getValue());
    this.getGUI().close();
  }
}
