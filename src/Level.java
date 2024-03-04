import bagel.Font;
import bagel.Input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Represents a single level in the ShadowDance game.
 */
public class Level {
    private final static int DEFAULT_NOTE_FALL_SPEED = 2;
    private final static int TOTAL_SCORE_POSITION_X = 35;
    private final static int TOTAL_SCORE_POSITION_Y = 35;
    private final static int LEVEL_1_SCORE = 150;
    private final static int LEVEL_2_SCORE = 400;
    private final static int LEVEL_3_SCORE = 350;
    private final static int ENEMY_LEVEL = 3;
    private final static int ENEMY_FREQUENCY = 600;

    private final Font TOTAL_SCORE_FONT = new Font("res/FSO8BITR.TTF", 30);
    private final int levelNumber;
    private final ArrayList<Lane> lanes = new ArrayList<>();
    private final ArrayList<Integer> activeDoubleScores = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final Guardian guardian = new Guardian();
    private int currentFrame = 0;
    private int totalScore = 0;
    private int fallSpeed = DEFAULT_NOTE_FALL_SPEED;

    /**
     * Creates and loads a new level based on the level number.
     * @param levelNumber The number of the level used to determine which level csv to load.
     */
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        readCSV("res/level" + levelNumber + ".csv");
    }

    private void readCSV(String fileName) {
        // Load a level from its csv file by creating relevant objects.
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;

            while ((text = br.readLine()) != null) {
                String[] arguments = text.split(",");

                if (arguments[0].equals("Lane")) {
                    lanes.add(new Lane(arguments[1], Integer.parseInt(arguments[2])));
                } else {
                    // Get the lane the note should be in.
                    Lane relevantLane = null;
                    for (Lane lane : lanes) {
                        if (lane.getType().equals(arguments[0])) {
                            relevantLane = lane;
                            break;
                        }
                    }

                    // Create the note
                    if (relevantLane != null) {
                        int appearanceFrame = Integer.parseInt(arguments[2]);
                        switch (arguments[1]) {
                            case "Normal":
                                relevantLane.addNote(new Note(appearanceFrame,
                                        "res/note" + relevantLane.getType() + ".png"));
                                break;
                            case "Hold":
                                relevantLane.addNote(new HoldNote(appearanceFrame,
                                        "res/holdNote" + relevantLane.getType() + ".png"));
                                break;
                            case "Bomb":
                                relevantLane.addNote(new BombNote(appearanceFrame));
                                break;
                            case "DoubleScore":
                                relevantLane.addNote(new DoubleScoreNote(appearanceFrame));
                                break;
                            case "SpeedUp":
                                relevantLane.addNote(new SpeedUpNote(appearanceFrame));
                                break;
                            case "SlowDown":
                                relevantLane.addNote(new SlowDownNote(appearanceFrame));
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines if the level is over by checking if all lanes are finished.
     * @return Whether the level is over or not.
     */
    public boolean isFinished() {
        // Check if all notes in every lane have fallen and the game should end
        for (Lane lane : lanes) {
            if (!lane.isFinished()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether the level was won depending on the score and level number.
     * @return Whether the level was won or not.
     */
    public boolean didWin() {
        switch (levelNumber) {
            case (1):
                return totalScore >= LEVEL_1_SCORE;
            case (2):
                return totalScore >= LEVEL_2_SCORE;
            case (3):
                return totalScore >= LEVEL_3_SCORE;
        }

        return false;
    }

    /**
     * Gets the speed at which notes fall every frame.
     * @return The fall speed of notes.
     */
    public int getFallSpeed() {
        return fallSpeed;
    }

    /**
     * Sets the speed at which notes fall every frame.
     * @param fallSpeed The fall speed of notes.
     */
    public void setFallSpeed(int fallSpeed) {
        this.fallSpeed = Math.max(fallSpeed, 1);
    }

    /**
     * Activates the double score effect.
     * @param duration The amount of frames the double score effect should be active for.
     */
    public void activateDoubleScore(int duration) {
        activeDoubleScores.add(duration);
    }

    /**
     * Processes one frame of the level, potentially reading from input.
     * @param input The bagel input object.
     */
    public void update(Input input) {
        if (isFinished()) {
            return;
        }

        currentFrame++;

        if (levelNumber == ENEMY_LEVEL) {
            // Create a new enemy every ENEMY_FREQUENCY frames if on ENEMY_LEVEL.
            if (currentFrame % ENEMY_FREQUENCY == 0) {
                enemies.add(new Enemy());
            }

            // Update all enemies and check collisions with notes.
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);

                if (enemy.isInactive()) {
                    // Enemy was hit by a projectile.
                    enemies.remove(i);
                    continue;
                }

                enemy.update();

                for (Lane lane : lanes) {
                    for (Note note : lane.getVisibleNotes()) {
                        // An enemy can only collide with normal notes.
                        // I don't like this, but I can't think of a nicer way to implement it.
                        if (!note.getClass().equals(Note.class)) {
                            continue;
                        }

                        if (enemy.isCollidingWith(lane.getPositionX(), note.getHeight())) {
                            note.deactivate();
                        }
                    }
                }
            }

            guardian.update(input, enemies);
        }

        // Update all active double scores and remove any which have ended
        for (int i = activeDoubleScores.size() - 1; i >= 0; i--) {
            int duration = activeDoubleScores.get(i);
            if (duration == 0) {
                activeDoubleScores.remove(i);
            } else {
                activeDoubleScores.set(i, duration - 1);
            }
        }

        for (Lane lane : lanes) {
            totalScore += lane.update(input, currentFrame) * ((int) Math.pow(2, activeDoubleScores.size()));
        }

        TOTAL_SCORE_FONT.drawString("SCORE " + totalScore, TOTAL_SCORE_POSITION_X, TOTAL_SCORE_POSITION_Y);

        Accuracy.update();
    }
}