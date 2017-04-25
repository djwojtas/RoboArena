package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.DataBaseParameters;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class holding working (connected to database) Connection object.
 * If connection would close it will be reopened before returning Connection.
 */
public class H2DataBaseConnection implements DataBaseConnection {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    /**
     * Holds open Connection object
     */
    private Connection conn;
    /**
     * Used for checking if {@link #conn} should be reopened in {@link #getConnection()}
     */
    private boolean closed = true;

    /**
     * Initializes Connection object
     * @throws SQLException when can't connect to database
     */
    public H2DataBaseConnection() throws SQLException{
        this.conn = DriverManager.getConnection(DataBaseParameters.DB_URL, DataBaseParameters.DB_USER, DataBaseParameters.DB_PASSWD);
    }

    public Connection getConnection() throws SQLException{
        if(conn.isClosed()) {
            conn = DriverManager.getConnection(DataBaseParameters.DB_URL, DataBaseParameters.DB_USER, DataBaseParameters.DB_PASSWD);
        }
        return conn;
    }
}
