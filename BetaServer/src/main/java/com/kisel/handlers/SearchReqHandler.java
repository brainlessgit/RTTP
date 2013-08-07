package com.kisel.handlers;

import com.kisel.gen.ProtoMessages;
import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.SearchReq;
import com.kisel.gen.ProtoMessages.SearchRes;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author brainless
 */
public class SearchReqHandler extends MessageHandler {

    public SearchReqHandler(MessageHandler nexHandler, DBHandler dBHandler) {
        super(nexHandler, dBHandler);
    }

    @Override
    public void handleMessage(byte[] message, OutputStream outputStream) {
        try {
            SearchReq searchReq = SearchReq.parseFrom(message);
            SearchRes.Builder searchRes = SearchRes.newBuilder();
            List<Alien> aliens;
            dbHandler.connect();
            aliens = dbHandler.search(searchReq);
            dbHandler.closeAll();
            searchRes.addAllAlien(aliens);
            searchRes.setCount(aliens.size());
            outputStream.write(searchRes.build().toByteArray());
        } catch (Exception e) {
            if (nextHandler != null) {
                nextHandler.handleMessage(message, outputStream);
            }
        }
    }
}
