package com.kisel.handlers;

import com.kisel.gen.ProtoMessages;
import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.AuthRes;
import java.io.OutputStream;

/**
 *
 * @author brainless
 */
public class AlienHandler extends MessageHandler {

    public AlienHandler(MessageHandler nextMessageHandler, DBHandler dbHandler) {
        super(nextMessageHandler, dbHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            Alien alien = Alien.parseFrom(message);
            AuthRes.Builder authRes = AuthRes.newBuilder();
            int res;
            dbHandler.connect();
            res = dbHandler.persist(alien);
            dbHandler.closeAll();

            if (res > 0) {
                Alien savedAlien = Alien.newBuilder()
                        .setId(res)
                        .setName(alien.getName())
                        .setPassword(alien.getPassword())
                        .setLang(alien.getLang())
                        .setAddress(alien.getAddress())
                        .build();
                authRes.setSuccess(true)
                        .setAlien(savedAlien);
            } else {
                authRes.setSuccess(false);
            }
            outputStream.write(authRes.build().toByteArray());
            outputStream.flush();
            System.out.println("ALIEN SAVED");
        } catch (Exception e) {
            if (nextHandler != null) {
                nextHandler.handleMessage(message, outputStream);
            }
        }
    }
}
