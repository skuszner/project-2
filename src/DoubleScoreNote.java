/**
 * A special type of note which will double the score of notes for a temporary time when activated.
 */
public class DoubleScoreNote extends SpecialNote {
    private final static String ACTIVATE_MESSAGE = "DOUBLE SCORE";
    private final static int DOUBLE_SCORE_DURATION = 480;
    private final static String IMAGE_FILE = "res/note2x.png";

    public DoubleScoreNote(int appearanceFrame) {
        super(appearanceFrame, IMAGE_FILE);
    }

    @Override
    protected void activateEffect(Lane lane) {
        Accuracy.setMessage(ACTIVATE_MESSAGE);
        ShadowDance.getCurrentLevel().activateDoubleScore(DOUBLE_SCORE_DURATION);
    }
}
