package co.basin.Managers;

import co.basin.Datatypes.Entities.Robot;
import co.basin.Datatypes.Tournament.Bracket;
import co.basin.Datatypes.Tournament.Match;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class TournamentManager {
    public Bracket bracket;

    public void init(Robot[] robots, SpriteBatch batch) throws Exception {
        this.bracket = new Bracket(robots, batch);
    }

    public void update() {
        this.bracket.update();
    }

    public void render(ShapeDrawer d) {
        this.bracket.render(d);
    }

    public EntityManager getCurrentEntityManager() {
        return this.getCurrentMatch().getManager();
    }

    public Match getCurrentMatch() {
        return this.bracket.getCurrentLevel().getCurrentMatch();
    }

    public boolean isInBetweenRounds() {
        return getCurrentMatch().isInBetweenRounds();
    }
}
