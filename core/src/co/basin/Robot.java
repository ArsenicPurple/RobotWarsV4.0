package co.basin;

import co.basin.BotAI.ExampleBot;
import co.basin.Datatypes.Scanner;
import co.basin.Datatypes.RobotHitWallDirection;
import co.basin.Datatypes.RobotType;
import co.basin.Datatypes.ScannedRobot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.TimeUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Random;

public abstract class Robot extends Entity {
    public final String name;
    private final Vector2 size;
    private final Scanner scanner;
    public RobotType type;
    private float direction;
    private Vector2 position;
    private long lastTimeFired;
    private final int maxHealth;
    private float weaponDirection;
    private final float turnSpeed;
    private final float bulletSpeed;
    private final float fireRate;
    private final float speed;
    private final int falloff;
    private final int damage;
    private final int id;
    private int health;
    private Color c;

    private boolean hitWallLastUpdate;
    private boolean scannedRobotLastUpdate;

    private final EntityManager em;
    private final ArrayList<Task> queue = new ArrayList<>();
    private Task currentTask = null;

    /**
     *
     * @param x The x coordinate of the robots starting position
     * @param y The y coordinate of the robots starting position
     * @param settings The object containing the default settings for the class that the robot extends
     * @param type The class the robot extends
     * @param em The entity manager, used for collisions etc.
     * @param name The name of the class the robot initialized from
     * @param id The internal unique number id of the robot
     * @param c The color the robot will render with unless overridden in subclass
     */
    public Robot(float x, float y, Settings settings, RobotType type, EntityManager em, String name, int id, Color c) {
        this.c = c;
        this.id = id;
        this.em = em;
        this.type = type;
        this.name = name;
        this.direction = 0;
        this.weaponDirection = 0;
        this.position = new Vector2(x, y);
        this.size = new Vector2(20, 20);
        this.scanner = Constants.getScanner(type, position);
        this.bulletSpeed = settings.bulletSpeed;
        this.turnSpeed = settings.turnSpeed;
        this.fireRate = settings.fireRate;
        this.maxHealth = settings.health;
        this.falloff = settings.falloff;
        this.health = settings.health;
        this.damage = settings.damage;
        this.speed = settings.speed;
    }

    //====================
    //     MAIN
    //====================

    @Override
    public final void update() {
        this.scan();
        if (currentTask == null) {
            if (queue.isEmpty()) { System.out.println("Queue is Empty"); return; }
            currentTask = this.shift();
        }
        if (currentTask.run(this)) { currentTask = null; }
    }

    @Override
    public final void render(ShapeDrawer d) {
        Vector2 center = getCenter();
        d.setColor(new Color(c.r, c.g, c.b, scannedRobotLastUpdate ? 0.8f : 0.1f));
        d.sector(scanner.center.x, scanner.center.y, scanner.radius, scanner.directionRad, scanner.segmentRadians);

        d.setColor(c);
        d.filledRectangle(this.position.x, this.position.y, this.size.x, this.size.y, direction * MathUtils.degRad);

        d.setColor(Constants.lightGray3);
        d.line(center, getWeaponHead(center), 10);
    }

    //====================
    //     CALLABLES
    //====================

    public final void moveAhead(int distance) {
        int ticks = Math.round((float) distance / speed);
        queue.add(new Task((robot) -> {
            Vector2 old = position.cpy();
            float radians = direction * MathUtils.degRad;
            position.add(new Vector2(MathUtils.cos(radians), MathUtils.sin(radians)).scl(speed));
            RobotHitWallDirection hitWallDirection;
            if ((hitWallDirection = Constants.isRobotOnScreen(getBody())) != null) {
                if (!hitWallLastUpdate) { this.hitWall(hitWallDirection); }
                this.position = old;
                hitWallLastUpdate = true;
            } else { hitWallLastUpdate = false; }
            return false;
        }, ticks));
    }

    public final void moveBack(int distance) {
        int ticks = Math.round((float) distance / speed);
        queue.add(new Task((robot) -> {
            Vector2 old = position.cpy();
            float radians = direction * MathUtils.degRad;
            position.add(new Vector2(MathUtils.cos(radians), MathUtils.sin(radians)).scl(speed));
            RobotHitWallDirection hitWallDirection;
            if ((hitWallDirection = Constants.isRobotOnScreen(getBody())) != null) {
                if (!hitWallLastUpdate) { this.hitWall(hitWallDirection); }
                this.position = old;
                hitWallLastUpdate = true;
            } else { hitWallLastUpdate = false; }
            return false;
        }, ticks));
    }

    public final void turnLeft(int angle) {
        int ticks = (int) ((float) angle / turnSpeed);
        queue.add(new Task((robot) -> {
            direction -= turnSpeed;
            return false;
        }, ticks));
    }

    public final void turnRight(int angle) {
        int ticks = (int) ((float) angle / turnSpeed);
        queue.add(new Task((robot) -> {
            direction += turnSpeed;
            return false;
        }, ticks));
    }

    public final void turnWeaponLeft(int angle) {
        int ticks = (int) ((float) angle / turnSpeed);
        queue.add(new Task((robot) -> {
            weaponDirection -= turnSpeed;
            return false;
        }, ticks));
    }

    public final void turnWeaponRight(int angle) {
        int ticks = (int) ((float) angle / turnSpeed);
        queue.add(new Task((robot) -> {
            weaponDirection += turnSpeed;
            return false;
        }, ticks));
    }

    public final void shoot() {
        switch (type) {
            case RUSHER: rShoot(); break;
            case TACTICAL: tShoot(); break;
            default:
                queue.add(new Task((robot) -> {
                    if (TimeUtils.millis() - lastTimeFired > fireRate) {
                        lastTimeFired = TimeUtils.millis();
                        Vector2 point = getWeaponHead();
                        em.addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, em, this));
                    }
                    return false;
                }, 1));
        }
    }

    private void rShoot() {
        Random r = new Random();
        queue.add(new Task((robot) -> {
            if (TimeUtils.millis() - lastTimeFired > fireRate) {
                lastTimeFired = TimeUtils.millis();
                Vector2 point = getWeaponHead();
                em.addEntity(new Bullet(point.x, point.y, weaponDirection + r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, em, this));
                em.addEntity(new Bullet(point.x, point.y, weaponDirection + r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, em, this));
                em.addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, em, this));
                em.addEntity(new Bullet(point.x, point.y, weaponDirection - r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, em, this));
                em.addEntity(new Bullet(point.x, point.y, weaponDirection - r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, em, this));
            }
            return false;
        }, 1));

    }

    private void tShoot() {
        Vector2 space = Constants.angleVector(weaponDirection).scl(5);
        queue.add(new Task((robot) -> {
            if (TimeUtils.millis() - lastTimeFired > fireRate) {
                lastTimeFired = TimeUtils.millis();
                Vector2 center = getCenter();
                Vector2 point = getWeaponHead(center);
                Vector2 between = Constants.between(point, center);
                em.addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, em, this));
                em.addEntity(new Bullet(between.x, between.y, weaponDirection, falloff, bulletSpeed, em, this));
                em.addEntity(new Bullet(center.x, center.y, weaponDirection, falloff, bulletSpeed, em, this));
            }
            return false;
        }, 1));
    }


    private void scan() {
        this.scanner.setCenter(getCenter());
        this.scanner.setDirectionRad(direction);
        for (Robot robot : em.getRobots(id)) {
            if (this.scanner.contains(robot.getCenter())) {
                if (!scannedRobotLastUpdate) { this.scannedRobot(new ScannedRobot(robot)); }
                scannedRobotLastUpdate = true;
                return;
            }
        }
        scannedRobotLastUpdate = false;
    }

    public void cancelActiveAction() {
        this.currentTask = null;
    }
    public void clearActionQueue() {
        this.queue.clear();
    }
    public void clearAllActions() {
        cancelActiveAction();
        clearActionQueue();
    }

    //====================
    //     UTILITY
    //====================

    private Task shift() {
        return this.queue.remove(0);
    }
    public final void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        this.health -= damage;

        if (health <= 0) {
            em.removeEntity(this);
        }
    }

    //====================
    //     GETTERS
    //====================

    public final Color getColor() { return this.c; }
    public final Scanner getScanner() { return this.scanner; }
    public final int id() { return id; }
    public final int getDamage() { return damage; }
    public final int getHealth() { return health; }
    public final int getMaxHealth() { return maxHealth; }
    public final Vector2 getPosition() { return position; }
    public final Rectangle getBody() {
        return new Rectangle(this.position.x, this.position.y, this.size.x, this.size.y);
    }
    public final Vector2 getCenter() {
        Vector2 center = Vector2.Zero;
        this.getBody().getCenter(center);
        return center;
    }
    public final Vector2 getWeaponHead(Vector2 center) {
        return center.cpy().add(Constants.angleVector(weaponDirection).scl(20));
    }
    public final Vector2 getWeaponHead() {
        return getWeaponHead(getCenter());
    }

    //====================
    //     ABSTRACTS
    //====================

    public abstract void run();
    public abstract void scannedRobot(ScannedRobot scannedRobot);
    public abstract void gotHit();
    public abstract void hitWall(RobotHitWallDirection direction);
    public abstract void bulletHitEnemy(ScannedRobot scannedRobot);

    //====================
    //     OVERRIDES
    //====================

//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Robot)) { return false; }
//        return id == ((Robot) o).id();
//    }

    @Override
    public String toString() {
        return "Robot{" +
                "id=" + id +
                '}';
    }
}
