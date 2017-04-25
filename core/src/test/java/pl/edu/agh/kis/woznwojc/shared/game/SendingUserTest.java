package pl.edu.agh.kis.woznwojc.shared.game;

import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.game.projectiles.LaserProjectile;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import static org.junit.Assert.*;

public class SendingUserTest {
    @Test
    public void getStringRepresentation() throws Exception {
        SendingUser testsubject = new SendingUser(new Robot(), new Session("testnick", "123456789testId", 1234567789));
        assertEquals("Rtestnick 123456789testId 100 0.0 0.0 0.0", testsubject.getStringRepresentation());
    }

    @Test
    public void buildFromString() throws Exception {
        SendingUser test = new SendingUser();
        try {
            test.buildFromString("Rtestnick 123456789testId 100 0.0 0.0 0.");
            fail();
        } catch(ClassNotFoundException ignored){}
        test.buildFromString("Rtestnick 123456789testId 100 0.0 0.0 0.0");
        assertEquals("Rtestnick 123456789testId 100 0.0 0.0 0.0", test.getStringRepresentation());
    }
}