package co.basin.BotAI;

import co.basin.Bots.Tactical;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Datatypes.Pure.ScannedRobot;
import co.basin.Managers.EntityManager;
import co.basin.Managers.TournamentManager;
import com.badlogic.gdx.graphics.Color;

public class SoldierBot extends Tactical {
    public SoldierBot(float x, float y, TournamentManager tm, int id, Color c) { super(x, y, tm, id, c); }
    public static SoldierBot init(float x, float y, TournamentManager tm, int id, Color c) { return new SoldierBot(x, y, tm, id, c); }

    boolean started = true;

    @Override
    public String setName() {
        return "SugSugAwesome";
    }

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

