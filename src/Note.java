import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Represents a note inside a lane.
 */
public class Note {
    private final static int START_HEIGHT = 100;

    private final int appearanceFrame;
    private final Image image;
    private int height;
    private boolean active = false;
    private boolean completed = false;

    protected Note(int appearanceFrame, String imageFileName, int startHeight) {
        this.appearanceFrame = appearanceFrame;
        image = new Image(imageFileName);
        height = startHeight;
    }

    /**
     * Create a new note based on a set of parameters.
     * @param appearanceFrame The frame which the note appears on.
     * @param imageFileName The directory of the note's image.
     */
    public Note(int appearanceFrame, String imageFileName) {
        this(appearanceFrame, imageFileName, START_HEIGHT);
    }

    protected boolean isActive() {
        return active;
    }

    /**
     * Gets the y position of the note.
     * @return The y position of the note.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Determines whether the note has already been scored.
     * @return Whether the note is completed or not.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Deactivates a note making it no longer visible.
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Evaluates the score of the note. Ran every frame.
     * @param input The bagel input object.
     * @param lane The lane which the note is contained in.
     * @param targetHeight The height of the target note.
     * @param key The key which must be pressed to trigger the note.
     * @return The score given for the note.
     */
    public int checkScore(Input input, Lane lane, int targetHeight, Keys key) {
        if (!active) {
            return Accuracy.NOT_SCORED;
        }

        int score = Accuracy.evaluateScore(height, targetHeight, input.wasPressed(key));
        if (score != Accuracy.NOT_SCORED) {
            deactivate();
        }

        return score;
    }

    /**
     * Draw the note's image.
     * @param x The x coordinate of the note.
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, height);
        }
    }

    /**
     * Called every frame to simulate the note falling and check if the note should be visible.
     * @param currentFrame The current frame number of the level.
     */
    public void update(int currentFrame, int fallSpeed) {
        if (active) {
            height += fallSpeed;
        }

        if (currentFrame >= appearanceFrame && !completed) {
            active = true;
        }
    }
}
