package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.DataBaseSetup;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseHandling;

import java.sql.PreparedStatement;

import static org.junit.Assert.*;

public class H2DataBaseHandlingTest {
    DataBaseConnection connection;
    DataBaseHandling handler = new H2DataBaseHandling();

    @Before
    public void setUp() throws Exception {
        Class.forName("org.h2.Driver");
        DataBaseSetup.executeStatements();
        connection = new H2DataBaseConnection();
        PreparedStatement stmt = connection.getConnection().prepareStatement("DELETE users");
        stmt.executeUpdate();
    }

    @Test
    public void insertAccountAndUserExists() throws Exception {
        assertFalse(handler.userExists("test_nick", connection));
        handler.insertAccount("test_nick", "test_password", connection);
        assertTrue(handler.userExists("test_nick", connection));
    }

    @Test
    public void getHashedPassword() throws Exception {
        assertNull(handler.getHashedPassword("test_nick", connection));
        handler.insertAccount("test_nick", "test_password", connection);
        assertNotNull(handler.getHashedPassword("test_nick", connection));
    }
}