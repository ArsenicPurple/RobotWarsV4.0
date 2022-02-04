package co.basin.Datatypes.Pure;

public class Settings {
    public final float bulletSpeed;
    public final float turnSpeed;
    public final float fireRate;
    public final float speed;
    public final int falloff;
    public final int health;
    public final int damage;

    public Settings(float bulletSpeed, float turnSpeed, float fireRate, float speed, int falloff, int health, int damage) {
        this.bulletSpeed = bulletSpeed;
        this.turnSpeed = turnSpeed;
        this.fireRate = fireRate;
        this.speed = speed;
        this.falloff = falloff;
        this.health = health;
        this.damage = damage;
    }
}
