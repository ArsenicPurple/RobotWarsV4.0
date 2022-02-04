package co.basin.Datatypes.Pure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class Scanner implements Shape2D {
    public boolean isCircle;

    public Vector2 center;
    public float direction;

    public final float radius;
    public final float start;
    public final float extent;

    public Scanner(Vector2 center, float radius, float start) {
        this.center = center;
        this.direction = 0;

        this.radius = radius;
        this.start = start;
        this.extent = (Math.abs(start) * 2);
    }

    public Scanner(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;

        this.start = 0;
        this.extent = 0;
        this.isCircle = true;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public void setDirectionRad(float degrees) {
        this.direction = degrees - start;
    }

    @Override
    public boolean contains(float x, float y) {
        return contains(new Vector2(x, y));
    }

    @Override
    public boolean contains(Vector2 point) {
        if (center.dst(point) > radius) { return false; }
        if (isCircle) { return true; }
        Vector2 polar = point.sub(center);
        float degrees = MathUtils.atan2(polar.y, polar.x) * MathUtils.radDeg;
        return degrees >= this.direction && degrees <= this.direction + extent;
    }
}
