package pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Interface for handling database operations
 */
public interface DataBaseHandling {
    /**
     * Inserts new user to db without checking anything
     * @param nick nick to insert
     * @param password hashed password to insert
     * @param connection database connection
     * @throws SQLException if connection with database was interrupted
     */
    void insertAccount(String nick, String password, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException;

    /**
     * Checks if user exist in DB
     * @param nick nick of player that should exist
     * @param connection database connection
     * @return true if user exists, false if not
     * @throws SQLException if connection with database was interrupted
     */
    boolean userExists(String nick, DataBaseConnection connection) throws SQLException;

    /**
     * Returns hashed password of user with given <b>nick</b>
     * @param nick nick of player that will have password checked
     * @param connection database connection
     * @return null if user don't exists
     * @throws SQLException if connection with database was interrupted
     * @throws UnsupportedEncodingException if machine don't supports UTF-8 encoding
     */
    String getHashedPassword(String nick, DataBaseConnection connection) throws SQLException, UnsupportedEncodingException;
}
