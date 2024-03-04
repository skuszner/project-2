import bagel.Image;
import bagel.util.Point;

import java.util.Random;

/**
 * An entity which moves around and steals notes.
 */
public class Enemy {
    private final static int COLLISION_RANGE = 104;
    private final static int MIN_X = 100;
    private final static int MAX_X = 900;
    private final static int MIN_Y = 100;
    private final static int MAX_Y = 500;
    private final static int SPEED = 1;
    private final static String IMAGE_FILE = "res/enemy.png";

    private final Image image;
    private int positionX;
    private final int positionY;
    private int direction;
    private boolean inactive = false;

    /**
     * Create a new enemy in a random position.
     */
    public Enemy() {
        Random random = new Random();
        positionX = random.nextInt(MAX_X - MIN_X + 1) + MIN_X;
        positionY = random.nextInt(MAX_Y - MIN_Y + 1) + MIN_Y;
        direction = random.nextBoolean() ? 1 : -1;
        image = new Image(IMAGE_FILE);
    }

    /**
     * Determines whether the enemy is within collision range of a point.
     * @param x The x position of the point.
     * @param y The y position of the point.
     * @return Whether the enemy is colliding with the point or not.
     */
    public boolean isCollidingWith(double x, double y) {
        return Math.sqrt(Math.pow(x - positionX, 2) + Math.pow(y - positionY, 2)) <= COLLISION_RANGE;
    }

    /**
     * Gets the current position of the enemy on the screen.
     * @return The current position of the enemy as a point.
     */
    public Point getPosition() {
        return new Point(positionX, positionY);
    }

    /**
     * Gets whether the enemy should be rendered or is inactive.
     * @return Whether the enemy is inactive or not.
     */
    public boolean isInactive() {
        return inactive;
    }

    /**
     * Deactivates the enemy, queuing it for deletion.
     */
    public void deactivate() {
        inactive = true;
    }

    /**
     * Render the enemy and update its position every frame.
     */
    public void update() {
        if (positionX <= 100 || positionX >= 900) {
            direction = -direction;
        }

        positionX += direction * SPEED;

        image.draw(positionX, positionY);
    }
}
