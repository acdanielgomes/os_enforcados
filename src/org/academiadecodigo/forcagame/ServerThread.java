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

    private String name;
    private int life;

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

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // recebe do player
            output = new PrintWriter(socket.getOutputStream(), true); // envia para a playerThread


            while (name == null){
                name = input.readLine();
            }

            System.out.println(name + " connected - listening...");
            System.out.println("Players online: " + server.getClientList().size());


            while(true) {

                String msg = input.readLine();

                if (msg != null) {

                    if (msg.equals("exit")) {
                        System.out.println("Received from " + name + ": " + msg);


                        server.getClientList().remove(this);
                        System.out.println("List size: " + server.getClientList().size());
                        socket.close();
                        break;
                    }

                    server.sendToAll(msg);

                    server.checkPlayerChoice(msg);
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


    //Getters && setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
