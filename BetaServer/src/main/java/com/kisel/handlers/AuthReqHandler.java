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

    public AuthReqHandler(MessageHandler nextMessageHandler) {
        super(nextMessageHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            AuthRes.Builder authRes = AuthRes.newBuilder();
            AuthReq authReq = AuthReq.parseFrom(message);
            if ("aaa".equals(authReq.getName()) && "bbb".equals(authReq.getPassword())) {
                Alien alien = Alien.newBuilder()
                        .setId(1)
                        .setName("aaa")
                        .build();
                authRes.setSuccess(true)
                        .setAlien(alien);
            } else {
                authRes.setSuccess(false);
            }
            outputStream.write(authRes.build().toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            nextHandler.handleMessage(message, outputStream);
        }
    }
}
