package co.basin.BotAI;

import co.basin.Bots.Ranger;
import co.basin.Datatypes.RobotHitWallDirection;
import co.basin.EntityManager;
import co.basin.Datatypes.ScannedRobot;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public final class ExampleBot extends Ranger {
    public ExampleBot(float x, float y, EntityManager em, String name, int id, Color c) {
        super(x, y, em, name, id, c);
    }
    public static ExampleBot init(float x, float y, EntityManager em, String name, int id, Color c) { return new ExampleBot(x, y, em, name, id, c); }

    enum State {
        SEARCHING,
        PANICKING,
        FIRING
    }

    Vector2 lastPositionSeen;

    private State currentState = State.SEARCHING;

    public Random rand = new Random();

    @Override
    public void run() {
        //shoot();
        switch (currentState) {
            case SEARCHING:
                moveAhead(100);
                turnRight(rand.nextInt(90));
                moveAhead(rand.nextInt(100));
                turnLeft(rand.nextInt(90));
                turnWeaponLeft(rand.nextInt(90));
                shoot();
                shoot();
                shoot();
                shoot();
                shoot();
                break;
            case PANICKING:
                break;
            case FIRING:
                if (lastPositionSeen == null) {
                    this.currentState = State.SEARCHING;
                }
                shoot();
                break;
            default:
        }
    }

    @Override
    public void scannedRobot(ScannedRobot scannedRobot) {
        System.out.println("Scanned Robot");
    }

    @Override
    public void gotHit() {
        System.out.println("Got Hit");
    }

    @Override
    public void hitWall(RobotHitWallDirection direction) {
        System.out.println("Hit Wall");
    }

    @Override
    public void bulletHitEnemy(ScannedRobot scannedRobot) {
        System.out.println("Bullet Hit Enemy");
        lastPositionSeen = scannedRobot.position;
    }
}
