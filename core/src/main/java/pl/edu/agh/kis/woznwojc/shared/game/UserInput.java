package pl.edu.agh.kis.woznwojc.shared.game;

import java.io.Serializable;

public class UserInput implements Serializable {
    public boolean keyW = false;
    public boolean keyA = false;
    public boolean keyS = false;
    public boolean keyD = false;
    public boolean shift = false;

    public String getStringRepresentation() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(keyW ? '1' : '0');
        buffer.append(keyA ? '1' : '0');
        buffer.append(keyS ? '1' : '0');
        buffer.append(keyD ? '1' : '0');
        buffer.append(shift ? '1' : '0');

        return buffer.toString();
    }

    public void buildFromString(String buildFrom) {
        keyW = buildFrom.charAt(0) == '1';
        keyA = buildFrom.charAt(1) == '1';
        keyS = buildFrom.charAt(2) == '1';
        keyD = buildFrom.charAt(3) == '1';
        shift = buildFrom.charAt(4) == '1';
    }
}
