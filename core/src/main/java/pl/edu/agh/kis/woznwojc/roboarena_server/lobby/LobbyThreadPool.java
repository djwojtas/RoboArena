package pl.edu.agh.kis.woznwojc.roboarena_server.lobby;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.ServerLobbyProtocol;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation.H2DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.game.User;
import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyThreadPool extends Thread{
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private ServerSocketChannel ssChannel;
    private ConcurrentHashMap<String, Session> sessions;
    private final int lobbyWorkers;
    private CopyOnWriteArrayList<User> newUsersForGameWorker;

    public LobbyThreadPool(ServerSocketChannel ssChannel, ConcurrentHashMap<String, Session> sessions, int lobbyWorkers, CopyOnWriteArrayList<User> newUsersForGameWorker) {
        this.ssChannel = ssChannel;
        this.sessions = sessions;
        this.lobbyWorkers = lobbyWorkers;
        this.newUsersForGameWorker = newUsersForGameWorker;
    }

    @Override
    public void run() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(lobbyWorkers);
            while(true) {
                logger.log(Logger.Level.DEBUG, "Listening for connection");
                SocketChannel incomingConnection = ssChannel.accept();
                logger.log(Logger.Level.DEBUG, "Connection accepted, starting LobbyWorker");
                Runnable worker = new LobbyWorker(new ServerLobbyProtocol(incomingConnection, ConnectionParams.timeout), new H2DataBaseConnection(), sessions, newUsersForGameWorker);
                executor.execute(worker);
            }
        } catch (IOException e) {
            logger.log(Logger.Level.FATAL, "Error during init, exiting");
            System.exit(2);
        } catch (SQLException e) {
            logger.log(Logger.Level.FATAL, "Database connection unsuccessful");
        }
    }
}
