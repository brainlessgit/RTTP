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

    public AlienHandler(MessageHandler nextMessageHandler) {
        super(nextMessageHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            Alien alien = Alien.parseFrom(message);
            AuthRes.Builder authRes = AuthRes.newBuilder();
            authRes.setSuccess(true)
                    .setAlien(alien);
//                authRes.setSuccess(false);
            outputStream.write(authRes.build().toByteArray());
            outputStream.flush();

        } catch (Exception e) {
            nextHandler.handleMessage(message, outputStream);
        }
    }
}
