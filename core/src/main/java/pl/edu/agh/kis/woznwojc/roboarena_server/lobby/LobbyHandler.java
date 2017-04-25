package pl.edu.agh.kis.woznwojc.roboarena_server.lobby;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.DataBaseHandler;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.PasswordHandler;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;

public class LobbyHandler {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    /**
     * Static <b>SecureRandom</b>  because of high initialization cost
     */
    private static final SecureRandom rand = new SecureRandom();

    /**
     * Checks if account with given nickname already exists, if not - creates account with given arguments
     * @param nick nick to insert
     * @param password hashed password to insert
     * @param connection database connection
     * @return true if account was created, false if not because user with given nick exists
     * @throws SQLException if connection with database was interrupted
     */
    public synchronized static boolean createAccount(String nick, String password, DataBaseConnection connection) throws SQLException
    {
        if(DataBaseHandler.userExists(nick, connection)) {
            logger.log(Logger.Level.DEBUG, "Account already exists with nick " + nick);
            return false;
        } else {
            try {
                DataBaseHandler.insertAccount(nick, password, connection);
                logger.log(Logger.Level.DEBUG, "Account created with nick " + nick);
            } catch(UnsupportedEncodingException e) {
                logger.log(Logger.Level.ERROR, "System don't supports UTF8");
            }
            return true;
        }
    }

    /**
     * Checks if user with given <b>nick</b> has the same <b>password</b> as given one.
     * If so, returns session id, otherwise returns null
     * @param nick nick of player that wants to login
     * @param password password of player that wants to login
     * @param connection database connection
     * @return session id if user with given <b>nick</b> exists and has the same <b>password</b>, otherwise returns null
     * @throws SQLException if connection with database was interrupted
     */
    public static String signIn(String nick, String password, DataBaseConnection connection) throws SQLException
    {
        try {
            String hashedPassword = DataBaseHandler.getHashedPassword(nick, connection);
            if(hashedPassword != null) {
                logger.log(Logger.Level.DEBUG, "Account asking for logging with nick " + nick);
            } else {
                logger.log(Logger.Level.DEBUG, "No account with nick " + nick);
                return null;
            }

            String sessionId = checkPassword(password, hashedPassword);
            if(sessionId != null) {
                logger.log(Logger.Level.DEBUG, "Account logged in with nick " + nick);
                return sessionId;
            } else {
                logger.log(Logger.Level.DEBUG, "Wrong password while logging in with nick " + nick);
                return null;
            }
        } catch(UnsupportedEncodingException e) {
            logger.log(Logger.Level.ERROR, "System don't supports UTF8");
            return null;
        }
    }

    /**
     * Compares plain password to hashed one and if they match returns session id
     * @param password plaintext password
     * @param hashedPassword hashed password
     * @return Session id if passwords are matching, null if not
     * @throws UnsupportedEncodingException if machine don't supports UTF-8 encoding
     */
    private static String checkPassword(String password, String hashedPassword) throws UnsupportedEncodingException {
        if(PasswordHandler.comparePassword(password, new String(hashedPassword.getBytes("UTF-8"), "UTF-8"))) {
            return new BigInteger(130, rand).toString(32);
        }
        return null;
    }
}
