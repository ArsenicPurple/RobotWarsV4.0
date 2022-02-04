package co.basin.Managers;

import co.basin.Datatypes.Entities.Entity;
import co.basin.Datatypes.Entities.Robot;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityManager {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<Robot> removed = new ArrayList<>();
    private Robot[] robots;

    private final ArrayList<Entity> toAdd = new ArrayList<>();
    private final ArrayList<Entity> toRemove = new ArrayList<>();

    private boolean isComplete = true;

    public void init(Robot[] robots) {
        entities.addAll(Arrays.asList(robots));
        this.robots = robots;
    }

    public void addEntity(Entity entity) {
        this.toAdd.add(entity);
        this.isComplete = false;
    }

    public void removeEntity(Entity entity) {
        if (entity instanceof Robot) {
            robots = Arrays.stream(robots).filter((robot -> !entity.equals(robot))).toArray(Robot[]::new);
            this.removed.add((Robot) entity);
        }
        this.toRemove.add(entity);
        this.isComplete = false;
    }

    public void update() {
        for (Entity entity : entities) {
            entity.update();

            if (entity instanceof Robot) {
                ((Robot) entity).run();
            }
        }
    }

    public void render(ShapeDrawer d) {
        for (Robot robot : robots) {
            robot.renderScanner(d);
        }

        for (Entity entity : entities) {
            entity.render(d);
        }
    }

    public void finish() {
        if (isComplete) {
            return;
        }

        entities.addAll(toAdd);
        entities.removeAll(toRemove);

        toAdd.clear();
        toRemove.clear();
    }

    public int countAlive() {
        return getRobots().length;
    }

    public ArrayList<Robot> getLosers() {
        return this.removed;
    }

    public Robot[] getRobots() {
        return robots;
    }
    public Robot[] getRobots(int id) { return Arrays.stream(robots).filter(robot -> robot.id() != id).toArray(Robot[]::new); }
}
