package sample;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeIn;
import animatefx.animation.ZoomOut;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class NotifStage extends Stage {

    Parent root;

    NotifStage(String ss, String word){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to the lower right corner of the visible bounds of the main screen
        this.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 490);
        this.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 110);
        root = null;

        FXMLLoader f = new FXMLLoader();
        f.setLocation(Main.class.getResource("/view/PopUp.fxml"));
        try {
            root = f.load();
        }catch (IOException e){
            e.printStackTrace();
        }

        ControllerPopUp c = f.getController();
        c.setText(ss, word);
        this.initStyle(StageStyle.TRANSPARENT);
        assert root != null;
        Scene s = new Scene(root);

        s.setFill(Color.TRANSPARENT);
        this.setWidth(480);
        this.setHeight(100);

        this.setScene(s);
        this.show();



        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        pause.setOnFinished(event -> {
            AnimationFX fx = new ZoomOut(root);
            fx.setOnFinished(actionEvent -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            });
            fx.play();
            //this.close();
        });
        pause.play();

    }
}
