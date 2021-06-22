package co.basin.Bots;

import co.basin.Constants;
import co.basin.Datatypes.RobotHitWallDirection;
import co.basin.Datatypes.RobotType;
import co.basin.EntityManager;
import co.basin.Robot;
import co.basin.Datatypes.ScannedRobot;
import com.badlogic.gdx.graphics.Color;

public abstract class Tank extends Robot {
    public Tank(float x, float y, EntityManager em, String name, int id, Color c) {
        super(x, y, Constants.Tank, RobotType.TANK, em, name, id, c);
    }

    public abstract void run();
    public abstract void scannedRobot(ScannedRobot scannedRobot);
    public abstract void gotHit();
    public abstract void hitWall(RobotHitWallDirection direction);
    public abstract void bulletHitEnemy(ScannedRobot scannedRobot);
}