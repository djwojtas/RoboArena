package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseInitialization;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.PasswordHandler;
import sun.nio.cs.StandardCharsets;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.Assert.*;

public class H2DataBaseInitializationTest {
    Connection dbConnection;

    @Before
    public void setUp() throws Exception {
        Class.forName("org.h2.Driver");
        H2DataBaseConnection connection = new H2DataBaseConnection();
        dbConnection = connection.getConnection();
        PreparedStatement stmt = dbConnection.prepareStatement("DROP TABLE users");
        try {
            stmt.executeUpdate();
        } catch(Exception ignored) {}
    }

    @After
    public void tearDown() throws Exception {
        PreparedStatement stmt = dbConnection.prepareStatement("DELETE users");
        stmt.executeUpdate();
    }


    @Test
    public void executeStatements() throws Exception {
        DataBaseInitialization initalizer = new H2DataBaseInitialization();
        initalizer.executeStatements();
        PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO users (nick, password) values (?, ?)");
        stmt.setString(1, "test");
        stmt.setBytes(2, "$2a$12$ILxNFIl3NgI9KYoQ033ydOUldvGKAAbxadWmwu5M9mb67mbmNKaQ2".getBytes("UTF8"));
        stmt.executeUpdate();


        stmt = dbConnection.prepareStatement("INSERT INTO users (nick, password) values (?, ?)");
        stmt.setString(1, "test");
        //61 length instead of 60
        stmt.setBytes(2, "$2a$12$ILxNFIl3NgI9KYQ033ydOUldaavGKAAbxadWmwu5M9mb67mbmNKaQ2".getBytes("UTF8"));
        try {
            stmt.executeUpdate();
            fail();
        } catch (Exception ignored) {}
    }

}