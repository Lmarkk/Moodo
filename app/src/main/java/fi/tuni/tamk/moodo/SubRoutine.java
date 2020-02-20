package fi.tuni.tamk.moodo;

/**
 * Class that represents an individual step in the completion of a Routine. Each subroutine has a unique description.
 *
 * @author Lassi Markkinen
 * @version 2020.0220
 *
 */

public class SubRoutine {
    private String description;
    private boolean isComplete;

    public SubRoutine(String desc) {
        description = desc;
    }
}
