package com.kisel.handlers;

import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.AuthRes;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 *
 * @author brainless
 */
public class AlienHandler extends MessageHandler {

    private static final Logger logger = Logger.getLogger(AlienHandler.class.getName());

    public AlienHandler(MessageHandler nextMessageHandler, DBHandler dbHandler) {
        super(nextMessageHandler, dbHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            Alien alien = Alien.parseFrom(message);
            AuthRes.Builder authRes = AuthRes.newBuilder();

            dbHandler.connect();
            int alienId = dbHandler.persist(alien);
            dbHandler.closeAll();

            if (alienId > 0) {
                Alien persistedAlien = Alien.newBuilder()
                        .setId(alienId)
                        .setName(alien.getName())
                        .setPassword(alien.getPassword())
                        .setLang(alien.getLang())
                        .setAddress(alien.getAddress())
                        .build();
                authRes.setSuccess(true)
                        .setAlien(persistedAlien);
            } else {
                authRes.setSuccess(false);
            }
            outputStream.write(authRes.build().toByteArray());
        } catch (Exception e) {
            if (nextHandler != null) {
                nextHandler.handleMessage(message, outputStream);
            }
        }
    }
}
