package co.basin.Datatypes.Tournament;

import co.basin.Datatypes.Entities.Robot;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bracket {
    private final ArrayList<Robot> contenders;
    private ArrayList<Robot> losers;
    private Level currentLevel;

    public Bracket(Robot[] robots, SpriteBatch batch) throws Exception {
        if (robots.length <= 1) { throw new Exception("Insufficient Participant Exception"); }
        this.contenders = shuffle(robots);

        this.currentLevel = new Level(new ArrayList<>(contenders), batch);
    }

    public void update() {
        this.currentLevel.update();
        if (this.currentLevel.isOver()) {
            this.contenders.clear();
            this.contenders.addAll(this.currentLevel.getFinalWinners());
            this.currentLevel = new Level(new ArrayList<>(contenders), this.currentLevel.getCurrentMatch().getBatch());
        }
    }

    public void render(ShapeDrawer d) {
        this.currentLevel.render(d);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    private static ArrayList<Robot> shuffle(Robot[] robots) {
        ArrayList<Robot> preShuffledRobots = new ArrayList<>(Arrays.asList(robots));
        ArrayList<Robot> shuffledRobots = new ArrayList<>();
        Random rand = new Random();
        int size = preShuffledRobots.size();
        for (int i = 0; i < size; i++) { shuffledRobots.add(preShuffledRobots.remove(rand.nextInt(preShuffledRobots.size()))); }
        return shuffledRobots;
    }
}
