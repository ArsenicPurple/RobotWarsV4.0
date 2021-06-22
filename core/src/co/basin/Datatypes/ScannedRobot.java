package co.basin.Datatypes;

import co.basin.Datatypes.RobotType;
import co.basin.Robot;
import com.badlogic.gdx.math.Vector2;

public class ScannedRobot {
    public final RobotType type;
    public final Vector2 position;
    public final float health;

    public ScannedRobot(Robot robot) {
        this.position = robot.getPosition();
        this.health = robot.getHealth();
        this.type = robot.type;
    }
}
