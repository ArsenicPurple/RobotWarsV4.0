package co.basin.Datatypes.Tournament;

import co.basin.Constants;
import co.basin.Datatypes.Entities.Robot;
import co.basin.Managers.EntityManager;
import co.basin.Renderers.GuiRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;

public class Match {
    private final EntityManager manager;
    private final GuiRenderer renderer;

    private int currentRound;
    private final int rounds;
    private boolean isInBetweenRounds;

    public long time;

    public static final long previewTime = 5000;
    public static final long matchTime = 20000;

    public Match(Robot[] robots, SpriteBatch batch) {
        this.isInBetweenRounds = true;

        this.manager = new EntityManager();
        this.manager.init(robots);

        this.renderer = new GuiRenderer(this, batch);

        this.rounds = Constants.roundsPerMatch;
        this.currentRound = 0;
    }

    public void renderPreview(ShapeDrawer d) {
        float angle = 0;
        float step = 360f / manager.countAlive();
        for (Robot robot : manager.getRobots()) {
            Vector2 position = Constants.angleVector(angle);
            robot.renderPreview(d, position.x, position.y, angle + 180, 3);
            angle += step;
        }
    }

    public void render(ShapeDrawer d) {
//        if (time < previewTime) {
//            renderPreview(d);
//            return;
//        }

        manager.render(d);
        renderer.renderFPS(d);
        renderer.renderHealthBars(d);
    }

    public boolean isOver() {
        return currentRound > rounds;
    }

    public void update() {
        time++;
        manager.update();
        manager.finish();
        if (roundIsOver()) {
            finishRound();
        }
        isInBetweenRounds = false;
    }

    public boolean roundIsOver() {
        return time >= matchTime + previewTime || manager.countAlive() == 1;
    }

    private void finishRound() {
        isInBetweenRounds = true;
        Arrays.stream(manager.getRobots()).forEach((Robot::incrementWins));
        refreshFighters();
        currentRound++;
    }

    public boolean isInBetweenRounds() {
        return isInBetweenRounds;
    }

    private void refreshFighters() {
        for (Robot robot : manager.getRobots()) {
            robot.refresh();
        }
    }

    public EntityManager getManager() {
        return this.manager;
    }

    public ArrayList<Robot> getFinalWinners() {
        ArrayList<Robot> winners = new ArrayList<>();
        int wins = 0;
        for (Robot robot : manager.getRobots()) {
            if (robot.getWins() < wins) { continue; }

            if (robot.getWins() > wins)  {
                winners.clear();
                wins = robot.getWins();
            }

            winners.add(robot);
        }

        return winners;
    }

    public SpriteBatch getBatch() {
        return this.renderer.getBatch();
    }
}
