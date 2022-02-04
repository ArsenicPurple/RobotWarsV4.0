package co.basin.Datatypes.Entities;

import co.basin.Constants;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Datatypes.Enums.RobotType;
import co.basin.Datatypes.Pure.ScannedRobot;
import co.basin.Datatypes.Pure.Scanner;
import co.basin.Datatypes.Pure.Settings;
import co.basin.Datatypes.Pure.Task;
import co.basin.Managers.TournamentManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Random;

public abstract class Robot extends Entity {
    public final String name;
    private final Scanner scanner;
    private final Rectangle body;
    private final RobotType type;
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
    private final TournamentManager tm;
    private final ArrayList<Task> queue = new ArrayList<>();
    private Task currentTask = null;

    private int wins;

    /**
     *
     * @param x The x coordinate of the robots starting position
     * @param y The y coordinate of the robots starting position
     * @param settings The object containing the default settings for the class that the robot extends
     * @param type The class the robot extends
     * @param id The internal unique number id of the robot
     * @param c The color the robot will render with unless overridden in subclass
     */
    public Robot(float x, float y, Settings settings, RobotType type, TournamentManager tm, int id, Color c) {
        this.c = c;
        this.id = id;
        this.tm = tm;
        this.type = type;
        this.direction = 0;
        this.weaponDirection = 0;
        this.name = this.setName();
        this.position = new Vector2(x, y);
        this.body = new Rectangle(this.position.x - 10, this.position.y - 10, 20, 20);
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
            if (queue.isEmpty()) { return; }
            currentTask = this.shift();
        }
        if (currentTask.run(this)) { currentTask = null; }
    }

    public final void renderPreview(ShapeDrawer d, float x, float y, float rotation, float scale) {
        d.setColor(c);
        float scaledWidth = this.body.width * scale;
        float scaledHeight = this.body.height * scale;
        d.filledRectangle(x - (scaledWidth / 2f), y - (scaledHeight / 2f), scaledWidth, scaledHeight, rotation * MathUtils.degRad);

        d.setColor(Constants.lightGray3);

        Vector2 center = new Vector2(x, y);
        Vector2 weaponHead = center.cpy().add(Constants.angleVector(rotation).scl(20));
        d.line(center, weaponHead, 10);
    }

    @Override
    public final void render(ShapeDrawer d) {
        d.setColor(c);
        d.filledRectangle(this.body.x, this.body.y, this.body.width, this.body.height, direction * MathUtils.degRad);

        d.setColor(Constants.lightGray3);
        d.line(getPosition(), getWeaponHead(), 10);
    }

    public final void renderScanner(ShapeDrawer d) {
        d.setColor(new Color(c.r, c.g, c.b, scannedRobotLastUpdate ? 0.5f : 0.1f));

        if (scanner.isCircle) {
            d.filledCircle(scanner.center.x, scanner.center.y, scanner.radius);
            return;
        }

        d.sector(scanner.center.x, scanner.center.y, scanner.radius, scanner.direction * MathUtils.degRad, scanner.extent * MathUtils.degRad);
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
                        tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, tm.getCurrentEntityManager(), this));
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
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection + r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection + r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection - r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection - r.nextFloat() * 7.5f, falloff, bulletSpeed - r.nextFloat() * 2, tm.getCurrentEntityManager(), this));
            }
            return false;
        }, 1));
    }

    private void tShoot() {
        Vector2 space = Constants.angleVector(weaponDirection).scl(5);
        queue.add(new Task((robot) -> {
            if (TimeUtils.millis() - lastTimeFired > fireRate) {
                lastTimeFired = TimeUtils.millis();
                Vector2 center = getPosition();
                Vector2 point = getWeaponHead();
                Vector2 between = Constants.between(point, center);
                tm.getCurrentEntityManager().addEntity(new Bullet(point.x, point.y, weaponDirection, falloff, bulletSpeed, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(between.x, between.y, weaponDirection, falloff, bulletSpeed, tm.getCurrentEntityManager(), this));
                tm.getCurrentEntityManager().addEntity(new Bullet(center.x, center.y, weaponDirection, falloff, bulletSpeed, tm.getCurrentEntityManager(), this));
            }
            return false;
        }, 1));
    }

    private void scan() {
        this.scanner.setCenter(getPosition());
        this.scanner.setDirectionRad(direction);

        Robot[] robotsExcept = tm.getCurrentEntityManager().getRobots(id);
        for (Robot robot : robotsExcept) {
            if (this.scanner.contains(robot.getPosition())) {
                if (!this.scannedRobotLastUpdate) {
                    this.scannedRobot(new ScannedRobot(robot));
                }
                this.scannedRobotLastUpdate = true;
                return;
            }
        }
        this.scannedRobotLastUpdate = false;
    }

    public final void refresh() {
        this.cancelAllActions();

        if (this.tm.isInBetweenRounds()) {
            this.health = this.maxHealth;
        }
    }

    public final void incrementWins() {
        if (this.tm.isInBetweenRounds())  {
            this.wins++;
        }
    }

    public void cancelActiveAction() {
        this.currentTask = null;
    }
    public void clearActionQueue() {
        this.queue.clear();
    }
    public void cancelAllActions() {
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
            tm.getCurrentEntityManager().removeEntity(this);
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
    public final Vector2 getPosition() { return position.cpy(); }
    public final Rectangle getBody() { this.body.setPosition(this.position.cpy().sub(10, 10)); return new Rectangle(body); }
    public final Vector2 getWeaponHead() { return this.getPosition().add(Constants.angleVector(weaponDirection).scl(20)); }
    public final RobotType getType() { return this.type; }
    public final int getWins() { return this.wins; }

    //====================
    //     ABSTRACTS
    //====================

    public abstract void run();
    public abstract void scannedRobot(ScannedRobot scannedRobot);
    public abstract void gotHit();
    public abstract void hitWall(RobotHitWallDirection direction);
    public abstract void bulletHitEnemy(ScannedRobot scannedRobot);

    public abstract String setName();

    //====================
    //     OVERRIDES
    //====================

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Robot)) { return false; }
        return id == ((Robot) o).id();
    }

    @Override
    public String toString() {
        return "Robot{" +
                "id=" + id +
                '}';
    }
}
