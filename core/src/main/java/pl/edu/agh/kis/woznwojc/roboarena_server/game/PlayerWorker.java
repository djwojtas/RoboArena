package pl.edu.agh.kis.woznwojc.roboarena_server.game;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.shared.game.SendingGameState;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerWorker implements Runnable {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private SendingGameState sendingGameState;
    private final Connectable connection;
    private CopyOnWriteArrayList<User> disconnectedUsers;
    private ConcurrentHashMap<String, UserInput> userInput;
    private Set<String> busyPlayers;
    private User user;

    public PlayerWorker(SendingGameState sendingGameState, Connectable connection, CopyOnWriteArrayList<User> disconnectedUsers, User user,
                        ConcurrentHashMap<String, UserInput> userInput, Set<String> busyPlayers) {
        this.sendingGameState = sendingGameState;
        this.connection = connection;
        this.disconnectedUsers = disconnectedUsers;
        this.userInput = userInput;
        this.user = user;
        this.busyPlayers = busyPlayers;
    }

    @Override
    public void run() {
        //long deltaTime = System.currentTimeMillis();
        try {
            synchronized (connection) {
                connection.sendString(sendingGameState.getStringRepresentation());
                //logger.log(Logger.Level.DEBUG, "Delta time for sending to player \"" + user.session.nick + "\": " + (System.currentTimeMillis() - deltaTime) + "ms");
                //deltaTime = System.currentTimeMillis();
                UserInput receivedInput = new UserInput();
                receivedInput.buildFromString(connection.receiveString());
                //logger.log(Logger.Level.DEBUG, "Delta time for receiving from player \"" + user.session.nick + "\": " + (System.currentTimeMillis() - deltaTime) + "ms");
                userInput.put(user.session.nick, receivedInput);
            }
        } catch (IOException e) {
            disconnectedUsers.add(user);
            logger.log(Logger.Level.INFO, "Connection with player \"" + user.session.nick + "\" lost");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Logger.Level.WARN, "Cannot hash on this machine - md5 algorithm not found");
        }
        busyPlayers.remove(user.session.nick);

    }
}
