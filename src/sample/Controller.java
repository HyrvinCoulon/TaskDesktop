package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements EventHandler<ActionEvent>, Initializable {

    ArrayList<Tasks> s = new ArrayList<>();
    ArrayList<Tasks> ss = new ArrayList<>();
    static HashMap<String, ArrayList<Tasks>> list;
    String[] flagers;
    ScheduledExecutorService service;


    int lindex, displayTracker = 0;
    private Boolean add = false, cadd = false, delete, edit = false, liste = false;

    static String eIndex;

    @FXML
    ScrollPane scrollpane;

    @FXML
    AnchorPane feedpane, concurrentpane;

    @FXML
    private Button ListButton, DeleteButton, feed, reduce,AddButton, EditButton, bAdd, aList, back, sendfeed, addc;

    @FXML
    private TextField namer, emailadd, nameadd, titlec;

    @FXML private ComboBox<String> heure, minute;

    @FXML
    private Label titleTaskList;

    @FXML private TextArea feedcomment;

    @FXML
    private VBox boxTask;

    @FXML
    public void closeWindow(MouseEvent m){
        if(service != null)
            service.shutdown();
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
                if(cadd){
                        cadd = false;
                        mainpart();
                        back.setOpacity(0);
                        boxTask.getChildren().clear();
                        init();
                        displayTracker -= 1;
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
                }if(cadd){
                    cadd = false;
                    mainpart();
                    back.setOpacity(0);
                    boxTask.getChildren().clear();
                    init();
                    displayTracker -= 2;
                }
                break;
        }
        eIndex = null;
    }

    @FXML
    public void handles(ActionEvent e) throws IOException { //Action doing by the main buttons List, Add, Delete and Edit

        checker();
        if(e.getSource() == ListButton){ // Show the List of List
            mainpart();
            hide();
            liste = true;
            add = cadd = false;
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
            mainpart();
            delete = edit = liste = cadd = false;
            boxTask.getChildren().clear();
            init();
        }else if(e.getSource() == bAdd){ // Depend on which boolean is set

            if(add && displayTracker == 2){
                if(cadd == true){
                    list.get(eIndex).add(new Tasks(titlec.getText(), heure.getValue(), minute.getValue()));
                    save(list);
                }else{
                    boxTask.getChildren().add(initAdd(namer.getText()));
                    list.get(eIndex).add(new Tasks(namer.getText()));
                    namer.setText("");
                    save(list);
                }
                flagers = flager();
            }else if(add) { // Will add tasks

                if(cadd == true){
                    ss.add(new Tasks(titlec.getText(), heure.getValue(), minute.getValue()));
                    add(titlec.getText());
                    titlec.setText("");
                }else {
                    ss.add(new Tasks(namer.getText()));
                    add(namer.getText());
                    namer.setText("");
                }
                save(list);
                flagers = flager();
            }else if(edit && displayTracker == 2){ // Will modify Title of Tasks

                if(cadd == true){
                    list.get(eIndex).set(lindex, new Tasks(titlec.getText(), heure.getValue(), minute.getValue()));
                    titlec.setText("");
                    save(list);
                }else {
                    list.get(eIndex).set(lindex, new Tasks(namer.getText()));
                    namer.setText("");
                    save(list);
                }

            }else if(edit){// Will modify Title of list
                s = list.get(eIndex);
                list.remove(eIndex);
                list.put(namer.getText().replace(" ", "_"), s);
                namer.setText("");
                save(list);
                flagers = flager();
            }else if(delete == true && displayTracker == 2){ // delete Tasks
                list.get(eIndex).remove(lindex);
                save(list);

            }else if(delete){ // Delete List
                list.remove(eIndex);
                save(list);
                flagers = flager();
            }else{

                if(namer.getPromptText().equals("Entrez le titre de la liste")) {
                    if(cadd == true){
                        titleTaskList.setText(namer.getText());
                        namer.setText("");
                        namer.setOpacity(0);
                    }else {
                        titleTaskList.setText(namer.getText());
                        namer.setText("");
                        namer.setPromptText("Entrer les différentes taches :");
                    }
                    add = true;
                }
            }

        }else if(e.getSource() == aList){ // Will add the new List to The super List

            //System.out.println(ss);

            list.put(titleTaskList.getText().replace(" ", "_"), ss);
            //System.out.println(list);
            titleTaskList.setText("");
            save(list);
            list = retrieveJ();

            ss = new ArrayList<>();
        }else if(e.getSource() == EditButton){// Show the display for modification of the list
            hide();
            mainpart();
            boxTask.getChildren().clear();
            delete = add = cadd = false;
            edit = true;
            initEdit();
        }else if(e.getSource() == DeleteButton){ // Part Of Deletion
            hide();
            mainpart();
            edit = add = cadd = false;
            delete = true;
            boxTask.getChildren().clear();
            initDelete();
        }else if(e.getSource() == feed){
            feedpart();
        }else if(e.getSource() == addc){
            boxTask.getChildren().clear();
            init("c");
            delete = edit = add = false;
        }

    }

    private void feedpart(){
        scrollpane.setOpacity(0);
        concurrentpane.setOpacity(0);
        feedpane.toFront();
        feedpane.setOpacity(1);
    }

    private void concurrentpart(){
        //feedpane.setOpacity(0);
        //scrollpane.toBack();
        concurrentpane.toFront();
        concurrentpane.setOpacity(1);
        scrollpane.setOpacity(0);
    }

    private void mainpart(){
        feedpane.setOpacity(0);
        scrollpane.toFront();
        concurrentpane.toBack();
        concurrentpane.setOpacity(0);
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

    private void init(String cs) throws IOException { //Initialisation of add part
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ModelActionTask.fxml"));
        f.setController(cL);
        Node n = f.load();
        cL.setId("addList" + cs);
        cL.setAction(this);
        boxTask.getChildren().add(n);

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

    private Node intiTask(String s, String s1){ //Init Tasks for ListTask Section
        ControllerList cL = new ControllerList();
        FXMLLoader f = new FXMLLoader(getClass().getResource("/view/ConcurrentBloc.fxml"));
        f.setController(cL);
        Node n = null;
        try {
            n = f.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cL.set(s, s1);

        cL.setButtonId(s);

        if(list.get(eIndex).get(find(s)).isDone()) {
            cL.setImage(new Image(String.valueOf(getClass().getResource("/images/checked.png"))));
            //cL.setStringB("Terminée");
        }else{
            cL.setImage(new Image(String.valueOf(getClass().getResource("/images/progress.png"))));
            //cL.setStringB("En cours ..");
        }

        if(list.get(eIndex).get(find(s)).isNotdone()){
            cL.setImage(new Image(String.valueOf(getClass().getResource("/images/fail.png"))));
        }

        cL.setShow(actionEvent1 -> {
            ImageView bn = (ImageView) actionEvent1.getSource();
            if(bn.getId().split(" ")[0].equals("check")){
                if(!list.get(eIndex).get(find(s)).isNotdone()) {
                    if (!list.get(eIndex).get(find(s)).isDone()) {
                        cL.setImage(new Image(String.valueOf(getClass().getResource("/images/checked.png"))));
                        //cL.setStringB("Terminée");
                        list.get(eIndex).get(find(s)).setDone(true);
                    } else {
                        cL.setImage(new Image(String.valueOf(getClass().getResource("/images/progress.png"))));
                        //cL.setStringB("En cours ..");
                        list.get(eIndex).get(find(s)).setDone(false);
                    }
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
        cL.setBImage(new Image(String.valueOf(getClass().getResource("/images/edit_tasks.png"))));
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
        cL.setBImage(new Image(String.valueOf(getClass().getResource("/images/delete_tasks.png"))));
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

    /*public void handler(MouseEvent mouseEvent){
        if(mouseEvent.getSource() == )
    }*/

    @Override
    public void handle(ActionEvent actionEvent) {
        Button b = (Button) actionEvent.getSource();
        String sl = b.getId();

        //System.out.println(sl);

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

        if(b.getId().equals("addListc")){
            concurrentpart();
            cadd = true;
            show();
            back.setOpacity(1);
            displayTracker++;
        }

        if(aT != null){
            boxTask.getChildren().clear();
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker += 2;
            add = true;
            if(Arrays.asList(flagers).contains(aT)){
                concurrentpart();
                cadd = true;
            }else {
                namer.setOpacity(1);
                namer.setText("");
                namer.setPromptText("Entrez la nouvelle tache:");
                for (Tasks as : list.get(aT))
                    boxTask.getChildren().add(initAddTask(as.getTitle()));
            }
            //System.out.println(aT);
            eIndex = aT;
        }

        //Take to the display of taskList selected
        if(list.size() != 0 && liste == true ){
            try {
                boxTask.getChildren().clear();
                eIndex = sl;
                if (list.containsKey(sl))
                    for (Tasks ts : list.get(sl)) {
                        if(ts.getLocalTime().toString().equals("00:00"))
                            boxTask.getChildren().add(intiTask(ts.getTitle(), ""));
                        else
                            boxTask.getChildren().add(intiTask(ts.getTitle(), Objects.requireNonNullElse(ts.getLocalTime().toString(), "")));
                    }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        if(ta != null){ // Récupère l'indice de la liste ou la tache voulue
            boxTask.getChildren().clear();
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker++;
            if(Arrays.asList(flagers).contains(eIndex)){
                cadd = true;
                concurrentpart();
            }else{
                namer.setOpacity(1);
            }
            lindex = Integer.parseInt(ta);
        }

        if(el != null){ // Récupère l'indice de la liste ou la tache voulue
            boxTask.getChildren().clear();
            namer.setOpacity(1);
            bAdd.setOpacity(1);
            back.setOpacity(1);
            displayTracker++;
            if(Arrays.asList(flagers).contains(el)){
                cadd = true;
            }
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

        comboinit();

        if(list.size() != 0)
            flagers = flager();
        ConcurrentInit();

        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(
                            60 *
                            60 *   // seconds to a minute
                            1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(list.size() != 0)
                    Platform.runLater(new Tester("Un nouvelle tâche vous attend.", word(list)));
            }
        });
        t.start();
    }

    private void ConcurrentInit(){
        if(list.size() != 0) {
            service = Executors.newScheduledThreadPool(list.size());
            for (String s1 : flagers) {
                Runnable r = () -> {
                    //System.out.println(s1);
                    for (Tasks t : list.get(s1)) {
                        int minutes = LocalTime.now().getMinute() - t.getLocalTime().getMinute();
                        int hour = LocalTime.now().getHour() - t.getLocalTime().getHour();
                        if(hour <= 0) {
                            if(minutes > 0 && hour == 0 && !t.isDone())
                                t.setNotdone(true);
                            if (minutes < 0)
                                minutes *= -1;
                            hour *= -1;
                            System.out.println(hour + " : " + minutes);
                            System.out.println(t.isDone() + " " + t.isNotdone() + " " );
                            if(t.isDone() == false && hour == 0 && minutes <= 30 && t.isNotdone() == false) {
                                Platform.runLater(new Tester("Un nouvelle tâche vous attend.", word(list)));
                            }
                        }
                    }
                };
                service.scheduleAtFixedRate(r, 2, 20, TimeUnit.SECONDS);
            }
        }
    }

    private String word(HashMap<String, ArrayList<Tasks>> lt){
        return Access.word(lt);
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

        System.out.println(eIndex);
        if(eIndex != null && list != null && cadd == false) {
            for (Tasks t : list.get(eIndex))
                if (!t.isDone())
                    i++;


            if (i == 0) {
                for (Tasks t : list.get(eIndex))
                    t.setDone(false);
            }
        }
    }

    void comboinit(){
        for(int i = 0; i < 60; i++){
            if(i < 10){
                heure.getItems().add("0" + i);
                minute.getItems().add("0" + i);
            }else if(i < 25){
                heure.getItems().add(String.valueOf(i));
                minute.getItems().add(String.valueOf(i));
            }else{
                minute.getItems().add(String.valueOf(i));
            }

        }
    }

    String[] flager(){
        String[] l = new String[list.size()];
        int i = 0;
        for(String sss : list.keySet()){
            if(!list.get(sss).get(0).getLocalTime().toString().equals("00:00")){
                l[i] = sss;
                i++;
            }
        }
        return l;
    }

    private void feeback(String name, String ad, String mes){
        try {
            Access.Feedback(name, ad, mes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleFeed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == sendfeed)
            feeback(nameadd.getText(), emailadd.getText(), feedcomment.getText());
    }
}

class Tester implements Runnable{
    String s, word;
    public Tester(String s, String word) {
        this.s = s;
        this.word = word;
    }

    @Override
    public void run() {
        new NotifStage(s, word);
    }
}