package fi.tuni.tamk.moodo;

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
public class Routine {
    private String name;
    private List subRoutines;

    public Routine() {
        subRoutines = new ArrayList<SubRoutine>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
