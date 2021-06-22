package co.basin;

import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityManager {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private Robot[] robots;

    private final ArrayList<Entity> toAdd = new ArrayList<>();
    private final ArrayList<Entity> toRemove = new ArrayList<>();

    private boolean isComplete = true;

    public void init(Robot[] robots) {
        entities.addAll(Arrays.asList(robots));
        this.robots = robots;
        System.out.println(this.robots.length);
    }

    public void addEntity(Entity entity) {
        this.toAdd.add(entity);
        this.isComplete = false;
    }

    public void removeEntity(Entity entity) {
        if (entity instanceof Robot) {
            robots = Arrays.stream(robots).filter((robot -> !entity.equals(robot))).toArray(Robot[]::new);
        }
        this.toRemove.add(entity);
        this.isComplete = false;
    }

    public void update() {
        for (Entity entity : entities) {
            if (entity instanceof Bullet) {
                Bullet b = (Bullet) entity;
                b.update();
            } else if (entity instanceof Robot) {
                entity.update();
                ((Robot) entity).run();
            }
        }
    }

    public void render(ShapeDrawer d) {
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

    public Robot[] getRobots() {
        return robots;
    }
    public Robot[] getRobots(int id) { return Arrays.stream(robots).filter((robot -> robot.id() != id)).toArray(Robot[]::new); }
}
