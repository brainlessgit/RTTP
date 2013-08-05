package com.kisel.server;

import com.kisel.handlers.AlienHandler;
import com.kisel.handlers.AuthReqHandler;
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

    public Processor(Socket s) {
        this.s = s;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run() {
        AuthReqHandler authReqHandler = new AuthReqHandler(null);
        AlienHandler alienHandler = new AlienHandler(authReqHandler);
        try {
            InputStream inputStream = s.getInputStream();
            OutputStream outputStream = s.getOutputStream();
            byte[] message;
            while ((message = reciveMessage(inputStream)) != null) {
                alienHandler.handleMessage(message, outputStream);
            }
        } catch (IOException e) {
        }
    }

    public static byte[] reciveMessage(InputStream inpustream) {
        try {
            byte len[] = new byte[1024];
            int count = inpustream.read(len);
            byte[] temp = new byte[count];
            System.arraycopy(len, 0, temp, 0, count);
            return temp;
        } catch (Exception e) {
            System.out.println("recvMsg() occur exception!" + e.toString());
        }
        return new byte[0];
    }
}
