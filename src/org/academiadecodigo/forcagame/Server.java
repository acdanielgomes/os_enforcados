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
            }
            System.out.println("Waiting for clients to connect...");

            game.start();

            while (true) {

                System.out.println("Clients online: " + clientList.size());

                clientSocket = serverSocket.accept();

                if (clientList.isEmpty()){

                // TODO: 22/06/16 put all this shit to a method

                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



                    if (n < maxNumberPlayers) {

                        setMaxNumberPlayers(n);
                        System.out.println(maxNumberPlayers);

                    } else {
                        System.out.println("You have reached the max number of players");
                    }
                }


                /* Create Threads and put them in the pool */
                ServerThread serverThread = new ServerThread(clientSocket,this);
                Thread clientThread = new Thread(serverThread);
                pool.submit(clientThread);


                /* Verify the size of the List and if is less to maxNumberPlayers, it's add to the List */
                if (clientList.size() < maxNumberPlayers){
                    clientList.add(serverThread);
                }

                if (clientList.size() > maxNumberPlayers){
                    System.out.println("Chat is too busy. Try again later!");
                    clientThread.interrupt();
                }

            }

        } catch (IOException e) {
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
            for (ServerThread client: clientList) {
                game.confirm(msg);
                client.write(/*msg + "\n" +*/ game.toString(game.getInvisibleLetters()));
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
