package com.kisel.client.controller;

import com.kisel.aliennet.model.Alien;
import java.net.Socket;
import com.kisel.gen.ProtoMessages;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author brainless
 */
public class Controller {

    Socket alphaServer;
    Socket betaServer;

    public Controller(Socket alphaServer, Socket betaServer) {
        this.alphaServer = alphaServer;
        this.betaServer = betaServer;
    }

    public boolean Register(Alien alien) {
        OutputStream os;
        InputStream is;
        ProtoMessages.Alien alienMessage = ProtoMessages.Alien.newBuilder()
                .setId(0)
                .setName(alien.getName())
                .setPassword(alien.getPassword())
                .setLang(alien.getLang())
                .setAddress(alien.getAddress())
                .build();
        byte[] toSend = alienMessage.toByteArray();
        try {
            if (alien.getAddress() < 100) {
                os = alphaServer.getOutputStream();
                is = alphaServer.getInputStream();
            } else {
                os = betaServer.getOutputStream();
                is = betaServer.getInputStream();
            }
            os.write(toSend);
            os.flush();

            byte[] recived = reciveMessage(is);
            ProtoMessages.AuthRes authRes = ProtoMessages.AuthRes.parseFrom(recived);
            return authRes.getSuccess();
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public static byte[] reciveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[1024];
            int count = inputStream.read(buf);
            byte[] temp = new byte[count];
            System.arraycopy(buf, 0, temp, 0, count);
            return temp;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
