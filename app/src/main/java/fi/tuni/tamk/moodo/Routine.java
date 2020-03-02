package fi.tuni.tamk.moodo;

import java.io.Serializable;
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

    public Routine(int id, String name) {
        setName(name);
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() > 0) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name field can't be empty");
        }
    }

    public void setId(int id) {
        if(id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("Index must be over 1");
        }
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
