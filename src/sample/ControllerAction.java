package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerAction {


    @FXML
    private Label title;

    @FXML
    private Button editTitle, editTasks;

    @FXML
    private ImageView i1, i2;


    public void setImage(Image im1, Image im2){
        i1.setImage(im1);
        i2.setImage(im2);
    }

    public void setText(String s, String ss){
        editTitle.setText(s);
        editTasks.setText(ss);
    }

    public void setTitle(String s){
        title.setText(s);
    }

    public void setId(String s){
        title.setId("t " + s);
        editTitle.setId("el "+ s);
        editTasks.setId("eT "+ s);
    }

    public void setDelete(String s) {
        editTitle.setId("d "+s);
        editTasks.setId("dT "+s);
    }

    public void setAction(EventHandler<ActionEvent> e){
        editTitle.setOnAction(e);
        editTasks.setOnAction(e);
    }
}
