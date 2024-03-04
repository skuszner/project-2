/**
 * A special type of note which will decrease the fall speed of notes by 1 when activated.
 */
public class SlowDownNote extends SpecialNote {
    private final static String ACTIVATE_MESSAGE = "SLOW DOWN";
    private final static int SCORE = 15;
    private final static String IMAGE_FILE = "res/noteSlowDown.png";

    public SlowDownNote(int appearanceFrame) {
        super(appearanceFrame, IMAGE_FILE);
    }

    @Override
    protected int getScore() {
        return SCORE;
    }

    @Override
    protected void activateEffect(Lane lane) {
        Accuracy.setMessage(ACTIVATE_MESSAGE);
        Level level = ShadowDance.getCurrentLevel();
        level.setFallSpeed(level.getFallSpeed() - 1);
    }
}
