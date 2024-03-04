/**
 * A special type of note which will clear all notes in its lane when activated.
 */
public class BombNote extends SpecialNote {
    private final static String ACTIVATE_MESSAGE = "LANE CLEAR";
    private final static String IMAGE_FILE = "res/noteBomb.png";

    public BombNote(int appearanceFrame) {
        super(appearanceFrame, IMAGE_FILE);
    }

    @Override
    protected void activateEffect(Lane lane) {
        Accuracy.setMessage(ACTIVATE_MESSAGE);
        lane.bombLane();
    }
}
