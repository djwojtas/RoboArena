package pl.edu.agh.kis.woznwojc.roboarena_server;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

/**
 * Connectable implementation for game server.
 */
public class ServerLobbyProtocol implements Connectable {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    /**
     * Client-side socket after accepting incoming connection
     */
    private SocketChannel sChannel;
    /**
     * Output stream used to send serialized objects
     */
    private ObjectOutputStream outStream;
    /**
     * input stream used to receive serialized hashmaps in {@link #receive()}
     */
    private ObjectInputStream inStream;
    private BufferedReader inBR;
    private PrintWriter outPW;

    /**
     * Opens ServerSocketChannel and binds it to defined port
     * @throws IOException if an I/O error occurs
     */
    public ServerLobbyProtocol(SocketChannel sChannel, int timeout) throws IOException {
        this.sChannel = sChannel;
        sChannel.configureBlocking(true);
        sChannel.socket().setSoTimeout(timeout);
    }

    @Override
    public synchronized void connect() throws IOException {
        logger.log(Logger.Level.TRACE, "Connection accepted");
        outStream = new ObjectOutputStream(sChannel.socket().getOutputStream());
        outStream.flush();
        inStream =  new ObjectInputStream(sChannel.socket().getInputStream());
        outPW = new PrintWriter(sChannel.socket().getOutputStream(), true);
        outPW.flush();
        inBR =  new BufferedReader(new InputStreamReader(sChannel.socket().getInputStream()));
        logger.log(Logger.Level.TRACE, "Streams created");
    }

    @Override
    public synchronized void send(Serializable toSend) throws IOException {
        outStream.writeObject(toSend);
        outStream.flush();
        outStream.reset();
    }

    @Override
    public synchronized Object receive() throws  IOException, ClassNotFoundException {
        return inStream.readObject();
    }

    @Override
    public synchronized void disconnect() {
        try {
            sChannel.close();
            logger.log(Logger.Level.TRACE, "Connection closed");
        } catch (IOException e) {
            logger.log(Logger.Level.WARN, "IO Exception occurred during connection closing");
        } catch (NullPointerException e) {
            logger.log(Logger.Level.WARN, "Closing before using connect()");
        }
    }

    @Override
    public void sendString(String toSend) throws IOException {
        outPW.println(toSend);
    }

    @Override
    public String receiveString() throws IOException {
        return inBR.readLine();
    }
}
