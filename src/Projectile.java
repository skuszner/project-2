import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Vector2;

/**
 * An object which the guardian fires at enemies to kill them.
 */
public class Projectile {
    private final static int COLLISION_RANGE = 62;
    private final static int SPEED = 6;
    private final static String IMAGE_FILE = "res/arrow.png";

    private double positionX;
    private double positionY;
    private boolean inactive = false;
    private final Vector2 velocity;
    private final Image image = new Image(IMAGE_FILE);
    private final DrawOptions drawOptions = new DrawOptions();

    /**
     * Creates a new projectile with a starting position and target.
     * @param positionX The x coordinate of the starting position.
     * @param positionY The y coordinate of the starting position.
     * @param targetX The x coordinate of the target position.
     * @param targetY The y coordinate of the target position.
     */
    public Projectile(double positionX, double positionY, double targetX, double targetY) {
        this.positionX = positionX;
        this.positionY = positionY;
        velocity = new Vector2(targetX - positionX, targetY - positionY).normalised().mul(SPEED);
        drawOptions.setRotation(Math.atan2(targetY - positionY, targetX - positionX));
    }

    /**
     * Determines whether the projectile is within collision range of a point.
     * @param x The x position of the point.
     * @param y The y position of the point.
     * @return Whether the projectile is colliding with the point or not.
     */
    public boolean isCollidingWith(double x, double y) {
        return Math.sqrt(Math.pow(positionX - x, 2) + Math.pow(positionY - y, 2)) <= COLLISION_RANGE;
    }

    /**
     * Gets whether the projectile is inactive (off-screen) or not.
     * @return Whether the projectile is inactive or not.
     */
    public boolean isInactive() {
        return inactive;
    }

    /**
     * Updates the position of the projectile every frame according to velocity.
     */
    public void update() {
        positionX += velocity.x;
        positionY += velocity.y;

        if (positionY < 0 || positionY > Window.getHeight() || positionX < 0 || positionX > Window.getWidth()) {
            inactive = true;
            return;
        }

        image.draw(positionX, positionY, drawOptions);
    }
}
