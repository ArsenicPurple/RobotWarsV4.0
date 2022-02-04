package co.basin.Renderers;


import co.basin.Constants;
import co.basin.Datatypes.Entities.Robot;
import co.basin.Datatypes.Tournament.Match;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GuiRenderer {
    private final Match match;

    private final SpriteBatch batch;
    private final BitmapFont font = new BitmapFont();

    public GuiRenderer(Match match, SpriteBatch batch) {
        this.match = match;
        this.batch = batch;
    }

    public void renderFPS(ShapeDrawer d) {
        font.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), 100, 900);
    }

    public void renderHealthBars(ShapeDrawer d) {
        int i = 0;
        for (Robot robot : match.getManager().getRobots()) {
            font.setColor(Constants.lightGray3);
            font.draw(batch, robot.name, 0, i * 100 + 70);

            d.setColor(Constants.darkGray4);
            d.filledRectangle(0, i * 100 + 30, 100, 20);

            d.setColor(robot.getColor());
            d.filledRectangle(0, i * 100 + 30, ((float) robot.getHealth() / (float) robot.getMaxHealth()) * 100, 20);
            i++;
        }
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }
}
