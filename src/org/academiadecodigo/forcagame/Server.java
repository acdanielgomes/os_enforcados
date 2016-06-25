package org.academiadecodigo.forcagame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 17/06/16.
 */
public class Server {

    private final int PORT_NUMBER = 8000;
    private int maxNumberPlayers = 5;

    private Game game;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private int n = 0;
    private BufferedReader input;
    private boolean isGameEnd;

    private ServerThread serverThread;
    private Thread clientThread;

    private List<ServerThread> clientList = Collections.synchronizedList(new ArrayList<>(maxNumberPlayers));

    public Server(){
        game = new Game();
    }

    /**
     * When the server started create the serverSocket with a port number.
     * Create a pool with a maximum number of threads equal to the maximum number of clients allowed.
     * Server is always waiting for new connections..
     * When a new connection is accept a thread is created in the pool.
     */
    public void start() {

        try {

            serverSocket = new ServerSocket(PORT_NUMBER);

            ExecutorService pool = Executors.newFixedThreadPool(maxNumberPlayers);

            input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Insert the number of players between 1 and 5!");

            while (n == 0) {
                n = Integer.parseInt(input.readLine());

                if (n <= maxNumberPlayers) {
                    setMaxNumberPlayers(n);
                    System.out.println(maxNumberPlayers);

                } else {
                    System.out.println("The nº of players can't be " + n + " please insert the number of players between 1 and 5!");
                    n = 0;
                }
            }


            System.out.println("Waiting for clients to connect...");


            //server.sendall(o jogo vai começar, o 1ro e ana)
            //WHILE
            //send8 ana TOKEN
            //OUVIR A RESPOSTA
            //SEND TO ALL RESPONSE.
            //
            // TODO: 22/06/16 put all this shit to a method
            /* while (true) {
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                }*/

             /* Verify the size of the List and if is less to maxNumberPlayers, it's add to the List */
            while (clientList.size() < maxNumberPlayers) {

                clientSocket = serverSocket.accept();
                System.out.println("Player accepted");


                /* Create Threads and put them in the pool */
                serverThread = new ServerThread(clientSocket, this);
                clientList.add(serverThread);
                clientThread = new Thread(serverThread);
                pool.submit(clientThread);
            }



            Thread.sleep(5000);

            game.start();


           /* while (!isGameEnd){



            }*/



            sendToAll(game.toString(game.getInvisibleLetters()));


        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            try {
               if (serverSocket != null) serverSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Method that is responsible to send the message to all clients connected */
    public void sendToAll(String msg){
        synchronized (clientList) {
            System.out.println("Sending to clients");

            /*game.confirmLetters(msg);
            isGameEnd = game.confirmWord(msg);*/



            for (ServerThread client: clientList) {

                if (msg.length() == 1) {

                    game.confirmLetters(msg);
                    isGameEnd = game.confirmWord(game.toString(game.getInvisibleLetters()).replaceAll("\\s",""));

                    client.write(game.toString(game.getInvisibleLetters()) + "\n" + game.getFailedLetters());

                } else {

                    isGameEnd = game.confirmWord(msg);
                    client.write(game.toString(game.getInvisibleLetters()) + "\n" + game.getFailedLetters());
                }
            }
        }
    }

    /* GETTERS AND SETTERS */

    public void setMaxNumberPlayers(int maxNumberPlayers) {
        this.maxNumberPlayers = maxNumberPlayers;
    }

    public List<ServerThread> getClientList() {
        return clientList;
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.start();

    }
}
