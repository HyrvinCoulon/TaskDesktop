package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerPopUp {

    @FXML Label view1, view2;

    @FXML ImageView exit;

    public void action_exit(MouseEvent mouseEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void setText(String v1, String v2){
        view1.setText(v1);
        view2.setText(v2);
    }

}
