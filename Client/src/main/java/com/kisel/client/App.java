package com.kisel.client;

import com.kisel.aliennet.model.Alien;
import com.kisel.client.controller.Controller;
import java.net.ConnectException;
import java.net.Socket;

public class App {

    public static final String ALPHA_SERVER_HOSTNAME = "localhost";
    public static final String BETA_SERVER_HOSTNAME  = "localhost";
    public static final int    ALPHA_SERVER_PORT     = 3129;
    public static final int    BETA_SERVER_PORT      = 3128;


    public static void main(String[] args) {
        try {
            Socket sa = null;
            Socket sb = null;
            try {
                sa = new Socket(ALPHA_SERVER_HOSTNAME, ALPHA_SERVER_PORT);
            } catch (ConnectException e) {
                System.out.println("Can't connect to alpha server.");
            }
            try {
                sb = new Socket(BETA_SERVER_HOSTNAME, BETA_SERVER_PORT);
            } catch (ConnectException e) {
                System.out.println("Can't connect to beta server.");
            }
//            Controller controller = new Controller(sa, sb);
//            Alien a = new Alien();
//            a.setName("vasia2");
//            a.setLang("01");
//            a.setAddress(12);
//            a.setPassword("pass");
//            boolean b = controller.register(a);
//            System.out.println(b);
            
//            Alien fromDB = controller.auth("vasia2", "pass");
//            System.out.println(fromDB.getName());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
