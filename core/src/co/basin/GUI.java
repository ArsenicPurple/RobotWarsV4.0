package co.basin;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GUI {
    private EntityManager em;

    private SpriteBatch batch;
    private BitmapFont font = new BitmapFont();

    public GUI(EntityManager em, SpriteBatch batch) {
        this.em = em;
        this.batch = batch;
    }

    public void render(ShapeDrawer d) {
        font.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), 100, 900);

        for (int i = 0; i < em.getRobots().length; i++) {
            //font.draw(batch, robots[i].name, robots[i].getPosition().x - (robots[i].name.length()), robots[i].getPosition().y);

            font.setColor(Constants.lightGray3);
            font.draw(batch, em.getRobots()[i].name, 0, i * 100 + 70);

            d.setColor(Constants.darkGray4);
            d.filledRectangle(0, i * 100 + 30, 100, 20);

            d.setColor(em.getRobots()[i].getColor());
            d.filledRectangle(0, i * 100 + 30, ((float) em.getRobots()[i].getHealth() / (float) em.getRobots()[i].getMaxHealth()) * 100, 20);
        }
    }
}
