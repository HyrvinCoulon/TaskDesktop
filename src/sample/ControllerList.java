package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class ControllerList {
    private int id;

    @FXML
    private ImageView image, check;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    private Label labelTask, NameList, hour, title;

    @FXML
    private Button actionButton, show;

    @FXML
    public void setLabelTask(String s){ //Name for Tasks
        labelTask.setText(s);
    }

    @FXML
    public void setNameList(String s){
        NameList.setText(s);
    } //Name for the list

    @FXML public void set(String s, String s1){
        title.setText(s);
        hour.setText(s1);
    }

    //Action for the name of task
    @FXML
    public void setAction(EventHandler<ActionEvent> e){//It will show the namer of task and the button for add, delete or edit
        actionButton.setOnAction(e);
    }

    @FXML
    public void setId(String s){
        actionButton.setId(s);
    }
    //Get the id for the action adding, deleting, edit or checked
    @FXML
    public void Opacity(){
        actionButton.setOpacity(0);
    }

    @FXML public void setButtonId(String s){
        check.setId(check.getId() + " " + s);
    }

    @FXML public void setShow(EventHandler<MouseEvent> e){
        check.setOnMouseClicked(e);
    }

    @FXML
    public void setStringB(String s){
        actionButton.setText(s);
    }//
    //END..


    //List Tasks Party

    //Action for the display of the tasks
    @FXML
    public void setImage(Image i){ check.setImage(i); }

    @FXML
    public void setBImage(Image i){ image.setImage(i); }

    @FXML
    public void setActionButtonId(String s){
        actionButton.setId(actionButton.getId() + " " + s);
    }


    @FXML
    public void setActionShow(EventHandler<ActionEvent> e){
        show.setOnAction(e);
    }//It will work only for task to do.

    @FXML
    public void setIdShow(String s){
        show.setId(s);
    }//Take the index of the list of List of Tasks
    //END..

}
