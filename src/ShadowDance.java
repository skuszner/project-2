import bagel.*;

/**
 * SWEN20003 Project 2, Semester 2, 2023.
 * Inspiration taken from Project 1 solution.
 * @author Sebastian Kuszner
 */

public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 64;
    private final static int INSTRUCTIONS_FONT_SIZE = 24;
    private final static int TITLE_POSITION_Y = 250;
    private final static int INSTRUCTIONS_TOP_POSITION_Y = TITLE_POSITION_Y + 190;
    private final static int INSTRUCTIONS_BOTTOM_POSITION_Y = INSTRUCTIONS_TOP_POSITION_Y + INSTRUCTIONS_FONT_SIZE + 16;
    private final static int LEVEL_SELECTION_POSITION_Y = INSTRUCTIONS_BOTTOM_POSITION_Y + 100;
    private final static int LEVEL_COUNT = 3;
    private final static String LEVEL_SELECTION_TEXT = "1 2 3";
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final static String INSTRUCTIONS_TOP_LINE = "SELECT LEVELS WITH";
    private final static String INSTRUCTIONS_BOTTOM_LINE = "NUMBER KEYS";
    private final static String WIN_TEXT = "CLEAR!";
    private final static int WIN_POSITION_Y = 300;
    private final static String LOSE_TEXT = "TRY AGAIN";
    private final static String RETURN_TEXT = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private final static int RETURN_POSITION_Y = 500;
    private final static String NUM_KEY_PREFIX = "NUM_";

    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Font TITLE_FONT = new Font("res/FSO8BITR.TTF", DEFAULT_FONT_SIZE);
    private final Font INSTRUCTIONS_FONT = new Font("res/FSO8BITR.TTF", INSTRUCTIONS_FONT_SIZE);
    private static Level currentLevel;

    /**
     * Create a new instance of the ShadowDance game.
     */
    public ShadowDance() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Run the game when the program executes.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Get the level which is currently being played.
     * @return The level currently being played.
     */
    public static Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Update the state of the game, potentially reading from input.
     * @param input The bagel input object.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        BACKGROUND_IMAGE.draw(0.5 * Window.getWidth(), 0.5 * Window.getHeight());

        // Check if the player started a level
        for (int numKey = 1; numKey <= LEVEL_COUNT; numKey++) {
            if (input.wasPressed(Keys.valueOf(NUM_KEY_PREFIX + numKey)) && currentLevel == null) {
                currentLevel = new Level(numKey);
            }
        }

        if (currentLevel == null) {
            // Draw title screen
            TITLE_FONT.drawString(GAME_TITLE,
                    0.5 * (Window.getWidth() - TITLE_FONT.getWidth(GAME_TITLE)),
                    TITLE_POSITION_Y);
            INSTRUCTIONS_FONT.drawString(INSTRUCTIONS_TOP_LINE,
                    0.5 * (Window.getWidth() - INSTRUCTIONS_FONT.getWidth(INSTRUCTIONS_TOP_LINE)),
                    INSTRUCTIONS_TOP_POSITION_Y);
            INSTRUCTIONS_FONT.drawString(INSTRUCTIONS_BOTTOM_LINE,
                    0.5 * (Window.getWidth() - INSTRUCTIONS_FONT.getWidth(INSTRUCTIONS_BOTTOM_LINE)),
                    INSTRUCTIONS_BOTTOM_POSITION_Y);
            INSTRUCTIONS_FONT.drawString(LEVEL_SELECTION_TEXT,
                    0.5 * (Window.getWidth() - INSTRUCTIONS_FONT.getWidth(LEVEL_SELECTION_TEXT)),
                    LEVEL_SELECTION_POSITION_Y);
            return;
        }

        if (currentLevel.isFinished()) {
            // End the game and calculate whether the player won or not since the level is over
            String textToDraw = currentLevel.didWin() ? WIN_TEXT : LOSE_TEXT;
            TITLE_FONT.drawString(textToDraw,
                    0.5 * (Window.getWidth() - TITLE_FONT.getWidth(textToDraw)),
                    WIN_POSITION_Y);
            INSTRUCTIONS_FONT.drawString(RETURN_TEXT,
                    0.5 * (Window.getWidth() - INSTRUCTIONS_FONT.getWidth(RETURN_TEXT)),
                    RETURN_POSITION_Y);

            if (input.wasPressed(Keys.SPACE)) {
                currentLevel = null;
                Accuracy.reset();
            }

            return;
        }

        currentLevel.update(input);
    }
}
