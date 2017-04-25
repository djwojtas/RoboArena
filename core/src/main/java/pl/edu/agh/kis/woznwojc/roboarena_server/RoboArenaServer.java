package pl.edu.agh.kis.woznwojc.roboarena_server;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.DataBaseSetup;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation.H2DataBaseConnection;
import pl.edu.agh.kis.woznwojc.roboarena_server.game.GameWorker;
import pl.edu.agh.kis.woznwojc.roboarena_server.game.User;
import pl.edu.agh.kis.woznwojc.roboarena_server.lobby.LobbyThreadPool;
import pl.edu.agh.kis.woznwojc.roboarena_server.lobby.LobbyWorker;
import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of game server, creates and manages thread pools of {@link LobbyWorker} and {@link GameWorker}.
 */
public class RoboArenaServer {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private ServerSocketChannel ssChannel;
    private ConcurrentHashMap<String, Session> sessions;
    private final int lobbyWorkers = 10;

    RoboArenaServer() {
        sessions = new ConcurrentHashMap<String, Session>();
    }

    /**
     * Prepares server for launch - creates all necessary data structures and loads database driver
     * @param createTables determines if database tables should be created during initialization using {@link DataBaseSetup#executeStatements()}
     */
    private void init(boolean createTables) {
        if(createTables) {
            try {
                Class.forName("org.h2.Driver");
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
                logger.log(Logger.Level.FATAL, "Error during database class loading");
                System.exit(1);
            }
            DataBaseSetup.executeStatements();
        }
    }

    private void listenOnPort(int port) {
        try {
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(true);
            ssChannel.socket().bind(new InetSocketAddress(port));
        } catch(IOException e) {
            e.printStackTrace();
            logger.log(Logger.Level.FATAL, "Can't init ServerSocket");
            System.exit(3);
        }
    }

    /**
     * Main method of whole server, calls {@link RoboArenaServer#init(boolean)}.
     * Creates and manages thread pools of {@link LobbyWorker} and {@link GameWorker}.
     * @param args Cmd arguments
     */
    public static void main(String[] args) {
        RoboArenaServer server = new RoboArenaServer();
        server.listenOnPort(ConnectionParams.port);

        CopyOnWriteArrayList<User> newUsersForGameWorker = new CopyOnWriteArrayList<User>();

        LobbyThreadPool lobbyThreadPool = new LobbyThreadPool(server.ssChannel, server.sessions, server.lobbyWorkers, newUsersForGameWorker);
        lobbyThreadPool.start();

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new GameWorker(newUsersForGameWorker));
    }
}
