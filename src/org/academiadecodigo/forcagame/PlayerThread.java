package org.academiadecodigo.forcagame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by codecadet on 17/06/16.
 */
public class PlayerThread implements Runnable{

    private Socket socket = null;
    private BufferedReader input = null;

    public PlayerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // recebe do serverThread

            String text = input.readLine();

            while(text != null) {

                if (text.equals("TOKEN")){
                    System.out.println("It's your turn");

//                    text = input.readLine();
                } else {
                    System.out.println(text);
//                    break;
                }
//                System.out.println(text); PROVOCAVA O A DUPLICAÇÃO DO IVISIBLE LETTERS E FAILED LETTERS

                text = input.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                if (socket != null) socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
