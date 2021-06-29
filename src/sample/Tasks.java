package sample;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Tasks implements Serializable {
    private String title = "";
    private boolean done = false;
    //private Date date;
    private LocalTime localTime;

    Tasks(String title){
        this.title = title;
    }

    Tasks(String title, String h, String m){
        this.title = title;
        localTime = LocalTime.parse(h + ":" + m);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }
}
