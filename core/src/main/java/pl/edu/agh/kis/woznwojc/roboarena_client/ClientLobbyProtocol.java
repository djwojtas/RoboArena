package pl.edu.agh.kis.woznwojc.roboarena_client;

import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ClientLobbyProtocol implements Connectable{
    private SocketChannel sChannel;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    final int timeout;
    private BufferedReader inBR;
    private PrintWriter outPW;

    public ClientLobbyProtocol(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void connect() throws IOException {
        sChannel = SocketChannel.open();
        sChannel.configureBlocking(true);
        sChannel.socket().setSoTimeout(timeout);
        if (sChannel.connect(new InetSocketAddress(ConnectionParams.serverAddress, ConnectionParams.port))) {
            inStream = new ObjectInputStream(sChannel.socket().getInputStream());
            outStream = new ObjectOutputStream(sChannel.socket().getOutputStream());
            outPW = new PrintWriter(sChannel.socket().getOutputStream(), true);
            outPW.flush();
            inBR =  new BufferedReader(new InputStreamReader(sChannel.socket().getInputStream()));
        }
    }

    @Override
    public void send(Serializable toSend) throws IOException {
        outStream.writeObject(toSend);
        outStream.flush();
        outStream.reset();
    }

    @Override
    public Object receive() throws IOException, ClassNotFoundException {
        return inStream.readObject();
    }

    @Override
    public void disconnect() throws IOException {
        sChannel.close();
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
