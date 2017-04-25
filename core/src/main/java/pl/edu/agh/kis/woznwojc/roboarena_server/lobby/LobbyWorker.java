package pl.edu.agh.kis.woznwojc.roboarena_server.lobby;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.roboarena_server.game.User;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LobbyWorker implements Runnable{
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private Connectable connection;
    private DataBaseConnection dbConnection;
    private ConcurrentHashMap<String, Session> sessions;
    private CopyOnWriteArrayList<User> newUsersForGameWorker;

    public LobbyWorker(Connectable connection, DataBaseConnection dbConnection, ConcurrentHashMap<String, Session> sessions, CopyOnWriteArrayList<User> newUsersForGameWorker) throws SQLException {
        this.connection = connection;
        this.dbConnection = dbConnection;
        this.sessions = sessions;
        this.newUsersForGameWorker = newUsersForGameWorker;
    }

    private boolean verifySession(HashMap<String, String> request) {
        return request.get("sessionId").equals(sessions.get(request.get("nick")).sessionId);
    }

    private boolean verifyLoginMap(HashMap<String, String> request) {
        return request.get("requestType").equals("login") &&
                request.get("nick") != null &&
                request.get("password") != null;
    }

    private boolean verifyCreateAccountMap(HashMap<String, String> request) {
        return request.get("requestType").equals("createAccount") &&
                request.get("nick") != null &&
                request.get("password") != null;
    }

    private boolean verifyJoinGameMap(HashMap<String, String> request) {
        return request.get("requestType").equals("joinGame") &&
                request.get("nick") != null &&
                request.get("sessionId") != null;
    }

    private boolean handleRequest(HashMap<String, String> request) throws IOException, SQLException, ClassNotFoundException {
        if(verifyLoginMap(request)) {
            String sessionId = LobbyRequestsHandler.handleLogin(request, connection, dbConnection);
            if(sessionId != null) {
                sessions.put(request.get("nick"), new Session(request.get("nick"), sessionId, System.currentTimeMillis()));
            }
            return true;
        } else if(verifyCreateAccountMap(request)) {
            LobbyRequestsHandler.createAccount(request, connection, dbConnection);
            return true;
        } else if(verifyJoinGameMap(request) && verifySession(request)) {
            Robot userRobot = (Robot) connection.receive();
            LobbyRequestsHandler.joinGame(request, connection, sessions, newUsersForGameWorker, userRobot);
            return false;
        } else {
            logger.log(Logger.Level.WARN, "Unknown request on " + this);
            return true;
        }
    }

    @Override
    public void run() {
        logger.log(Logger.Level.DEBUG, this + "started.");

        boolean closeConnection = true;

        try {
            connection.connect();
            HashMap<String, String> request = (HashMap<String, String>) connection.receive();
            logger.log(Logger.Level.DEBUG, "Received " + request + "on " + this);

            closeConnection = handleRequest(request);
            logger.log(Logger.Level.TRACE, "Request handled successfully on " + this);
        } catch (IOException e) {
            logger.log(Logger.Level.WARN, "Connection lost on " + this);
        } catch (SQLException e) {
            logger.log(Logger.Level.ERROR, "SQL error on " + this);
        } catch (ClassNotFoundException e) {
            logger.log(Logger.Level.ERROR, "Error during object casting " + this);
        }

        if(closeConnection) {
            try {
                connection.disconnect();
            } catch (IOException e) {
                logger.log(Logger.Level.DEBUG, "Connection interrupted during closing on " + this);
            }
        }

        logger.log(Logger.Level.DEBUG, "LobbyWorker " + this + " ended.");
    }
}
