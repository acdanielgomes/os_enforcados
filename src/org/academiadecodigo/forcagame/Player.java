package org.academiadecodigo.forcagame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 17/06/16.
 */
public class Player {

    private String hostName;
    private int portNumber;

    private Socket clientSocket = null;
    private PrintWriter out = null;
    private BufferedReader inKeyboard = null;

    private PlayerThread playerThread;

    /**
     * @param hostName Name of the the host to connect
     * @param portNumber Port number to connect
     */
    public Player(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * creates a socket for the client with a host and a port number
     * creates a new thread for the respective player (client)
     */
    public void start() {

        try {

            clientSocket = new Socket(hostName, portNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));

            playerThread = new PlayerThread(clientSocket);

            Thread thread = new Thread(new PlayerThread(clientSocket));
            thread.start();

            String inputRead = "";

            while(true){

                try {

                    inputRead = inKeyboard.readLine();

                    if (inputRead != null) {



                        out.write(inputRead + "\n");
                        out.flush();

                        if (inputRead.equals("exit")){
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



            /* Wait for this thread to die */
            thread.join();

        } catch (IOException e) {
            System.out.println("The game has already started with the max players! Try again in the next round!");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            try {

                if (clientSocket != null) clientSocket.close();
                if (out != null) out.close();
                if (inKeyboard != null) inKeyboard.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)  {

        Player player = new Player(args[0], Integer.parseInt(args[1]));
        player.start();
    }
}
