package pl.edu.agh.kis.woznwojc.roboarena_server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;
import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ServerLobbyProtocolTest {
    private Connectable serverProtocol;
    private ServerSocketChannel ssChannel;
    static volatile boolean normalEquality;
    static volatile boolean emptyEquality;
    static volatile boolean specialEquality;
    static volatile boolean robotEquality;
    static volatile boolean userInputEquality;
    static HashMap<String, String> normal;
    static HashMap<String, String> empty;
    static HashMap<String, String> specialChars;
    static Robot robot;
    static UserInput userInput;

    {
        robot = new Robot();
        robot.x = 10;
        robot.y = 11;
        robot.acceleration = 11.2;
        robot.hp = 100;
        robot.rotation = 12.23;
        robot.rotationSpeed = 12.75;
        robot.rotationAcceleration = 123.5;
        robot.acceleration = 213.3425;
        robot.speed = 20.4;

        userInput = new UserInput();
        userInput.keyW = true;
        userInput.keyD = true;

        normal = new HashMap<String, String>();
        normal.put("test1", "testA");
        normal.put("test2", "testB");
        empty = new HashMap<String, String>();
        specialChars = new HashMap<String, String>();
        specialChars.put("     X", "\n\n  X");
        specialChars.put("\n\r\tXXó{\"źł", "");
    }

    @Before
    public void setUp() throws Exception {
        ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(true);
        ssChannel.socket().bind(new InetSocketAddress(ConnectionParams.port));
    }

    @After
    public void tearDown() throws Exception {
        serverProtocol.disconnect();
        ssChannel.close();
    }

    @Test
    public void connect() throws Exception {
        new Connector().start();
        SocketChannel connection = ssChannel.accept();
        serverProtocol = new ServerLobbyProtocol(connection, ConnectionParams.timeout);
        serverProtocol.connect();
        try {
            serverProtocol.connect();
            fail();
        } catch (SocketTimeoutException ignored) {}
    }

    @Test
    public void send() throws Exception {
        Receiver receiver = new Receiver();
        receiver.start();
        SocketChannel connection = ssChannel.accept();
        serverProtocol = new ServerLobbyProtocol(connection, ConnectionParams.timeout);
        serverProtocol.connect();

        serverProtocol.send(normal);
        serverProtocol.send(empty);
        serverProtocol.send(specialChars);
        serverProtocol.send(robot);
        serverProtocol.send(userInput);
        receiver.join();

        assertTrue(normalEquality);
        assertTrue(emptyEquality);
        assertTrue(specialEquality);
        assertTrue(robotEquality);
        assertTrue(userInputEquality);
    }

    @Test
    public void receive() throws Exception {
        Sender sender = new Sender();
        sender.start();
        SocketChannel connection = ssChannel.accept();
        serverProtocol = new ServerLobbyProtocol(connection, ConnectionParams.timeout);
        serverProtocol.connect();

        assertTrue(serverProtocol.receive().toString().equals(ServerLobbyProtocolTest.normal.toString()));
        assertTrue(serverProtocol.receive().toString().equals(ServerLobbyProtocolTest.empty.toString()));
        assertTrue(serverProtocol.receive().toString().equals(ServerLobbyProtocolTest.specialChars.toString()));
        sender.join();
    }

    @Test
    public void disconnect() throws Exception {
        new Connector().start();
        SocketChannel connection = ssChannel.accept();
        serverProtocol = new ServerLobbyProtocol(connection, ConnectionParams.timeout);
        serverProtocol.connect();
        serverProtocol.disconnect();
        serverProtocol.disconnect();
    }
}

class Connector extends Thread {
    @Override
    public void run() {
        try {
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);

            if (sChannel.connect(new InetSocketAddress(ConnectionParams.serverAddress, ConnectionParams.port))) {
                ObjectOutputStream outStream =
                        new ObjectOutputStream(sChannel.socket().getOutputStream());
                outStream.flush();
                ObjectInputStream inStream =
                        new ObjectInputStream(sChannel.socket().getInputStream());
            } else {
                fail();
            }
        } catch (IOException e) {fail();}
    }
}

class Receiver extends Thread {
    @Override
    public void run() {
        try {
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);

            if (sChannel.connect(new InetSocketAddress(ConnectionParams.serverAddress, ConnectionParams.port))) {
                ObjectOutputStream outStream =
                        new ObjectOutputStream(sChannel.socket().getOutputStream());
                outStream.flush();
                ObjectInputStream inStream =
                        new ObjectInputStream(sChannel.socket().getInputStream());


                ServerLobbyProtocolTest.normalEquality = ((HashMap<String, String>) inStream.readObject()).toString().equals(ServerLobbyProtocolTest.normal.toString());
                ServerLobbyProtocolTest.emptyEquality = ((HashMap<String, String>) inStream.readObject()).toString().equals(ServerLobbyProtocolTest.empty.toString());
                ServerLobbyProtocolTest.specialEquality = ((HashMap<String, String>) inStream.readObject()).toString().equals(ServerLobbyProtocolTest.specialChars.toString());
                Robot testRobo = (Robot) inStream.readObject();
                ServerLobbyProtocolTest.robotEquality = ServerLobbyProtocolTest.robot.x == testRobo.x &&
                        ServerLobbyProtocolTest.robot.y == testRobo.y &&
                        ServerLobbyProtocolTest.robot.acceleration == testRobo.acceleration &&
                        ServerLobbyProtocolTest.robot.hp == testRobo.hp &&
                        ServerLobbyProtocolTest.robot.rotation == testRobo.rotation &&
                        ServerLobbyProtocolTest.robot.rotationSpeed == testRobo.rotationSpeed &&
                        ServerLobbyProtocolTest.robot.rotationAcceleration == testRobo.rotationAcceleration &&
                        ServerLobbyProtocolTest.robot.acceleration == testRobo.acceleration &&
                        ServerLobbyProtocolTest.robot.speed == testRobo.speed;
                UserInput userInput = (UserInput) inStream.readObject();
                ServerLobbyProtocolTest.userInputEquality = ServerLobbyProtocolTest.userInput.keyW == userInput.keyW &&
                        ServerLobbyProtocolTest.userInput.keyA == userInput.keyA &&
                        ServerLobbyProtocolTest.userInput.keyS == userInput.keyS &&
                        ServerLobbyProtocolTest.userInput.keyD == userInput.keyD;
            } else {
                fail();
            }
        } catch (Exception ignored) {fail();}
    }
}

class Sender extends Thread {
    @Override
    public void run() {
        try {
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);

            if (sChannel.connect(new InetSocketAddress(ConnectionParams.serverAddress, ConnectionParams.port))) {
                ObjectOutputStream outStream =
                        new ObjectOutputStream(sChannel.socket().getOutputStream());
                outStream.flush();

                outStream.writeObject(ServerLobbyProtocolTest.normal);
                outStream.writeObject(ServerLobbyProtocolTest.empty);
                outStream.writeObject(ServerLobbyProtocolTest.specialChars);
            } else {
                fail();
            }
        } catch (Exception e) {fail();}
    }
}