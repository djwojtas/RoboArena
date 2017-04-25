package pl.edu.agh.kis.woznwojc.roboarena_server.game;

import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

public class User {
    public Connectable connection;
    public Robot robot;
    public Session session;
    public UserInput userInput;
    public long lastShoot = 0;

    public User(Connectable connection, Session session, Robot robot, UserInput userInput) {
        this.connection = connection;
        this.session = session;
        this.robot = robot;
        this.userInput = userInput;
    }
}
