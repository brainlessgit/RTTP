package com.kisel.server;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 *
 * @author brainless
 */
public class DataServer {

    public static void main(String[] args) {
        try {
            ServerSocket server =
                    new ServerSocket(Integer.parseInt(args[1]), 0, InetAddress.getByName(args[0]));
            System.out.println("server is started on: " + args[0] + ":" + args[1]);
            while (true) {
                new Processor(server.accept(), args[2]);
            }
        } catch (Exception e) {
            System.out.println("init error: " + e);
        }
    }
}
