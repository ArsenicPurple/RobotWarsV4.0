package co.basin.Datatypes.Pure;

import co.basin.Datatypes.Entities.Robot;

import java.util.function.Predicate;

public class Task {
    public Predicate<Robot> p;
    public int ticksRemaining;

    public Task(Predicate<Robot> p, int ticks) {
        this.p = p;
        this.ticksRemaining = ticks;
    }

    public boolean run(Robot robot) {
        p.test(robot);
        ticksRemaining--;
        return ticksRemaining <= 0;
    }
}
