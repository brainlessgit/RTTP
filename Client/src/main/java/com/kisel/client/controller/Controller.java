package com.kisel.client.controller;

import com.kisel.aliennet.model.Alien;

import java.net.Socket;

import com.kisel.gen.ProtoMessages;
import com.kisel.gen.ProtoMessages.AuthReq;
import com.kisel.gen.ProtoMessages.AuthRes;
import com.kisel.gen.ProtoMessages.SearchReq;
import com.kisel.gen.ProtoMessages.SearchRes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.kisel.client.App.*;
/**
 * @author brainless
 */
public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    private List<Socket> servers;

    private AtomicInteger roundRobinThing = new AtomicInteger();

    public Controller(Socket... servers) {
        this.servers = new ArrayList<Socket>();
        this.servers.addAll(Arrays.asList(servers));
    }

    public Controller() {
        servers = new ArrayList<Socket>();
        try {
            servers.add(new Socket(ALPHA_SERVER_HOSTNAME, ALPHA_SERVER_PORT));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to connect to alpha server", e);
        }
        try {
            servers.add(new Socket(BETA_SERVER_HOSTNAME, BETA_SERVER_PORT));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to connect to beta server", e);
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
            int chosenServer = chooseServer(alien);
            os = servers.get(chosenServer).getOutputStream();
            is = servers.get(chosenServer).getInputStream();
            os.write(toSend);
            byte[] received = receiveMessage(is);
            AuthRes authRes = AuthRes.parseFrom(received);
            return authRes.getSuccess();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to register alien", e);
        }
        return false;
    }

    private int chooseServer(Alien alien) {
        if (servers.size() < 1) {
            throw new IllegalStateException("There are no servers. Please start at least one and restart the app.");
        }
        return roundRobinThing.incrementAndGet() % servers.size();
    }

    public Alien auth(String name, String password) {
        Alien alien = null;
        OutputStream os;
        InputStream is;
        AuthReq authReq = AuthReq.newBuilder()
                .setName(name)
                .setPassword(password)
                .build();
        AuthRes authRes;
        byte[] toSend = authReq.toByteArray();
        byte[] received;
        try {
            for (Socket server : servers) {
                os = server.getOutputStream();
                is = server.getInputStream();
                os.write(toSend);
                received = receiveMessage(is);
                authRes = AuthRes.parseFrom(received);
                if (authRes.getSuccess()) {
                    alien = new Alien();
                    alien.setName(authRes.getAlien().getName());
//                alien.setPassword(password);
                    alien.setLang(authRes.getAlien().getLang());
                    alien.setAddress(authRes.getAlien().getAddress());
                    break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to auth alien", e);
        }
        return alien;
    }

    public List<Alien> search(String name) {
        List<Alien> result = new ArrayList<Alien>();
        SearchReq searchReq = SearchReq.newBuilder()
                .setName(name)
                .build();
        SearchRes searchRes;
        byte[] toSend = searchReq.toByteArray();
        byte[] received;
        OutputStream os;
        InputStream is;
        try {
            for (Socket server : servers) {
                is = server.getInputStream();
                os = server.getOutputStream();
                os.write(toSend);
                received = receiveMessage(is);

                searchRes = SearchRes.parseFrom(received);
                for (ProtoMessages.Alien ai : searchRes.getAlienList()) {
                    Alien alien = new Alien();
                    alien.setName(ai.getName());
                    alien.setLang(ai.getLang());
                    alien.setAddress(ai.getAddress());
                    result.add(alien);
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to execute search", e);
        }
        return result;
    }

    public static byte[] receiveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[8 * 1024];
            int count = inputStream.read(buf);
            if (count > 0) {
                byte[] temp = new byte[count];
                System.arraycopy(buf, 0, temp, 0, count);
                return temp;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to receiver message");
        }
        return null;
    }
}
