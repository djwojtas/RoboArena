package pl.edu.agh.kis.woznwojc.shared.game;

import java.io.Serializable;

public class Robot implements Serializable {
    public double airResistance = 0.01;
    public int hp = 100;
    public double x = 0;
    public double y = 0;
    public double speed = 0;
    public double rotation = 0;
    public double rotationSpeed = 0;

    public double acceleration = 0;
    public double rotationAcceleration = 0;
}
