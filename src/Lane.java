import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;

/**
 * Represents a lane inside a level.
 */
public class Lane {
    private final static int HEIGHT = 384;
    private final static int TARGET_HEIGHT = 657;

    private final String type;
    private final int positionX;
    private final ArrayList<Note> notes;
    private final Image image;
    private final Keys key;
    private int currentNote = 0;
    private boolean shouldClearNotes = false;

    /**
     * Create a new lane based on its type.
     * @param type The type of the lane which will determine what key must be pressed.
     * @param positionX The x position of the lane.
     */
    public Lane(String type, int positionX) {
        this.type = type;
        this.positionX = positionX;
        notes = new ArrayList<>();
        image = new Image("res/lane" + type + ".png");
        key = type.equals("Special") ? Keys.SPACE : Keys.valueOf(type.toUpperCase());
    }

    /**
     * Gets the type of the lane.
     * @return The type of the lane.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the x position of the lane.
     * @return The x position of the lane.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Adds a note to the lane.
     * @param note The note to add.
     */
    public void addNote(Note note) {
        notes.add(note);
    }

    /**
     * Determines whether a lane is finished, which is when all of its notes are completed.
     * @return Whether the lane is finished.
     */
    public boolean isFinished() {
        for (Note note : notes) {
            if (!note.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Render the lane and all of its notes.
     */
    public void draw() {
        image.draw(positionX, HEIGHT);

        for (Note note : notes) {
            note.draw(positionX);
        }
    }

    /**
     * Removes all notes which are currently visible on the screen when the next note is completed.
     */
    public void bombLane() {
        shouldClearNotes = true;
    }

    /**
     * Gets the notes which are currently visible in the lane.
     * @return A list of notes which are currently visible.
     */
    public ArrayList<Note> getVisibleNotes() {
        ArrayList<Note> visibleNotes = new ArrayList<>();

        for (int i = currentNote; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (note.isActive()) {
                visibleNotes.add(note);
            }
        }

        return visibleNotes;
    }

    /**
     * Draw the lane and its notes as well as calculate and return the score every frame.
     * @param input The bagel input object.
     * @param currentFrame The current frame of the level.
     * @return The total score given for this frame.
     */
    public int update(Input input, int currentFrame) {
        draw();

        for (Note note : notes) {
            note.update(currentFrame, ShadowDance.getCurrentLevel().getFallSpeed());
        }

        if (currentNote < notes.size()) {
            Note note = notes.get(currentNote);
            int score = note.checkScore(input, this, TARGET_HEIGHT, key);
            if (note.isCompleted()) {
                if (shouldClearNotes) {
                    for (int i = currentNote; i < notes.size(); i++) {
                        Note nextNote = notes.get(i);
                        if (nextNote.isActive()) {
                            nextNote.deactivate();
                            currentNote++;
                        }
                    }
                    shouldClearNotes = false;
                } else {
                    while (currentNote < notes.size() && notes.get(currentNote).isCompleted()) {
                        currentNote++;
                    }
                }
            }
            return score;
        }

        return Accuracy.NOT_SCORED;
    }
}
