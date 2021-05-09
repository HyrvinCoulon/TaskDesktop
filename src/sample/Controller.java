package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import org.jetbrains.annotations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements EventHandler<ActionEvent>, Initializable {

    ArrayList<Tasks> s = new ArrayList<>();
    ArrayList<Tasks> ss = new ArrayList<>();
    static HashMap<String, ArrayList<Tasks>> list;


    int lindex, displayTracker = 0;
    private Boolean add = false, delete, edit = false;

    static String eIndex;

    @FXML
    ScrollPane scrollpane;

    @FXML
    AnchorPane feedpane;

    @FXML
    private Button ListButton, DeleteButton, feed, reduce,AddButton, EditButton, bAdd, aList, back, sendfeed;

    @FXML
    private TextField namer, emailadd;

    @FXML
    private Label titleTaskList;

    @FXML private TextArea feedcomment;

    @FXML
    private VBox boxTask;

    @FXML
    public void closeWindow(MouseEvent m){
        System.exit(0);
    }

    @FXML
    public void reduceWindow(MouseEvent m){
        Stage c = (Stage) reduce.getScene().getWindow();
        c.setIconified(true);
    }

    @FXML
    public void handleBack(ActionEvent e) throws IOException {
        switch (displayTracker){
            case 1:
                hide();
                boxTask.getChildren().clear();
                if(edit) {
                    initEdit();
                    displayTracker--;
                }
                if(delete){
                    initDelete();
                    displayTracker--;
                }
                if(add){
                    namer.setPromptText("Entrez le titre de la liste");
                    init();
                    displayTracker--;
                    add = false;
                }
                back.setOpacity(0);
                break;
            case 2:
                hide();
                if(edit) {
                    boxTask.getChildren().clear();
                    int i = 0;
                    for (Tasks sT : list.get(eIndex))
                        boxTask.getChildren().add(initEditTask(sT.getTitle(), String.valueOf(i++)));
                    displayTracker--;
                }
                if(delete){
                    boxTask.getChildren().clear();
                    int i = 0;
                    for (Tasks sT : list.get(eIndex))
                        boxTask.getChildren().add(initDeleteTask(sT.getTitle(), String.valueOf(i++)));
                    displayTracker--;
                }
                if(add){
                    add = false;
                    namer.setPromptText("Entrez le titre de la liste");
                    back.setOpacity(0);
                    boxTask.getChildren().clear();
                    init();
                    displayTracker -= 2;
                }
                break;
        }
    }

    @FXML
    public void handles(ActionEvent e) throws IOException { //Action doing by the main buttons List, Add, Delete and Edit

        checker();
        mainpart();
        if(e.getSource() == ListButton){ // Show the List of List
            hide();
            boxTask.getChildren().clear();
            for(String as : list.keySet()){
                ControllerList cL = new ControllerList();
                FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ListTask.fxml"));
                f.setController(cL);
                Node n = f.load();
                cL.setIdShow(as);
                cL.setNameList(as);
                cL.setActionShow(this);
                boxTask.getChildren().add(n);
            }
        }else if(e.getSource() == AddButton){ // Show The List of the list with the add functions
            hide();
            delete = edit = false;
            boxTask.getChildren().clear();
            init();
        }else if(e.getSource() == bAdd){ // Depend on which boolean is set

            if(add && displayTracker == 2){
                boxTask.getChildren().add(initAdd(namer.getText()));
                list.get(eIndex).add(new Tasks(namer.getText()));
                namer.setText("");
                save(list);
            }else if(add) { // Will add tasks

                ss.add(new Tasks(namer.getText()));
                add(namer.getText());
                namer.setText("");
                save(list);

            }else if(edit && displayTracker == 2){ // Will modify Title of Tasks

                list.get(eIndex).set(lindex, new Tasks(namer.getText()));
                namer.setText("");
                save(list);

            }else if(edit){// Will modify Title of list
                s = list.get(eIndex);
                list.remove(eIndex);
                list.put(namer.getText().replace(" ", "_"), s);
                namer.setText("");
                save(list);

            }else if(delete && displayTracker == 2){ // delete Tasks
                list.get(eIndex).remove(lindex);
                save(list);

            }else if(delete){ // Delete List
                list.remove(eIndex);
                save(list);
            }else{

                if(namer.getPromptText().equals("Entrez le titre de la liste")) {
                    titleTaskList.setText(namer.getText());
                    namer.setText("");
                    namer.setPromptText("Entrer les différentes taches :");
                    add = true;
                }
            }
        }else if(e.getSource() == aList){ // Will add the new List to The super List

            //System.out.println(ss);

            list.put(titleTaskList.getText().replace(" ", "_"), ss);
            System.out.println(list);

            titleTaskList.setText("");

            save(list);

            list = retrieveJ();

            ss = new ArrayList<>();
        }else if(e.getSource() == EditButton){// Show the display for modification of the list
            hide();
            boxTask.getChildren().clear();
            delete = add = false;
            edit = true;
            initEdit();
        }else if(e.getSource() == DeleteButton){ // Part Of Deletion
            hide();
            edit = add = false;
            delete = true;
            boxTask.getChildren().clear();
            initDelete();
        }else if(e.getSource() == feed){
            feedpart();
        }

    }

    private void feedpart(){
        scrollpane.setOpacity(0);
        feedpane.toFront();
        feedpane.setOpacity(1);
    }

    private void mainpart(){
        feedpane.setOpacity(0);
        scrollpane.toFront();
        scrollpane.setOpacity(1);
    }

    private void initEdit() throws IOException {
            for(String as : list.keySet()){
                ControllerAction c = new ControllerAction();
                FXMLLoader fl = new FXMLLoader(getClass().getResource("/view/ModelAction2.fxml"));
                fl.setController(c);
                Node nl = fl.load();
                c.setTitle(as);
                c.setAction(this);
                c.setId(as);
                boxTask.getChildren().add(nl);
            }
    }

    private void initDelete() throws IOException {
        int i = 0;
        for(String as : list.keySet()){
            ControllerAction c = new ControllerAction();
            FXMLLoader fl = new FXMLLoader(getClass().getResource("/view/ModelAction2.fxml"));
            fl.setController(c);
            Node nl = fl.load();
            c.setTitle(as);
            c.setAction(this);
            c.setText("Delete List", "Delete Tasks");
            c.setImage(new Image(String.valueOf(getClass().getResource("/images/delete_menu.png"))), new Image(String.valueOf(getClass().getResource("/images/delete_tasks.png"))));
            c.setDelete(as);
            boxTask.getChildren().add(nl);
        }
    }



    private void save(HashMap<String, ArrayList<Tasks>> l) throws IOException {
        Access.saveString(l);
    }

    private HashMap<String, ArrayList<Tasks>> retrieveJ() throws FileNotFoundException {
        return  Access.retrieveH();
    }

    private void init() throws IOException { //Initialisation of add part
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);
        Node n = f.load();
        cL.setId("addList");
        cL.setAction(this);
        boxTask.getChildren().add(n);
        int i = 0;
        if(list != null){
            for(String as : list.keySet()){
                ControllerList c = new ControllerList();
                FXMLLoader fl = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
                fl.setController(c);
                Node nl = fl.load();
                c.setLabelTask(as);
                c.setId("addTask " + as);
                c.setAction(this);
                boxTask.getChildren().add(nl);
            }
        }
    }

    private Node intiTask(String s){ //Init Tasks for ListTask Section
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);
        Node n = null;
        try {
            n = f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cL.setLabelTask(s);
        cL.setActionButtonId(s);

        if(list.get(eIndex).get(find(s)).isDone()) {
            cL.setImage(new Image(String.valueOf(getClass().getResource("/images/checked.png"))));
            cL.setStringB("Terminée");
        }else{
            cL.setImage(new Image(String.valueOf(getClass().getResource("/images/progress.png"))));
            cL.setStringB("En cours ..");
        }

        cL.setAction(actionEvent1 -> {
            Button bn = (Button) actionEvent1.getSource();
            if(bn.getId().split(" ")[0].equals("actionButton")){
                if(!list.get(eIndex).get(find(s)).isDone()) {
                    cL.setImage(new Image(String.valueOf(getClass().getResource("/images/checked.png"))));
                    cL.setStringB("Terminée");
                    list.get(eIndex).get(find(s)).setDone(true);
                }else{
                    cL.setImage(new Image(String.valueOf(getClass().getResource("/images/progress.png"))));
                    cL.setStringB("En cours ..");
                    list.get(eIndex).get(find(s)).setDone(false);
                }
                try {
                    save(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return n;
    }

    private Node initAdd(String as){
        ControllerList c = new ControllerList();
        FXMLLoader fl = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        fl.setController(c);

        Node nl = null;
        try {
            nl = fl.load();
        }catch(IOException e){
            e.printStackTrace();
        }
        c.setLabelTask(as);
        c.Opacity();

        return nl;
    }

    private Node initAddTask(String st){

        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelTask.fxml"));

        Node nl = null;

        try{
            nl = f.load();
        }catch(IOException e){
            e.printStackTrace();
        }
        ControlModel c = f.getController();
        c.setLabelTask(st);

        return nl;
    }

    private Node initEditTask(String sL, String sI) {
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);

        Node n = null;
        try {
            n = f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cL.setLabelTask(sL);
        cL.setId("e "+sI);
        cL.setStringB("edit");
        cL.setImage(new Image(String.valueOf(getClass().getResource("/images/edit_tasks.png"))));
        cL.setAction(this);

        return n;
    }

    private Node initDeleteTask(String sL, String sI) {
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);

        Node n = null;
        try {
            n = f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cL.setLabelTask(sL);
        cL.setId("dd "+sI);
        cL.setStringB("delete");
        cL.setImage(new Image(String.valueOf(getClass().getResource("/images/delete_tasks.png"))));
        cL.setAction(this);

        return n;
    }

    private void add(String s) throws IOException {// Adding Tasks on the process of creation of list
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);
        Node n = f.load();
        cL.setLabelTask(s);
        cL.setId("addTask");
        boxTask.getChildren().add(n);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button b = (Button) actionEvent.getSource();
        String sl = b.getId();

        System.out.println(sl);

        String ta = null, te = null, dT = null, dd = null, dTs = null, aT = null, el = null;
        switch (b.getId().split(" ")[0]) {
            case "e":
                //System.out.println(b.getId().split(" ")[0]);
                ta = Access.ActionId(b.getId().split(" "));
                break;
            case "el":
                //System.out.println(b.getId().split(" ")[0]);
                el = Access.ActionId(b.getId().split(" "));
                break;
            case "d":
                dT = Access.ActionId(b.getId().split(" "));
                break;
            case "dd":
                dd = Access.ActionId(b.getId().split(" "));
                break;
            case "dT":
                dTs = Access.ActionId(b.getId().split(" "));
                System.out.println(dTs);
                break;
            case "addTask":
                aT = Access.ActionId(b.getId().split(" "));
                break;
            default:
                // System.out.println(b.getId().split(" ")[0]);
                te = Access.ActionId(b.getId().split(" "));
                break;
        }

        //System.out.println(b.getId()+" "+ta);

        if(b.getId().equals("addList")){//Take to section of creation of new List
            boxTask.getChildren().clear();
            show();
            back.setOpacity(1);
            displayTracker++;
        }

        if(aT != null){
            boxTask.getChildren().clear();
            namer.setOpacity(1);
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker += 2;
            add = true;
            namer.setText("");
            namer.setPromptText("Entrez la nouvelle tache:");
            for(Tasks as : list.get(aT))
                boxTask.getChildren().add(initAddTask(as.getTitle()));

            System.out.println(aT);
            eIndex = aT;
        }

        //Take to the display of taskList selected
        if(list.size() != 0) {
            boxTask.getChildren().clear();
            eIndex = sl;
            if(list.containsKey(sl))
                for (Tasks ts : list.get(sl))
                    boxTask.getChildren().add(intiTask(ts.getTitle()));
        }


        if(ta != null){ // Récupère l'indice de la liste ou la tache voulue
            boxTask.getChildren().clear();
            namer.setOpacity(1);
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker++;
            lindex = Integer.parseInt(ta);
        }

        if(el != null){ // Récupère l'indice de la liste ou la tache voulue
            boxTask.getChildren().clear();
            namer.setOpacity(1);
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker++;
            eIndex = el;
        }

        if(dT != null){
            boxTask.getChildren().clear();
            list.remove(dT);
            try {
                save(list);
                initDelete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(dd != null){ // Actualiser la liste avec une tache en moins
            boxTask.getChildren().clear();
            list.get(eIndex).remove(Integer.parseInt(dd));
            try {
                save(list);
                int i = 0;
                for(Tasks sT : list.get(eIndex))
                    boxTask.getChildren().add(initDeleteTask(sT.getTitle(), String.valueOf(i++)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(te != null){ // Affiche les taches à éditer
            boxTask.getChildren().clear();
            back.setOpacity(1);
            displayTracker++;
            int i = 0;
            for(Tasks sT : list.get(te))
                boxTask.getChildren().add(initEditTask(sT.getTitle(), String.valueOf(i++)));
            eIndex = te;
        }

        if(dTs != null){ // Affiche les taches à supprimer
            boxTask.getChildren().clear();
            back.setOpacity(1);
            displayTracker++;
            int i = 0;
            for(Tasks sT : list.get(dTs))
                boxTask.getChildren().add(initDeleteTask(sT.getTitle(), String.valueOf(i++)));
            eIndex = dTs;
        }

    }


    private void show(){
        namer.setOpacity(1);
        aList.setOpacity(1);
        bAdd.setOpacity(1);
    }

    private void hide(){
        namer.setOpacity(0);
        aList.setOpacity(0);
        bAdd.setOpacity(0);
    }//Hide Components for modify tasklist

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (new File("SaveH.txt").exists()) {
            try {
                list = retrieveJ();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            list = new HashMap<>();
        }
        //System.out.println(list);


        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(2000 * 60 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Tester());
            }
        });
        t.start();
    }


    private static int find(String s){
        int i = 0;
        for(Tasks t : list.get(eIndex)){
            if(t.getTitle().equals(s)){
                return i;
            }else{
                i++;
            }
        }
        return i;
    }

    private void checker(){
        int i = 0;
        if(eIndex != null) {
            for (Tasks t : list.get(eIndex))
                if (!t.isDone())
                    i++;


            if (i == 0) {
                for (Tasks t : list.get(eIndex))
                    t.setDone(false);
            }
        }
    }

    private void feeback(String ad, String mes){
        Access.Feedback(ad, mes);
    }

    @FXML
    public void handleFeed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == sendfeed)
            feeback(emailadd.getText(), feedcomment.getText());
    }
}

class Tester implements Runnable{
    @Override
    public void run() {
        new NotifStage();
    }
}