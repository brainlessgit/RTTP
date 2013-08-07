package com.kisel.server;

import com.kisel.gen.ProtoMessages;
import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.handlers.AlienHandler;
import com.kisel.handlers.AuthReqHandler;
import com.kisel.handlers.DBHandler;
import com.kisel.handlers.SearchReqHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author brainless
 */
public class Processor extends Thread {

    private Socket s;
    private final String dbName;

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
            while ((message = reciveMessage(inputStream)) != null) {
                alienHandler.handleMessage(message, outputStream);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static byte[] reciveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[1024];
            int count = inputStream.read(buf);
            if (count > 0) {
                byte[] temp = new byte[count];
                System.arraycopy(buf, 0, temp, 0, count);
                return temp;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
