package fi.tuni.tamk.moodo;

import java.io.Serializable;

/**
 * Class that represents an individual step in the completion of a Routine. Each subroutine has a unique description.
 *
 * @author Lassi Markkinen
 * @version 2020.0220
 *
 */

public class SubRoutine implements Serializable {
    private String description;
    private boolean isComplete;
    private int id;

    public SubRoutine(int id, String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
