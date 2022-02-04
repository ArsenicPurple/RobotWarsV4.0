package co.basin.Datatypes.Tournament;

import co.basin.Constants;
import co.basin.Datatypes.Entities.Robot;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;

public class Level {
    private final Match[] matches;
    private int currentMatch;

    public Level(ArrayList<Robot> contenders, SpriteBatch batch) {
        ArrayList<Match> matchBuilder = new ArrayList<>();

        int size = contenders.size();
        System.out.println("Contender Count: " + size);
        if (size % 2 != 0) {
            Robot[] matchFighters = new Robot[3];
            for (int i = 0; i < 3; i++) {
                matchFighters[i] = contenders.remove(size - (i + 1));
            }
            matchBuilder.add(new Match(matchFighters, batch));
        }

        size = contenders.size();
        System.out.println("Contender Count: " + size);
        for (int i = 0; i < size; i += 2) {
            Robot[] matchFighters = { contenders.remove(i + 1), contenders.remove(i) };
            matchBuilder.add(new Match(matchFighters, batch));
        }

        this.matches = matchBuilder.toArray(new Match[0]);
    }

    public void update() {
        Match current = this.matches[currentMatch];
        current.update();
        if (current.isOver()) {
            this.currentMatch++;
        }
    }

    public void render(ShapeDrawer d) {
        this.matches[currentMatch].render(d);
    }

    public boolean isOver() {
        for (Match match : matches) {
            if (!match.isOver()) { return false; }
        }

        return true;
    }

    public ArrayList<Robot> getFinalWinners() {
        ArrayList<Robot> winners = new ArrayList<>();
        for (Match match : matches) { winners.addAll(match.getFinalWinners()); }
        return winners;
    }

    public Match[] getMatches() {
        return matches;
    }

    public Match getCurrentMatch() {
        return this.matches[currentMatch];
    }
}
