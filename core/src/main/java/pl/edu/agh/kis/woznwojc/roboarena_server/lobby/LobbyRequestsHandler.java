package pl.edu.agh.kis.woznwojc.roboarena_server.lobby;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.game.User;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LobbyRequestsHandler {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);

    public static String handleLogin(HashMap<String, String> request, Connectable connection, DataBaseConnection dbConnection) throws SQLException, IOException{
        String sessionId = LobbyHandler.signIn(request.get("nick"), request.get("password"), dbConnection);
        logger.log(Logger.Level.DEBUG, "Session ID: " + sessionId);
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("sessionId", sessionId);
        logger.log(Logger.Level.TRACE, "Sending response");
        connection.send(response);
        logger.log(Logger.Level.TRACE, "Response sent");
        return sessionId;
    }

    public static void createAccount(HashMap<String, String> request, Connectable connection, DataBaseConnection dbConnection) throws SQLException, IOException {
        boolean accountCreated = LobbyHandler.createAccount(request.get("nick"), request.get("password"), dbConnection);
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("accountCreated", accountCreated ? "true" : "false");
        connection.send(response);
    }

    public static void joinGame(HashMap<String, String> request, Connectable connection,
                                ConcurrentHashMap<String, Session> sessions,
                                CopyOnWriteArrayList<User> newUsersForGameWorker, Robot userRobot) throws SQLException, IOException {
        User joiningUser = new User(connection, sessions.get(request.get("nick")), userRobot, new UserInput());
        newUsersForGameWorker.add(joiningUser);
    }
}
