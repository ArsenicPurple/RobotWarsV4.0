package co.basin;

import co.basin.Datatypes.Pure.Scanner;
import co.basin.Datatypes.Enums.RobotHitWallDirection;
import co.basin.Datatypes.Enums.RobotType;
import co.basin.Datatypes.Pure.Settings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    public static final String[] robots = {
            "ExampleBot",
            "ExampleBot",
            "SoldierBot",
            "SoldierBot",
            "TurretBot",
    };

    public static final boolean isTournament = false;
    public static final int roundsPerMatch = 3;

    /**
     *
     * @return Rectangle with the default dimensions of the screen
     * @see Rectangle
     */
    public static Rectangle dimensions() { return RobotWars.getDimensions(); }

    /**
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return {@code boolean} denoting whether the point is in the base screen dimensions
     */
    public static boolean isOnScreen(float x, float y) { return dimensions().contains(x, y); }
    /**
     *
     * @param point The Vector2 containing the x and y of the point
     * @return {@code boolean} denoting whether the point is in the base screen dimensions
     * @see Vector2
     */
    public static boolean isOnScreen(Vector2 point) { return isOnScreen(point.x, point.y); }

    /**
     *
     * @param angle The angle to be calculated
     * @return {@code Vector2} containing the angles x and y components
     * @see Vector2
     */
    public static Vector2 angleVector(float angle) {
        return new Vector2(MathUtils.cos(angle * MathUtils.degRad), MathUtils.sin(angle * MathUtils.degRad));
    }

    public static Vector2 between(Vector2 v1, Vector2 v2) {
        return new Vector2((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
    }

    public static RobotHitWallDirection isRobotOnScreen(Rectangle rect) {
        RobotHitWallDirection[] dirs = { RobotHitWallDirection.NORTH, RobotHitWallDirection.SOUTH, RobotHitWallDirection.EAST, RobotHitWallDirection.WEST };
        Vector2[] points = {
                new Vector2(rect.x, rect.y).add(rect.getWidth() / 2, 0),
                new Vector2(rect.x, rect.y).add(rect.getWidth() / 2, rect.getHeight()),
                new Vector2(rect.x, rect.y).add(rect.getWidth(), rect.getHeight() / 2),
                new Vector2(rect.x, rect.y).add(0, rect.getHeight() / 2)
        };
        for (int i = 0; i < points.length; i++) {
            if (!dimensions().contains(points[i].x, points[i].y)) {
                return dirs[i];
            }
        }
        return null;
    }

    public static final Color[] colors = {
            Constants.byteColor(10,132,255, 1),
            Constants.byteColor(48, 209, 88, 1),
            Constants.byteColor(94, 92, 230, 1),
            Constants.byteColor(255, 159, 10, 1),
            Constants.byteColor(255, 55, 95, 1),
            Constants.byteColor(191, 55, 242, 1),
            Constants.byteColor(255, 69, 58, 1),
            Constants.byteColor(100, 210, 255, 1),
            Constants.byteColor(255, 214, 10, 1)
    };

    public static final Color bulletColor = Constants.byteColor(230, 255, 117, 1);
    public static final Color darkGray6 = Constants.byteColor(28, 28, 30, 1);
    public static final Color darkGray5 = Constants.byteColor(44, 44, 46, 1);
    public static final Color darkGray4 = Constants.byteColor(58, 58, 60, 1);
    public static final Color darkGray3 = Constants.byteColor(72, 72, 74, 1);
    public static final Color darkGray2 = Constants.byteColor(99, 99, 102, 1);
    public static final Color gray = Constants.byteColor(142, 142, 147, 1);
    public static final Color lightGray2 = Constants.byteColor(174, 174, 178, 1);
    public static final Color lightGray3 = Constants.byteColor(199, 199, 204, 1);
    public static final Color lightGray4 = Constants.byteColor(209, 209, 214, 1);
    public static final Color lightGray5 = Constants.byteColor(229, 229, 234, 1);
    public static final Color lightGray6 = Constants.byteColor(242, 242, 247, 1);

    /**
     *
     * Scales the values from 0-255 to 0-1
     * @return {@code Color} containing the red, green, blue, and alpha components
     * @see Color
     */
    public static Color byteColor(float r, float g, float b, float a) {
        return new Color(r / 255,g / 255,b / 255, a);
    }

    public static Settings Ranger = new Settings(
            10,
            1,
            500,
            1f,
            500,
            75,
            25
    );

    public static Settings Rusher = new Settings(
            4,
            10,
            500,
            6,
            30,
            75,
            7
    );

    public static Settings Tank = new Settings(
            3,
            2,
            300,
            2,
            160,
            200,
            14
    );

    public static Settings Tactical = new Settings(
            5,
            3,
            500,
            3,
            120,
            100,
            6
    );

    public static Scanner getScanner(RobotType type, Vector2 center) {
        switch (type) {
            case TANK: return createTankScanner(center);
            case RUSHER: return createRusherScanner(center);
            case TACTICAL: return createTacticalScanner(center);
            default: return createRangerScanner(center);
        }
    }

    private static Scanner createRangerScanner(Vector2 center) {
        return new Scanner(center, 500f, 5f);
    }

    private static Scanner createTankScanner(Vector2 center) {
        return new Scanner(center, 150f, 30f);
    }

    private static Scanner createTacticalScanner(Vector2 center) {
        return new Scanner(center, 200f, 45f);
    }

    private static Scanner createRusherScanner(Vector2 center) {
        return new Scanner(center, 100f);
    }
}
