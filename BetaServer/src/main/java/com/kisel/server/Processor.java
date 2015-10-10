package com.kisel.server;

import com.kisel.handlers.AlienHandler;
import com.kisel.handlers.AuthReqHandler;
import com.kisel.handlers.DBHandler;
import com.kisel.handlers.SearchReqHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brainless
 */
public class Processor extends Thread {

    private       Socket s;
    private final String dbName;

    private static final Logger logger = Logger.getLogger(Processor.class.getName());

    public Processor(Socket s, String dbName) {
        this.s = s;
        this.dbName = dbName;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run() {
        DBHandler dbHandler = new DBHandler(dbName);

        SearchReqHandler searchReqHandler = new SearchReqHandler(null, dbHandler);
        AuthReqHandler authReqHandler = new AuthReqHandler(searchReqHandler, dbHandler);
        AlienHandler alienHandler = new AlienHandler(authReqHandler, dbHandler);

        try {
            InputStream inputStream = s.getInputStream();
            OutputStream outputStream = s.getOutputStream();
            byte[] message;
            while ((message = receiveMessage(inputStream)) != null) {
                alienHandler.handleMessage(message, outputStream);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to handle message", e);
        }
    }

    public static byte[] receiveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[1024];
            int count = inputStream.read(buf);
            if (count > 0) {
                byte[] temp = new byte[count];
                System.arraycopy(buf, 0, temp, 0, count);
                return temp;
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read message", e);
        }
        return null;
    }
}
