package fi.tuni.tamk.moodo.Classes;

import java.io.Serializable;


public class SubRoutine implements Serializable {
    private String description;
    private int id;

    public SubRoutine(int id, String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return description;
    }
}
