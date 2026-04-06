import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX GUI application with a menu bar containing four menu options:
 * 1. Show Date/Time - displays current date and time in text area
 * 2. Save to Log - writes text area contents to log.txt
 * 3. Change Color - changes background to a random shade of green generated at startup
 * 4. Exit - exits application
 */
public class MenuGUI extends Application {
    private TextArea textArea;
    private BorderPane root;
    private final Random rng = new Random();

    // Class-level field — generated once at startup
    private final Color randomGreen = generateRandomGreen();

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        /*
         * Text Area
         */
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(400);
        textArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 13px;");
        root.setCenter(textArea);

        /*
         * Menu
         */
        Menu menu = new Menu("Options");

        MenuItem showDateTime = new MenuItem("1. Show Date and Time");
        MenuItem saveToLog = new MenuItem("2. Save to Log");
        MenuItem changeColor = new MenuItem("3. Change Background Color");
        MenuItem exitApp = new MenuItem("4. Exit");

        menu.getItems().addAll(showDateTime, saveToLog, changeColor, exitApp);

        MenuBar menuBar = new MenuBar(menu);
        root.setTop(menuBar);

        /*
         * Event Handlers
         */
        // Menu Option 1 - Show Date and Time
        showDateTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy  hh:mm:ss a");
                String dateTime = LocalDateTime.now().format(formatter);
                textArea.appendText("Date/Time: " + dateTime + "\n");
            }
        });

        // Menu Option 2 - Save to Log
        saveToLog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String content = textArea.getText();
                try (FileWriter writer = new FileWriter("log.txt", true)) {
                    writer.write(content);
                    writer.write("\n--- saved " +
                            LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + " ---\n");
                    textArea.appendText("[System] Contents saved to log.txt\n");
                } catch (IOException e) {
                    textArea.appendText("[Error] Could not write to log.txt: "
                            + e.getMessage() + "\n");
                }
            }
        });

        // Menu Option 3 - Change Background Color
        changeColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.setBackground(new Background(
                        new BackgroundFill(randomGreen, CornerRadii.EMPTY, Insets.EMPTY)));

                String hex = String.format("#%02X%02X%02X",
                        (int) (randomGreen.getRed() * 255),
                        (int) (randomGreen.getGreen() * 255),
                        (int) (randomGreen.getBlue() * 255));
                textArea.appendText("[System] Background changed to green hue " + hex + "\n");
            }
        });

        // Menu Option 4 - Exit
        exitApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        /*
         * Scene
         */
        Scene scene = new Scene(root, 600, 460);
        primaryStage.setTitle("Menu GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Generates a new random shade of green on each call.
     */
    private Color generateRandomGreen() {
        int green = 128 + rng.nextInt(128);
        int red = rng.nextInt(green / 2);
        int blue = rng.nextInt(green / 2);
        return Color.rgb(red, green, blue);
    }

    public static void main(String[] args) {
        launch(args);
    }
}