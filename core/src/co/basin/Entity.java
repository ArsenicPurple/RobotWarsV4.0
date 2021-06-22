package co.basin;

import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Entity {
    public abstract void update();
    public abstract void render(ShapeDrawer d);
}
