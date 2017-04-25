package pl.edu.agh.kis.woznwojc.roboarena_server;

public class Session {
    public String nick;
    public String sessionId;
    public long initTime;

    public Session(String nick, String sessionId, long initTime) {
        this.nick = nick;
        this.sessionId = sessionId;
        this.initTime = initTime;
    }
}
