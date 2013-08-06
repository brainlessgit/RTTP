package com.kisel.client;

import com.kisel.aliennet.model.Alien;
import com.kisel.client.controller.Controller;
import java.net.ConnectException;
import java.net.Socket;

public class App {

    public static void main(String[] args) {
        try {
            Socket sa = null;
            Socket sb = null;
            try {
                sa = new Socket("localhost", 3129);
            } catch (ConnectException e) {
                System.out.println("Can't connect to alpha server.");
            }
            try {
                sb = new Socket("localhost", 3128);
            } catch (ConnectException e) {
                System.out.println("Can't connect to beta server.");
            }
            Controller controller = new Controller(sa, sb);
            Alien a = new Alien();
            a.setName("vasia2");
            a.setLang("01");
            a.setAddress(12);
            a.setPassword("pass");
            boolean b = controller.Register(a);
            System.out.println(b);
            
            Alien fromDB = controller.auth("vasia2", "pass");
            System.out.println(fromDB.getName());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
