package com.kisel.handlers;

import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.AuthReq;
import com.kisel.gen.ProtoMessages.AuthRes;
import java.io.OutputStream;

/**
 *
 * @author brainless
 */
public class AuthReqHandler extends MessageHandler {

    public AuthReqHandler(MessageHandler nextMessageHandler, DBHandler dbHandler) {
        super(nextMessageHandler, dbHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            AuthRes.Builder authRes = AuthRes.newBuilder();
            AuthReq authReq = AuthReq.parseFrom(message);
            dbHandler.connect();
            Alien alien = dbHandler.auth(authReq);
            dbHandler.closeAll();
            if (alien != null) {
                authRes.setSuccess(true)
                        .setAlien(alien);
            } else {
                authRes.setSuccess(false);
            }
            outputStream.write(authRes.build().toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            if (nextHandler != null) {
                nextHandler.handleMessage(message, outputStream);
            }
        }
    }
}
