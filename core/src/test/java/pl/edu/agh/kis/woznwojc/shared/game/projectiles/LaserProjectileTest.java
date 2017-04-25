package pl.edu.agh.kis.woznwojc.shared.game.projectiles;

import org.junit.Test;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import static org.junit.Assert.*;

public class LaserProjectileTest {
    @Test
    public void collisionWithRobot() throws Exception {

    }

    @Test
    public void collisionWithObstacle() throws Exception {

    }

    @Test
    public void getStringRepresentation() throws Exception {
        Projectile test = new LaserProjectile();
        try {
            test.buildFromString("Plaser 10.23 20.34 30.");
            fail();
        } catch(ClassNotFoundException ignored){}
        test.buildFromString("Plaser 10.23 20.34 30.0");
        assertEquals("Plaser 10.23 20.34 30.0", test.getStringRepresentation());
    }
}