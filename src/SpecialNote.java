import bagel.Input;
import bagel.Keys;

/**
 * An abstract superclass representing any special note.
 */
public abstract class SpecialNote extends Note {
    private final static int SPECIAL_DISTANCE = 50;

    /**
     * Create a new special note based on a set of parameters.
     * @param appearanceFrame The frame which the note appears on.
     * @param imageFileName The directory of the note's image.
     */
    public SpecialNote(int appearanceFrame, String imageFileName) {
        super(appearanceFrame, imageFileName);
    }

    protected abstract void activateEffect(Lane lane);

    // Intended to be overridden by special notes where a score needs to be given.
    protected int getScore() {
        return Accuracy.NOT_SCORED;
    }

    @Override
    public int checkScore(Input input, Lane lane, int targetHeight, Keys key) {
        if (!isActive()) {
            return Accuracy.NOT_SCORED;
        }

        int distance = Math.abs(targetHeight - getHeight());

        if (getHeight() >= targetHeight && distance > SPECIAL_DISTANCE) {
            deactivate();
        } else if (input.wasPressed(key) && distance <= SPECIAL_DISTANCE) {
            activateEffect(lane);
            deactivate();
            return getScore();
        }

        return Accuracy.NOT_SCORED;
    }
}
