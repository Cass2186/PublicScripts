package scripts.justj.justcrushchocolate;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import scripts.justj.api.gui.GUI;
import scripts.justj.api.paint.Paint;
import scripts.justj.api.task.Task;
import scripts.justj.api.task.TaskSet;
import scripts.justj.justcrushchocolate.paint.MainPaintTab;
import scripts.justj.justcrushchocolate.tasks.CloseInterfacesTask;
import scripts.justj.justcrushchocolate.tasks.buying.BuyChocolateTask;
import scripts.justj.justcrushchocolate.tasks.buying.SellDustTask;
import scripts.justj.justcrushchocolate.tasks.making.BankTask;
import scripts.justj.justcrushchocolate.tasks.making.CrushTask;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Optional;


@ScriptManifest(name = "JustCrushChocolate", authors = "JustJ", category = "Money Making",
    description = "Professional Chocolate Crusher", version = 0.1)
public class JustCrushChocolate extends Script implements Painting, MessageListening07 {

  private final TaskSet makingTasks = new TaskSet(
      new BankTask(),
      new CrushTask(),
      new CloseInterfacesTask()
  );


  private final TaskSet sellingTasks = new TaskSet(
      new SellDustTask(this),
      new BuyChocolateTask(this),
      new CloseInterfacesTask()
  );

  private final Paint paint;

  public JustCrushChocolate() {
    super();
    paint = new Paint(() -> State.getInstance().getStatus(), "https://i.imgur.com/hSEAleN.png", new MainPaintTab());
    paint.start();
  }

  @Override
  public void run() {
    setUpGUI();

    while (State.getInstance().isRunning()) {

      Optional<Task> optionalTask;

      if (State.getInstance().isBuying()) {
        optionalTask = sellingTasks.getNextTask();
      } else {
         optionalTask = makingTasks.getNextTask();
      }
      if (optionalTask.isPresent()) {
        optionalTask.get().run();

      } else {
        System.out.println("No task available");
        State.getInstance().setRunning(false);
      }
      sleep(32, 64);
    }

  }

  @Override
  public void serverMessageReceived(String message) {
    if ("You cut the chocolate bar into tiny pieces.".equals(message)) {
      Statistics.getInstance().incrementChocolateCrushed();
    }
  }

  @Override
  public void onPaint(Graphics graphics) {
    if (paint != null) {
      Graphics2D g = (Graphics2D) graphics;
      paint.paint(g);
    }
  }

  //TODO better way then dumping the file in here?
  private void setUpGUI() {

    GUI gui = new GUI("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "\n" +
        "<?import javafx.geometry.Insets?>\n" +
        "<?import javafx.scene.control.Button?>\n" +
        "<?import javafx.scene.control.ComboBox?>\n" +
        "<?import javafx.scene.control.Label?>\n" +
        "<?import javafx.scene.control.Slider?>\n" +
        "<?import javafx.scene.layout.ColumnConstraints?>\n" +
        "<?import javafx.scene.layout.GridPane?>\n" +
        "<?import javafx.scene.layout.HBox?>\n" +
        "<?import javafx.scene.layout.RowConstraints?>\n" +
        "<?import javafx.scene.layout.VBox?>\n" +
        "<?import javafx.scene.text.Font?>\n" +
        "\n" +
        "<GridPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"430.0\" prefWidth=\"400.0\" xmlns=\"http://javafx.com/javafx/11.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller=\"scripts.justj.justcrushchocolate.gui.GUIController\">\n" +
        "  <columnConstraints>\n" +
        "    <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\n" +
        "  </columnConstraints>\n" +
        "  <rowConstraints>\n" +
        "    <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\n" +
        "    <RowConstraints maxHeight=\"155.0\" minHeight=\"10.0\" prefHeight=\"155.0\" vgrow=\"SOMETIMES\" />\n" +
        "    <RowConstraints maxHeight=\"155.0\" minHeight=\"10.0\" prefHeight=\"155.0\" vgrow=\"SOMETIMES\" />\n" +
        "      <RowConstraints maxHeight=\"78.0\" minHeight=\"10.0\" prefHeight=\"11.0\" vgrow=\"SOMETIMES\" />\n" +
        "  </rowConstraints>\n" +
        "   <children>\n" +
        "      <VBox alignment=\"CENTER\" prefHeight=\"400.0\" prefWidth=\"100.0\">\n" +
        "         <children>\n" +
        "            <Label text=\"Just Chocolate Duster\">\n" +
        "               <font>\n" +
        "                  <Font size=\"30.0\" />\n" +
        "               </font>\n" +
        "            </Label>\n" +
        "            <Label text=\"Please start the script at the Grand Exchange\" />\n" +
        "         </children>\n" +
        "      </VBox>\n" +
        "      <VBox fillWidth=\"false\" prefHeight=\"200.0\" prefWidth=\"100.0\" GridPane.rowIndex=\"1\">\n" +
        "         <children>\n" +
        "            <HBox alignment=\"CENTER\" fillHeight=\"false\" prefHeight=\"50.0\" prefWidth=\"359.0\" spacing=\"20.0\">\n" +
        "               <children>\n" +
        "                  <Label alignment=\"BOTTOM_RIGHT\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"25.0\" prefWidth=\"105.0\" text=\"Crushing Item\">\n" +
        "                     <font>\n" +
        "                        <Font size=\"16.0\" />\n" +
        "                     </font>\n" +
        "                  </Label>\n" +
        "                  <ComboBox fx:id=\"crushingItem\" />\n" +
        "               </children>\n" +
        "            </HBox>\n" +
        "            <VBox fillWidth=\"false\">\n" +
        "               <children>\n" +
        "                  <Label minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"25.0\" prefWidth=\"360.0\" text=\"Average wait time between GE Offers (minutes)\">\n" +
        "                     <font>\n" +
        "                        <Font size=\"14.0\" />\n" +
        "                     </font>\n" +
        "                     <VBox.margin>\n" +
        "                        <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" />\n" +
        "                     </VBox.margin>\n" +
        "                  </Label>\n" +
        "                  <Slider fx:id=\"averageWaitTime\" blockIncrement=\"1.0\" majorTickUnit=\"5.0\" max=\"30.0\" min=\"5.0\" minorTickCount=\"5\" prefHeight=\"38.0\" prefWidth=\"351.0\" showTickLabels=\"true\" showTickMarks=\"true\" value=\"10.0\" />\n" +
        "               </children>\n" +
        "               <padding>\n" +
        "                  <Insets right=\"20.0\" />\n" +
        "               </padding>\n" +
        "            </VBox>\n" +
        "         </children>\n" +
        "         <padding>\n" +
        "            <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" />\n" +
        "         </padding>\n" +
        "         <GridPane.margin>\n" +
        "            <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n" +
        "         </GridPane.margin>\n" +
        "      </VBox>\n" +
        "      <VBox prefHeight=\"100.0\" GridPane.rowIndex=\"2\">\n" +
        "         <children>\n" +
        "            <VBox fillWidth=\"false\">\n" +
        "               <children>\n" +
        "                  <Label minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"25.0\" prefWidth=\"341.0\" text=\"What % of the sell price do you want to sell dust for?\">\n" +
        "                     <font>\n" +
        "                        <Font size=\"14.0\" />\n" +
        "                     </font>\n" +
        "                     <VBox.margin>\n" +
        "                        <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" />\n" +
        "                     </VBox.margin>\n" +
        "                  </Label>\n" +
        "                  <Slider fx:id=\"sellPrice\" blockIncrement=\"1.0\" majorTickUnit=\"20.0\" max=\"120.0\" min=\"80.0\" minorTickCount=\"5\" prefHeight=\"38.0\" prefWidth=\"351.0\" showTickLabels=\"true\" showTickMarks=\"true\" value=\"100.0\" />\n" +
        "               </children>\n" +
        "               <padding>\n" +
        "                  <Insets right=\"20.0\" />\n" +
        "               </padding>\n" +
        "            </VBox>\n" +
        "            <VBox fillWidth=\"false\">\n" +
        "               <children>\n" +
        "                  <Label minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"25.0\" prefWidth=\"341.0\" text=\"What % of the buy price do you want to buy bars for?\">\n" +
        "                     <font>\n" +
        "                        <Font size=\"14.0\" />\n" +
        "                     </font>\n" +
        "                     <VBox.margin>\n" +
        "                        <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" />\n" +
        "                     </VBox.margin>\n" +
        "                  </Label>\n" +
        "                  <Slider fx:id=\"buyPrice\" blockIncrement=\"1.0\" majorTickUnit=\"20.0\" max=\"120.0\" min=\"80.0\" minorTickCount=\"5\" prefHeight=\"38.0\" prefWidth=\"351.0\" showTickLabels=\"true\" showTickMarks=\"true\" value=\"100.0\" />\n" +
        "               </children>\n" +
        "               <padding>\n" +
        "                  <Insets right=\"20.0\" />\n" +
        "               </padding>\n" +
        "            </VBox>\n" +
        "         </children>\n" +
        "         <padding>\n" +
        "            <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" />\n" +
        "         </padding>\n" +
        "         <GridPane.margin>\n" +
        "            <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n" +
        "         </GridPane.margin>\n" +
        "      </VBox>\n" +
        "      <Button fx:id=\"startScriptButton\" alignment=\"CENTER\" contentDisplay=\"CENTER\" mnemonicParsing=\"false\" onAction=\"#startScriptPressed\" text=\"Start Script\" GridPane.halignment=\"CENTER\" GridPane.rowIndex=\"3\" GridPane.valignment=\"CENTER\">\n" +
        "         <font>\n" +
        "            <Font size=\"18.0\" />\n" +
        "         </font>\n" +
        "         <GridPane.margin>\n" +
        "            <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n" +
        "         </GridPane.margin>\n" +
        "      </Button>\n" +
        "   </children>\n" +
        "</GridPane>\n");
    gui.show();
    while (gui.isOpen()) {
      sleep(500);
    }

  }
}
