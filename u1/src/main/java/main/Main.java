package main;


import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)throws Exception {
        Controller controller = new Controller();
        Model model = new Model();
        View view = new View();
        Scene scene = new Scene(view);

        //Laden einer Stylesheet zum gestallten der UI.
        scene.getStylesheets().add((getClass().getResource("../style.css")).toExternalForm());

        controller.link(model, view);
        controller.setStage(primaryStage);
        controller.setFileConfig();
        controller.loadMusicFolder();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
