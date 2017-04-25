package pl.edu.agh.kis.woznwojc.shared.interfaces;

import pl.edu.agh.kis.woznwojc.shared.game.Robot;

import java.util.ArrayList;

public interface Projectile {
    boolean collision(Obstacle obstacle);
    boolean collision(Robot robot);
    double getX();
    double getY();
    double getRotation();
    void move(long deltaTime);
    boolean exists();
    String getStringRepresentation();
    void buildFromString(String buildFrom) throws ClassNotFoundException;
    String getOwner();
}
