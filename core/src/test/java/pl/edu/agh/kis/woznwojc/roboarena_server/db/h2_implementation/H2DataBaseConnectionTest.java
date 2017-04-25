package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;

import java.sql.PreparedStatement;

import static org.junit.Assert.*;

public class H2DataBaseConnectionTest {
    @Before
    public void setUp() throws Exception {
        Class.forName("org.h2.Driver");
        PreparedStatement stmt = new H2DataBaseConnection().getConnection().prepareStatement("DROP TABLE test_table");
        try {
            stmt.executeUpdate();
        } catch(Exception ignored) {}
        stmt = new H2DataBaseConnection().getConnection().prepareStatement("CREATE TABLE test_table(id INT NOT NULL AUTO_INCREMENT, test_row VARCHAR(16))");
        try {
            stmt.executeUpdate();
        } catch(Exception ignored) {}
    }

    @After
    public void tearDown() throws Exception {
        PreparedStatement stmt = new H2DataBaseConnection().getConnection().prepareStatement("DROP TABLE test_table");
        try {
            stmt.executeUpdate();
        } catch(Exception ignored) {}
    }

    @Test
    public void getConnection() throws Exception {
        PreparedStatement stmt = new H2DataBaseConnection().getConnection().prepareStatement("INSERT INTO test_table (test_row) VALUES ('testowe')");
        stmt.executeUpdate();
    }

}