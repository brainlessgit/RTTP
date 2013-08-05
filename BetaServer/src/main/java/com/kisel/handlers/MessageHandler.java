package com.kisel.handlers;

import java.io.OutputStream;

/**
 *
 * @author brainless
 */
public abstract class MessageHandler {

    protected MessageHandler nextHandler;

    public MessageHandler() {
    }

    public MessageHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleMessage(byte[] message, OutputStream outputStream) {
        if (nextHandler != null) {
            nextHandler.handleMessage(message, outputStream);
        }
    }
}
