package org.academiadecodigo.forcagame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 20/06/16.
 */
public class ServerThread implements Runnable{

    private Socket socket;
    private Server server;

    private BufferedReader input = null;
    private PrintWriter output = null;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void write(String msg){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        output.write(msg + "\n");
        output.flush();
    }

    @Override
    public void run() {

        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            //server.sendToAll("Number of players? [0-5]");

            while(true) {

                String msg = input.readLine();

                if (msg != null) {

                    if (msg.equals("exit")) {

                        server.getClientList().remove(this);
                        System.out.println("List size: " + server.getClientList().size());
                        socket.close();
                        break;
                    }

                    server.sendToAll(msg);
                }
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



  /*  public String[] convertWordToLetters(String word) {

        String[] chars = word.split("");

        //char[] chars = word.toCharArray();
        //System.out.println(chars);
        return chars;
    }

    public String[] hideLetters(String[] chars) {

        String[] hideLetters = new String[chars.length];

        for (int i = 0; i < hideLetters.length; i++) {
            hideLetters[i] = "__";
        }

        //System.out.println(toString(hideLetters));

        return hideLetters;
    }

    public String toString(String[] hideLetters) {

        String line = "";

        for (int i = 0; i < hideLetters.length; i++) {
            line += hideLetters[i] + " ";
        }
        return line;
    }*/
}
