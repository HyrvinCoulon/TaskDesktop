package sample;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class NotifStage extends Stage {

    NotifStage(){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to the lower right corner of the visible bounds of the main screen
        this.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 490);
        this.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 110);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/PopUp.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        this.initStyle(StageStyle.TRANSPARENT);
        assert root != null;
        Scene s = new Scene(root);
        s.setFill(Color.TRANSPARENT);
        this.setWidth(480);
        this.setHeight(100);

        this.setScene(s);
        this.show();

    }
}
