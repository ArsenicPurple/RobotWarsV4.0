package co.basin.Datatypes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class Scanner implements Shape2D {
    public Vector2 center;
    public float directionRad;

    public final float radius;
    public final float startRadians;
    public final float segmentRadians;

    public Scanner(Vector2 center, float radius, float startAngle) {
        this.center = center;
        this.directionRad = 0;

        this.radius = radius;
        this.startRadians = startAngle * MathUtils.degRad;
        this.segmentRadians = (Math.abs(startAngle) * 2) * MathUtils.degRad;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public void setDirectionRad(float degrees) {
        this.directionRad = degrees * MathUtils.degRad - startRadians;
    }

    @Override
    public boolean contains(float x, float y) {
        return contains(new Vector2(x, y));
    }

    @Override
    public boolean contains(Vector2 point) {
        Vector2 polar = point.sub(center);
        if (polar.len() > radius) {
            return false;
        }

        float radians = (float) Math.atan(polar.y / polar.x) * MathUtils.degRad;
        return radians >= directionRad && radians <= directionRad + segmentRadians;
    }
}
