package sample;

import java.io.Serializable;

public class Tasks implements Serializable {
    private String title = "";
    private boolean done = false;

    Tasks(String title){
        this.title = title;
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
}
