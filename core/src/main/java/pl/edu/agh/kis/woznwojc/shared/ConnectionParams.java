package pl.edu.agh.kis.woznwojc.shared;

import java.io.*;

/**
 * Holds connection params for both client and server
 */
public class ConnectionParams {
    /**
     * Port on which client and server should connect
     */
    public static int port;
    /**
     * Server address
     */
    public static String serverAddress;
    /**
     * Time to wait before closing connection
     */
    public static int timeout;

    static {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("server.properties"), "UTF8"));

            String sCurrentLine;
            sCurrentLine = br.readLine();
            System.out.println(sCurrentLine);
            port = Integer.parseInt(sCurrentLine);
            sCurrentLine = br.readLine();
            serverAddress = sCurrentLine;
            sCurrentLine = br.readLine();
            timeout = Integer.parseInt(sCurrentLine);

        } catch (Exception e) {
            System.out.println("Error reading properties file, loading default config");
            port = 5768;
            serverAddress = "localhost";
            timeout = 2000;
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ignored) {}
        }

    }



}
