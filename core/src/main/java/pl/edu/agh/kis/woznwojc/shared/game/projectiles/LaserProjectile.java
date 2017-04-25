package pl.edu.agh.kis.woznwojc.shared.game.projectiles;

import pl.edu.agh.kis.woznwojc.shared.Utils;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Obstacle;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;
import pl.edu.agh.kis.woznwojc.shared.interfaces.RoboSlot;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LaserProjectile implements Projectile {
    private int TTL;
    private double x;
    private double y;
    private double rotation;
    private String owner;
    private boolean exists = true;

    public LaserProjectile() {}

    public LaserProjectile(int TTL, double x, double y, double rotation, String owner) {
        this.TTL = TTL;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.owner = owner;
    }

    @Override
    public boolean collision(Obstacle obstacle) {
        return false;
    }

    @Override
    public boolean collision(Robot robot) {
        return Math.sqrt((robot.x-x)*(robot.x-x)+(robot.y-y)*(robot.y-y)) < 101 + 25;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void move(long deltaTime) {
        x += deltaTime*Math.cos(rotation);
        y += deltaTime*Math.sin(rotation);
        TTL -= deltaTime;
        if(TTL <= 0) {
            exists = false;
        }
    }

    @Override
    public boolean exists() {
        return exists;
    }

    @Override
    public String getStringRepresentation() {
        StringBuilder buffer = new StringBuilder();

        buffer.append('P');
        buffer.append("laser ");
        buffer.append(x);
        buffer.append(' ');
        buffer.append(y);
        buffer.append(' ');
        buffer.append(rotation);

        return buffer.toString();
    }

    @Override
    public void buildFromString(String buildFrom) throws ClassNotFoundException{
        Pattern p = Pattern.compile("Plaser (-?\\d+\\.\\d+) (-?\\d+\\.\\d+) (-?\\d+\\.\\d+)");
        Matcher m = p.matcher(buildFrom);
        if(m.matches()) {
            x = Double.parseDouble(m.group(1));
            y = Double.parseDouble(m.group(2));
            rotation = Double.parseDouble(m.group(3));
        } else {
            throw new ClassNotFoundException("Can't parse given String \"" + buildFrom + "\" to LaserProjectile Object");
        }
    }

    @Override
    public String getOwner() {
        return owner;
    }


}
