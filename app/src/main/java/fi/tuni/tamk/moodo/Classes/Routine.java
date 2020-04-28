package fi.tuni.tamk.moodo.Classes;

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
    private int time;
    private String name;
    private List<SubRoutine> subRoutines = new ArrayList<>();
    private int id;

    public Routine(int id, String name, int time) {
        setName(name);
        setId(id);
        setTime(time);
    }

    public Routine(int id, String name, int time, List<SubRoutine> subRoutines) {
        setName(name);
        setId(id);
        setTime(time);
        setSubRoutines(subRoutines);
    }

    public int getTime() {
        return time;
    }

    private void setTime(int time) {
        if(time > 0) {
            this.time = time;
        } else {
            throw new IllegalArgumentException("Time must be larger than 0");
        }
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        if(name.length() > 0) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name field can't be empty");
        }
    }

    private void setId(int id) {
        if(id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("Index must be over 1");
        }
    }

    public int getId() {
        return id;
    }

    public void setSubRoutines(List<SubRoutine> subRoutines) {
        this.subRoutines = new ArrayList<>(subRoutines);
    }

    public void addSubRoutine(SubRoutine subRoutine) {
        subRoutines.add(subRoutine);
    }

    public List<SubRoutine> getSubRoutines() {
        return subRoutines;
    }

    @Override
    public String toString() {
        return name;
    }
}
