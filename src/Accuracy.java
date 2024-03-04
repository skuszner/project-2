import bagel.Font;
import bagel.Window;

/**
 * A utility class for calculating and displaying a score.
 */
public final class Accuracy {
    private final static String PERFECT_TEXT = "PERFECT";
    private final static int PERFECT_DISTANCE = 15;
    private final static int PERFECT_SCORE = 10;
    private final static String GOOD_TEXT = "GOOD";
    private final static int GOOD_DISTANCE = 50;
    private final static int GOOD_SCORE = 5;
    private final static String BAD_TEXT = "BAD";
    private final static int BAD_DISTANCE = 100;
    private final static int BAD_SCORE = -1;
    private final static String MISS_TEXT = "MISS";
    private final static int MISS_DISTANCE = 200;
    /**
     * The score given when a note is missed.
     */
    public final static int MISS_SCORE = -5;
    /**
     * A value to represent if a note was not scored.
     */
    public final static int NOT_SCORED = 0;
    private final static int MESSAGE_RENDER_FRAMES = 30;
    private final static int ACCURACY_FONT_SIZE = 40;
    private final static Font ACCURACY_FONT = new Font("res/FSO8BITR.TTF", ACCURACY_FONT_SIZE);
    private static int framesRemaining = 0;
    private static String scoreMessage = null;

    /**
     * Resets the score notification. Mostly used when a level ends and a new one might start.
     */
    public static void reset() {
        framesRemaining = 0;
        scoreMessage = null;
    }

    /**
     * Sets the message to be rendered on screen.
     * @param message The text of the message.
     */
    public static void setMessage(String message) {
        framesRemaining = MESSAGE_RENDER_FRAMES;
        scoreMessage = message;
    }

    /**
     * Calculate and display a score based on the distance between a note and the target note.
     * @param height The height of the note.
     * @param targetHeight The height of the target note.
     * @param triggered Whether the note's corresponding key was pressed.
     * @return The score given for the note.
     */
    public static int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(targetHeight - height);

        if (height >= targetHeight && distance > BAD_DISTANCE) {
            setMessage(MISS_TEXT);
            return MISS_SCORE;
        }

        if (triggered) {
            if (distance <= PERFECT_DISTANCE) {
                setMessage(PERFECT_TEXT);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_DISTANCE) {
                setMessage(GOOD_TEXT);
                return GOOD_SCORE;
            } else if (distance <= BAD_DISTANCE) {
                setMessage(BAD_TEXT);
                return BAD_SCORE;
            } else if (distance <= MISS_DISTANCE) {
                setMessage(MISS_TEXT);
                return MISS_SCORE;
            }
        }

        return NOT_SCORED;
    }

    /**
     * Draw the score message every frame if there is one to display.
     */
    public static void update() {
        if (scoreMessage != null && framesRemaining > 0) {
            framesRemaining--;
            ACCURACY_FONT.drawString(scoreMessage,
                    0.5 * (Window.getWidth() - ACCURACY_FONT.getWidth(scoreMessage)),
                    0.5 * (Window.getHeight() + ACCURACY_FONT_SIZE));
        }
    }
}
