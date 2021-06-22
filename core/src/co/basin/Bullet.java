package co.basin;

import co.basin.Datatypes.ScannedRobot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.swing.*;

public class Bullet extends Entity {
    private final Vector2 position;
    private final Vector2 velocity;

    private final int initdmg;
    private int damage;

    private final int falloff;
    private int dist;

    private final EntityManager em;
    private final Robot owner;

    public Bullet(float x, float y, float direction, int falloff, float speed, EntityManager em, Robot owner) {
        this.em = em;
        this.position = new Vector2(x, y);

        this.damage = owner.getDamage();
        this.initdmg = this.damage;
        this.falloff = falloff;
        this.dist = 0;

        float radians = direction * MathUtils.degRad;
        this.velocity = new Vector2(MathUtils.cos(radians), MathUtils.sin(radians)).scl(speed);
        this.owner = owner;
    }

    public void update() {
        this.position.add(velocity);
        this.dist++;
        if (dist > falloff) { damage--;
            if (damage <= 0) { em.removeEntity(this); return; }
        }

        if (!Constants.isOnScreen(this.position)) {
            em.removeEntity(this);
            return;
        }

        for (Robot robot : em.getRobots(this.owner.id())) {
            Rectangle body = robot.getBody();
            float scalar = 1.5f;
            float ox = body.width * (scalar - 1);
            float oy = body.height * (scalar - 1);
            body.width *= scalar;
            body.height *= scalar;
            body.x -= ox / 2;
            body.y -= oy / 2;

            if (body.contains(this.position)) {
                robot.gotHit();
                //System.out.println("Robot: " + robot.id() + " Took Damage");
                robot.takeDamage(damage);
                owner.bulletHitEnemy(new ScannedRobot(robot));
                em.removeEntity(this);
                return;
            }
        }
    }

    @Override
    public void render(ShapeDrawer d) {
        d.setColor(new Color(Constants.bulletColor.r, Constants.bulletColor.g, Constants.bulletColor.b, (float) damage / (float) initdmg));
        d.filledRectangle(this.position.x, this.position.y, 5, 5);
    }

    public Robot getOwner() { return owner; }

    @Override
    public String toString() {
        return "Bullet{" +
                "owner=" + owner +
                '}';
    }
}
