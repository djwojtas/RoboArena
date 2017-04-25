package pl.edu.agh.kis.woznwojc.shared.game;

import pl.edu.agh.kis.woznwojc.roboarena_server.Session;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingUser implements Serializable {
    public Robot robot;
    public String nick;
    public String sessionId;

    public SendingUser() {
        this.robot = new Robot();
    }

    public SendingUser(Robot robot, Session session) {
        this.robot = robot;
        this.nick = session.nick;
        this.sessionId = session.sessionId;
    }

    public String getStringRepresentation() {
        StringBuilder buffer = new StringBuilder();

        buffer.append('R');
        buffer.append(nick);
        buffer.append(' ');
        buffer.append(sessionId);
        buffer.append(' ');
        buffer.append(robot.hp);
        buffer.append(' ');
        buffer.append(robot.rotation);
        buffer.append(' ');
        buffer.append(robot.x);
        buffer.append(' ');
        buffer.append(robot.y);

        return buffer.toString();
    }

    public void buildFromString(String buildFrom) throws ClassNotFoundException{
        Pattern p = Pattern.compile("R(\\w+) (\\w+) (-?\\d+) (-?\\d+\\.\\d+) (-?\\d+\\.\\d+) (-?\\d+\\.\\d+)");
        Matcher m = p.matcher(buildFrom);
        if(m.matches()) {
            nick = m.group(1);
            sessionId = m.group(2);
            robot.hp = Integer.parseInt(m.group(3));
            robot.rotation = Double.parseDouble(m.group(4));
            robot.x = Double.parseDouble(m.group(5));
            robot.y = Double.parseDouble(m.group(6));
        } else {
            throw new ClassNotFoundException("Can't parse given String \"" + buildFrom + "\" to LaserProjectile Object");
        }
    }
}
