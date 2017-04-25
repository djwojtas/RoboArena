package pl.edu.agh.kis.woznwojc.shared.interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Implementing <b>Connectable</b> interface allows class to connect to target and send/receive <b>HashMap</b> with simple API
 */
public interface Connectable {
    /**
     * Connects to target
     * @throws IOException if an I/O error occurs
     */
    void connect() throws IOException;

    /**
     * Sends <b>Serializable</b> object to receiver
     * @param toSend <b>Serializable</b> that will be sent to receiver
     * @throws IOException if an I/O error occurs
     */
    void send(Serializable toSend) throws IOException;

    /**
     * Receives <b>Serializable</b> object from receiver
     * @return Received <b>Object</b>
     * @throws IOException if an I/O error occurs
     */
    Object receive() throws IOException, ClassNotFoundException;

    /**
     * Disconnects from target
     * @throws IOException if an I/O error occurs
     */
    void disconnect() throws IOException;

    void sendString(String toSend) throws IOException;

    String receiveString() throws IOException;
}
