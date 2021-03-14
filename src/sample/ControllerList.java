package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ControllerList {
    private int id;

    @FXML
    private ImageView image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    private Label labelTask, NameList;

    @FXML
    private Button actionButton, show;

    @FXML
    public void setLabelTask(String s){ //Name for Task
        labelTask.setText(s);
    }

    @FXML
    public void setNameList(String s){
        NameList.setText(s);
    } //Name for the list

    //Action for the name of task
    @FXML
    public void setAction(EventHandler<ActionEvent> e){//It will show the namer of task and the button for add, delete or edit
        actionButton.setOnAction(e);
    }
    @FXML
    public void setId(String s){
        actionButton.setId(s);
    }//Get the id for the action adding, deleting, edit or checked
    @FXML
    public void Opacity(){
        actionButton.setOpacity(0);
    }

    @FXML
    public void setStringB(String s){
        actionButton.setText(s);
    }//
    //END..


    //List Task Party

    //Action for the display of the tasks
    @FXML
    public void setImage(Image i){
        image.setImage(i);
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
