package co.basin.BotAI;

import co.basin.Bots.Tactical;
import co.basin.Datatypes.RobotHitWallDirection;
import co.basin.Datatypes.ScannedRobot;
import co.basin.EntityManager;
import com.badlogic.gdx.graphics.Color;

public class SoldierBot extends Tactical {
    public SoldierBot(float x, float y, EntityManager em, String name, int id, Color c) { super(x, y, em, name, id, c); }
    public static SoldierBot init(float x, float y, EntityManager em, String name, int id, Color c) { return new SoldierBot(x, y, em, name, id, c); }

    boolean started = true;

    @Override
    public void run() {
        if (started) {
            moveAhead(100);
            started = false;
        }

        turnWeaponRight(5);
        shoot();
    }

    @Override
    public void scannedRobot(ScannedRobot scannedRobot) {
        System.out.println("Domo");
    }

    @Override
    public void gotHit() {

    }

    @Override
    public void hitWall(RobotHitWallDirection direction) {

    }

    @Override
    public void bulletHitEnemy(ScannedRobot scannedRobot) {

    }
}

