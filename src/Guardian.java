import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.ArrayList;

/**
 * An entity which fires projectiles at enemies.
 */
public class Guardian {
    private final static int POSITION_X = 800;
    private final static int POSITION_Y = 600;
    private final static String IMAGE_FILE = "res/guardian.png";

    private final Image image;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Creates a new guardian at (800, 600).
     */
    public Guardian() {
        image = new Image(IMAGE_FILE);
    }

    private Enemy getClosestEnemy(ArrayList<Enemy> enemies) {
        Enemy closestEnemy = null;
        double closestDistance = -1;

        for (Enemy enemy : enemies) {
            Point enemyPosition = enemy.getPosition();
            double distance = Math.sqrt(Math.pow(POSITION_X - enemyPosition.x, 2)
                    + Math.pow(POSITION_Y - enemyPosition.y, 2));

            if (closestDistance == -1 || distance < closestDistance) {
                closestEnemy = enemy;
                closestDistance = distance;
            }
        }

        return closestEnemy;
    }

    private void fireProjectile(Enemy enemy) {
        Point enemyPosition = enemy.getPosition();
        projectiles.add(new Projectile(POSITION_X, POSITION_Y, enemyPosition.x, enemyPosition.y));
    }

    /**
     * Renders the guardian and creates new projectiles when needed.
     * @param input The bagel input object.
     * @param enemies The enemies currently on the screen.
     */
    public void update(Input input, ArrayList<Enemy> enemies) {
        image.draw(POSITION_X, POSITION_Y);

        // Fire projectiles when shift key is pressed.
        if (input.wasPressed(Keys.LEFT_SHIFT)) {
            Enemy closestEnemy = getClosestEnemy(enemies);
            if (closestEnemy != null) {
                fireProjectile(closestEnemy);
            }
        }

        for (Projectile projectile : projectiles) {
            projectile.update();
        }

        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);

            if (projectile.isInactive()) {
                // Projectile went off-screen.
                projectiles.remove(i);
                continue;
            }

            projectile.update();

            // Check for projectile collisions with enemies
            for (Enemy enemy : enemies) {
                Point enemyPosition = enemy.getPosition();
                if (projectile.isCollidingWith(enemyPosition.x, enemyPosition.y)) {
                    enemy.deactivate();
                }
            }
        }
    }
}
