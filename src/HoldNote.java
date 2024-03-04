import bagel.Input;
import bagel.Keys;

/**
 * Represents a hold note inside a lane.
 */
public class HoldNote extends Note {
    private final static int START_HEIGHT = 24;
    private final static int HEIGHT_OFFSET = 82;

    private boolean pressed = false;

    /**
     * Create a new hold note based on a set of parameters.
     * @param appearanceFrame The frame which the hold note appears on.
     * @param imageFileName The directory of the hold note's image.
     */
    public HoldNote(int appearanceFrame, String imageFileName) {
        super(appearanceFrame, imageFileName, START_HEIGHT);
    }

    // Gets the ending position of the hold note
    private int getTopHeight() {
        return getHeight() - HEIGHT_OFFSET;
    }

    // Gets the starting position of the hold note
    private int getBottomHeight() {
        return getHeight() + HEIGHT_OFFSET;
    }

    // Modified so that two scores are calculated. One when the note is triggered, and another when the note is released.
    @Override
    public int checkScore(Input input, Lane lane, int targetHeight, Keys key) {
        if (!isActive()) {
            return Accuracy.NOT_SCORED;
        }

        int score;

        if (pressed) {
            score = Accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(key));
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
            }
        } else {
            score = Accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(key));
            if (score == Accuracy.MISS_SCORE) {
                deactivate();
            } else if (score != Accuracy.NOT_SCORED) {
                pressed = true;
            }
        }

        return score;
    }
}
