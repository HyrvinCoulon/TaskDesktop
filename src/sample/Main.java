package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;

public class Main extends Application {

    private double x = 0, y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass().getResource("/view/sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        Scene s = new Scene(root, 764, 480);
        primaryStage.setScene(s);
        primaryStage.setTitle("Tasked");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        

        s.setFill(Color.TRANSPARENT);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}
