package rocks.wallenius.joop.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rocks.wallenius.joop.controller.Controller;
import rocks.wallenius.joop.model.Model;
import rocks.wallenius.joop.view.View;

/**
 *
 * Main Class
 *
 * Created by philipwallenius on 12/02/16.
 */
public class JoopApplication extends Application {

    private final static String WINDOW_TITLE = "JOOP";
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(WINDOW_TITLE);
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("css/default_theme.css");
        scene.getStylesheets().add("css/java-keywords.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        Model model = new Model();
        View view = fxmlLoader.getController();
        Controller controller = new Controller(model, view);
        view.setController(controller);
    }

    @Override
    public void stop() {
        View controller = fxmlLoader.getController();
        controller.stop();
    }

}
