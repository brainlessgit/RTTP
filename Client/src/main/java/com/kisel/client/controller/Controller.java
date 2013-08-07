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

/**
 *
 * @author brainless
 */
public class Controller {

    private List<Socket> servers;

    public Controller(Socket... servers) {
        this.servers = new ArrayList<Socket>();
        this.servers.addAll(Arrays.asList(servers));
    }

    public Controller() {
        servers = new ArrayList<Socket>();
        try {
            servers.add(new Socket("localhost", 3129));
        } catch (IOException e) {
            System.out.println("Can't connect to alpha server.");
        }
        try {
            servers.add(new Socket("localhost", 3128));
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
            int magiConst = 1000;
            int serverCount = servers.size();
            int chosenServer =
                    alien.getAddress() / (magiConst / (serverCount - 1));
            os = servers.get(chosenServer).getOutputStream();
            is = servers.get(chosenServer).getInputStream();
            os.write(toSend);
            byte[] recived = reciveMessage(is);
            AuthRes authRes = AuthRes.parseFrom(recived);
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
        AuthReq authReq = AuthReq.newBuilder()
                .setName(name)
                .setPassword(password)
                .build();
        AuthRes authRes;
        byte[] toSend = authReq.toByteArray();
        byte[] recived;
        try {
            for (Socket server : servers) {
                os = server.getOutputStream();
                is = server.getInputStream();
                os.write(toSend);
                recived = reciveMessage(is);
                authRes = AuthRes.parseFrom(recived);
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
            System.out.println(e);
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
        byte[] recived;
        OutputStream os;
        InputStream is;
        try {
            for (Socket server : servers) {
                is = server.getInputStream();
                os = server.getOutputStream();
                os.write(toSend);
                recived = reciveMessage(is);
                System.out.println("searching in, lengh: " + recived.length);
                searchRes = SearchRes.parseFrom(recived);
                for (ProtoMessages.Alien ai : searchRes.getAlienList()) {
                    Alien alien = new Alien();
                    alien.setName(ai.getName());
                    alien.setLang(ai.getLang());
                    alien.setAddress(ai.getAddress());
                    result.add(alien);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] reciveMessage(InputStream inputStream) {
        try {
            byte buf[] = new byte[8 * 1024];
            int count = inputStream.read(buf);
            if (count > 0) {
                byte[] temp = new byte[count];
                System.arraycopy(buf, 0, temp, 0, count);
                return temp;
            }
        } catch (IOException e) {
            System.out.println("recive mess ex: " + e);
        }
        return null;
    }
}
