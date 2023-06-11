package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {
    private static double x = 0;
    private static double y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginpage.fxml"));
        primaryStage.setTitle("Amplangkuu");
        primaryStage.setScene(new Scene(root, 250, 300));
        primaryStage.show();
        drag(primaryStage, root);
    }

    public static void drag(Stage stage, Parent root){
        root.setOnMousePressed((MouseEvent event) ->{
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(.7);
        });

        root.setOnMouseReleased((MouseEvent event) ->{
            stage.setOpacity(1);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
