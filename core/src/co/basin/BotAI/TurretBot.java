package co.basin.BotAI;

import co.basin.Bots.Ranger;
import co.basin.Datatypes.Entities.Robot;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Datatypes.Pure.ScannedRobot;
import co.basin.Managers.TournamentManager;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class TurretBot extends Ranger {
    public TurretBot(float x, float y, TournamentManager tm, int id, Color c) { super(x, y, tm, id, c); }
    public static Robot init(float x, float y, TournamentManager tm, int id, Color c) { return new TurretBot(x, y, tm, id, c); }

    boolean started = true;

    Random rand = new Random();

    @Override
    public String setName() {
        return "BallsMcJawls";
    }

    @Override
    public void run() {
        if (started) {
            moveAhead(100);
            started = false;
        }

        turnWeaponRight(rand.nextInt(5));
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
