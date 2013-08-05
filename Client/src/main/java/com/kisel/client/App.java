package com.kisel.client;

import com.kisel.aliennet.model.Alien;
import com.kisel.client.controller.Controller;
import java.net.Socket;

public class App {

    public static void main(String[] args) {
        try {
            Socket sa = new Socket("localhost", 3129);
            Socket sb = new Socket("localhost", 3128);
            Controller controller = new Controller(sa, sb);

            Alien a = new Alien();
            a.setName("vasia");
            a.setLang("01");
            a.setAddress(120);
            a.setPassword("pass");

            boolean b = controller.Register(a);
            System.out.println(b);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
