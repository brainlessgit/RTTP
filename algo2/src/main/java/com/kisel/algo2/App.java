package com.kisel.algo2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Reading input values from `2.in` and prints result to `2.out`.
 *
 */
public class App {

    public static void main(String[] ar) {
        int l;
        int d;
        int n;
        try {
            BufferedReader br = new BufferedReader(new FileReader("2.in"));
            String[] args = br.readLine().split(" ");
            l = Integer.parseInt(args[0]);
            d = Integer.parseInt(args[1]);
            n = Integer.parseInt(args[2]);

            String[] words = new String[d];
            for (int i = 0; i < d; i++) {
                words[i] = br.readLine();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("2.out"));
            for (int i = 1; i <= n; i++) {
                Matcher matcher = new Matcher(br.readLine());
                bw.append("Case #" + i + ": " + matcher.match(words) + "\n");
            }
            br.close();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("smth frong with file");
        }
    }
}
