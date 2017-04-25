package pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.PasswordHandler;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseHandling;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of {@link DataBaseHandling} with H2 database
 */
public class H2DataBaseHandling implements DataBaseHandling {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);

    /**
     * Inserts new user to db without checking anything
     * @param nick nick to insert
     * @param password hashed password to insert
     * @param connection database connection
     * @throws SQLException if connection with database was interrupted
     */
    public void insertAccount(String nick, String password, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException {
        Connection dbConnection = connection.getConnection();
        PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO users (nick, password) values (?, ?)");
        stmt.setString(1, nick);
        stmt.setBytes(2, PasswordHandler.hashPassword(password).getBytes("UTF8"));
        stmt.executeUpdate();
    }

    public boolean userExists(String nick, DataBaseConnection connection) throws SQLException {
        Connection dbConnection = connection.getConnection();
        PreparedStatement stmt = dbConnection.prepareStatement("SELECT id FROM users WHERE nick = ? LIMIT 1");
        stmt.setString(1, nick);
        ResultSet sameNickSet = stmt.executeQuery();

        return sameNickSet.next();
    }

    public String getHashedPassword(String nick, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException {
        PreparedStatement stmt = connection.getConnection().prepareStatement("SELECT id, password FROM users WHERE nick = ? LIMIT 1");
        stmt.setString(1, nick);
        ResultSet user = stmt.executeQuery();
        if(user.next()) {
            return new String(user.getBytes("PASSWORD"), "UTF-8");
        } else {
            return null;
        }
    }
}
