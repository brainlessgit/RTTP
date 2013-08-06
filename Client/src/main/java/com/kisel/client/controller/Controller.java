package com.kisel.client.controller;

import com.kisel.aliennet.model.Alien;
import java.net.Socket;
import com.kisel.gen.ProtoMessages;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;

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

    public Controller() {
        try {
            alphaServer = new Socket("localhost", 3129);
        } catch (IOException e) {
            System.out.println("Can't connect to alpha server.");
        }
        try {
            betaServer = new Socket("localhost", 3128);
        } catch (IOException e) {
            System.out.println("Can't connect to beta server.");
        }

    }

    public boolean register(Alien alien) {
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
            byte[] recived = reciveMessage(is);
            ProtoMessages.AuthRes authRes = ProtoMessages.AuthRes.parseFrom(recived);
            return authRes.getSuccess();
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public Alien auth(String name, String password) {
        Alien alien = null;
        OutputStream os;
        InputStream is;
        ProtoMessages.AuthReq authReq = ProtoMessages.AuthReq.newBuilder()
                .setName(name)
                .setPassword(password)
                .build();
        ProtoMessages.AuthRes authRes;
        byte[] toSend = authReq.toByteArray();
        byte[] recived;
        try {
            os = alphaServer.getOutputStream();
            is = alphaServer.getInputStream();
            os.write(toSend);
            recived = reciveMessage(is);
            authRes = ProtoMessages.AuthRes.parseFrom(recived);
            if (!authRes.getSuccess()) {
                os = betaServer.getOutputStream();
                is = betaServer.getInputStream();
                os.write(toSend);
                recived = reciveMessage(is);
                authRes = ProtoMessages.AuthRes.parseFrom(recived);
            }
            if (authRes.getSuccess()) {
                alien = new Alien();
                alien.setName(authRes.getAlien().getName());
//                alien.setPassword(password);
                alien.setLang(authRes.getAlien().getLang());
                alien.setAddress(authRes.getAlien().getAddress());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return alien;
    }

    public static byte[] reciveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[1024];
            int count = inputStream.read(buf);
            byte[] temp = new byte[count];
            System.arraycopy(buf, 0, temp, 0, count);
            return temp;
        } catch (IOException e) {
            System.out.println("recive mess ex: " + e);
        }
        return null;
    }
}
