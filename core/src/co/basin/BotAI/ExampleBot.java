package co.basin.BotAI;

import co.basin.Bots.Rusher;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Managers.EntityManager;
import co.basin.Datatypes.Pure.ScannedRobot;
import co.basin.Managers.TournamentManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public final class ExampleBot extends Rusher {
    public ExampleBot(float x, float y, TournamentManager tm, int id, Color c) { super(x, y, tm, id, c); }
    public static ExampleBot init(float x, float y, TournamentManager tm, int id, Color c) { return new ExampleBot(x, y, tm, id, c); }

    @Override
    public String setName() {
        return "NinjaBaby";
    }

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
    }

    @Override
    public void gotHit() {

    }

    @Override
    public void hitWall(RobotHitWallDirection direction) {

    }

    @Override
    public void bulletHitEnemy(ScannedRobot scannedRobot) {
        lastPositionSeen = scannedRobot.position;
    }
}
