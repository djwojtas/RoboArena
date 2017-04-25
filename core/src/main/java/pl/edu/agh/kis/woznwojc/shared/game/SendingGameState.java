package pl.edu.agh.kis.woznwojc.shared.game;

import pl.edu.agh.kis.woznwojc.shared.Utils;
import pl.edu.agh.kis.woznwojc.shared.game.projectiles.LaserProjectile;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingGameState implements Serializable {
    public ArrayList<SendingUser> users;
    public ArrayList<Projectile> projectiles;

    public SendingGameState(ArrayList<SendingUser> users, ArrayList<Projectile> projectiles) {
        this.users = users;
        this.projectiles = projectiles;
    }

    public SendingGameState() {
        this.users = new ArrayList<SendingUser>();
        this.projectiles = new ArrayList<Projectile>();
    }

    public String getStringRepresentation() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder buffer = new StringBuilder();

        for(SendingUser user : users) {
            buffer.append(user.getStringRepresentation());
            buffer.append(' ');
        }
        buffer.append(' ');
        for(Projectile projectile : projectiles) {
            buffer.append(projectile.getStringRepresentation());
            buffer.append(' ');
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(buffer.toString().getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; ++i) {
            sb.append(Integer.toHexString((hash[i] & 0xFF) | 0x100).substring(1,3));
        }

        buffer.append(sb.toString());

        return buffer.toString();
    }

    public void buildFromString(String buildFrom) throws ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if(buildFrom.length() > 32) {
            byte[] bytesOfMessage = buildFrom.substring(0, buildFrom.length()-32).getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(bytesOfMessage);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; ++i) {
                sb.append(Integer.toHexString((hash[i] & 0xFF) | 0x100).substring(1,3));
            }

            if(sb.toString().equals(buildFrom.substring(buildFrom.length()-32, buildFrom.length()))) {
                users.clear();
                projectiles.clear();

                Pattern p = Pattern.compile("(P(\\w+) -?\\d+\\.\\d+ -?\\d+\\.\\d+ -?\\d+\\.\\d+) ");
                Matcher m = p.matcher(buildFrom);
                while(m.find()) {
                    if(m.group(2).equals("laser")) {
                        LaserProjectile next = new LaserProjectile();
                        next.buildFromString(m.group(1));
                        projectiles.add(next);
                    }
                }
                p = Pattern.compile("(R\\w+ \\w+ -?\\d+ -?\\d+\\.\\d+ -?\\d+\\.\\d+ -?\\d+\\.\\d+) ");
                m = p.matcher(buildFrom);
                while(m.find()) {
                    SendingUser next = new SendingUser();
                    next.buildFromString(m.group(1));
                    users.add(next);
                }
            } else {
                throw new ClassNotFoundException("Can't parse given String \"" + buildFrom + "\" to SendingGameState Object, md5 didn't match");
            }
        } else {
            throw new ClassNotFoundException("Can't parse given String \"" + buildFrom + "\" to SendingGameState Object");
        }
    }
}
