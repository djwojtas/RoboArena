package pl.edu.agh.kis.woznwojc.shared.game;

import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.game.projectiles.LaserProjectile;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SendingGameStateTest {
    @Test
    public void getStringRepresentation() throws Exception {
        ArrayList<SendingUser> users = new ArrayList<SendingUser>();
        ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
        users.add(new SendingUser(new Robot(), new Session("XD", "XDFDF", 234245234)));
        users.add(new SendingUser(new Robot(), new Session("X3D", "XDFDFcccc67878", 2342434)));
        users.add(new SendingUser(new Robot(), new Session("X4554D", "XDFdsfsdfDF", 0)));
        projectiles.add(new LaserProjectile(1234, 123, 56, 780, "ROFL"));
        projectiles.add(new LaserProjectile(134, 13, 5, 70, "ROsdsdL"));
        projectiles.add(new LaserProjectile(34, 3, 546456, 0, "ROsdsdFL"));

        SendingGameState test = new SendingGameState(users, projectiles);
        System.out.println(test.getStringRepresentation());
    }

    @Test
    public void buildFromString() throws Exception {
        SendingGameState test = new SendingGameState();
        String buildFrom = "RXD XDFDF 100 0.0 0.0 0.0 RX3D XDFDFcccc67878 100 0.0 0.0 0.0 RX4554D XDFdsfsdfDF 100 0.0 0.0 0.0  Plaser 123.0 56.0 780.0 Plaser 13.0 5.0 70.0 Plaser 3.0 546456.0 0.0 188e139cc73a4e1205441fadedd32a5b";
        test.buildFromString(buildFrom);
        test.buildFromString(test.getStringRepresentation());
        assertEquals(test.getStringRepresentation(), buildFrom);
    }

}