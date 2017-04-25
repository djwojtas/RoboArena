package pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Assures that opened Database connection is always returned
 */
public interface DataBaseConnection {
    /**
     * Makes sure that returned Connection object will be working one (not closed)
     * @return Opened Connection
     * @throws SQLException when can't connect to database
     */
    Connection getConnection() throws SQLException;
}
