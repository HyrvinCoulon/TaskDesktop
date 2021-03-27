package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControlModel {
    @FXML private Label labelTask;

    public void setLabelTask(String Task) {
        labelTask.setText(Task);
    }
}
