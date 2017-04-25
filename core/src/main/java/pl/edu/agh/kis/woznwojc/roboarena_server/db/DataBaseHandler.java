package pl.edu.agh.kis.woznwojc.roboarena_server.db;

import pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation.H2DataBaseHandling;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseHandling;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Wrapper around {@link DataBaseHandling} object to make methods static
 */
public class DataBaseHandler {
    /**
     * {@link DataBaseHandling} object used to do DB operations
     */
    private static final DataBaseHandling dbHandler = new H2DataBaseHandling();

    /**
     * Inserts new user to db without checking anything
     * @param nick nick to insert
     * @param password hashed password to insert
     * @param connection database connection
     * @throws SQLException if connection with database was interrupted
     */
    public static void insertAccount(String nick, String password, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException
    {
        dbHandler.insertAccount(nick, password, connection);
    }

    /**
     * Checks if user exist in DB
     * @param nick nick of player that should exist
     * @param connection database connection
     * @return true if user exists, false if not
     * @throws SQLException if connection with database was interrupted
     */
    public static boolean userExists(String nick, DataBaseConnection connection) throws SQLException {
        return dbHandler.userExists(nick, connection);
    }

    /**
     * Returns hashed password of user with given <b>nick</b>
     * @param nick nick of player that will have password checked
     * @param connection database connection
     * @return null if user don't exists
     * @throws SQLException if connection with database was interrupted
     * @throws UnsupportedEncodingException if machine don't supports UTF-8 encoding
     */
    public static String getHashedPassword(String nick, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException {
        return dbHandler.getHashedPassword(nick, connection);
    }
}