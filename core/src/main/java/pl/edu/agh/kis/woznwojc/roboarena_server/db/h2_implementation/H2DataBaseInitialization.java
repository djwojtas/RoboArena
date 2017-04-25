package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.DataBaseParameters;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseInitialization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of {@link DataBaseInitialization} with H2 database
 */
public class H2DataBaseInitialization implements DataBaseInitialization{
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    public void executeStatements() {
        final String[] statements = new String[]{
                "CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT, nick VARCHAR(16), password VARBINARY(60))"
        };

        try {
            Class.forName("org.h2.Driver");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Logger.Level.ERROR, "Cannot load h2 database driver during database initialization. Initialization aborted.");
            return;
        }
        try {
            Connection conn = DriverManager.getConnection(DataBaseParameters.DB_URL, DataBaseParameters.DB_USER, DataBaseParameters.DB_PASSWD);
            for(String statement : statements) {
                PreparedStatement stmt = conn.prepareStatement(statement);
                try {
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    logger.log(Logger.Level.WARN, "Statement execution unsuccessful:\n" + e.getMessage());
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            logger.log(Logger.Level.ERROR, "Cannot connect to h2 database during database initialization. Initialization aborted.");
        }
    }
}
