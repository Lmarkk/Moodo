package fi.tuni.tamk.moodo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds information about a user's daily activity. The Routine contains several
 * SubRoutines.
 *
 * @author Lassi Markkinen
 * @version 2020.0220
 *
 */
public class Routine implements Serializable {
    private String name;
    private List subRoutines;
    private int id;

    public Routine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int returnId() {
        return id;
    }

    public void setSubRoutines(List<SubRoutine> subRoutines) {
        this.subRoutines = subRoutines;
    }

    public List getSubRoutines() {
        return subRoutines;
    }

    @Override
    public String toString() {
        return name;
    }
}
