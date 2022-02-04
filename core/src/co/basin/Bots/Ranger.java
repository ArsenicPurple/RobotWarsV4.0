package co.basin.Bots;

import co.basin.*;
import co.basin.Datatypes.Entities.Robot;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Datatypes.Enums.RobotType;
import co.basin.Datatypes.Pure.ScannedRobot;
import co.basin.Managers.EntityManager;
import co.basin.Managers.TournamentManager;
import com.badlogic.gdx.graphics.Color;

public abstract class Ranger extends Robot {
    public Ranger(float x, float y, TournamentManager tm, int id, Color c) {
        super(x, y, Constants.Ranger, RobotType.RANGER, tm, id, c);
    }

    public abstract void run();
    public abstract void scannedRobot(ScannedRobot scannedRobot);
    public abstract void gotHit();
    public abstract void hitWall(RobotHitWallDirection direction);
    public abstract void bulletHitEnemy(ScannedRobot scannedRobot);
}
