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

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private ServerThread serverThread;
    private Thread clientThread;
    private BufferedReader input;

    private List<ServerThread> clientList = Collections.synchronizedList(new ArrayList<>(maxNumberPlayers));

    private int numberPlayers = 0;

    private Game game;
    private boolean isGameEnd;




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


            while (numberPlayers == 0) {
                numberPlayers = Integer.parseInt(input.readLine());

                if (numberPlayers <= maxNumberPlayers) {
                    setMaxNumberPlayers(numberPlayers);
                    System.out.println(maxNumberPlayers);

                } else {
                    System.out.println("The nº of players can't be " + numberPlayers + " please insert the number of players between 1 and 5!");
                    numberPlayers = 0;
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


            int indexCurrentPlayer = 0;
            ServerThread currentPlayer = clientList.get(indexCurrentPlayer);



            while (!isGameEnd){

                //enviar token para current
                //currentPlayer.write("TOKEN");
                // enviar !token para os outros
                sendToken(indexCurrentPlayer, currentPlayer);
                System.out.println("aqui");


                sendToAll(game.toString(game.getInvisibleLetters()));



                //wait clientList[currnetplayter]
                synchronized (currentPlayer) {
                    currentPlayer.wait();

                }
                //verificar se acertou

                //increment current player se nao acertou
                if (indexCurrentPlayer <= clientList.size()) {
                    indexCurrentPlayer++;
                } else {
                    indexCurrentPlayer = 0;
                }

                currentPlayer = clientList.get(indexCurrentPlayer);
            }


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


    public void sendToken(int indexCurrentPlayer, ServerThread currentPlayer){

            for (int i = 0; i < clientList.size(); i++) {

                if (i != indexCurrentPlayer) {
                    clientList.get(i).write("It's " + currentPlayer.getName() + " turn!");
                    //System.out.println(clientList.get(i).getName());

                } else {
                    //System.out.println("token");
                    currentPlayer.write("TOKEN");
                }
        }
    }


    /* Method that is responsible to send the message to all clients connected */
    public void sendToAll(String msg){
        synchronized (clientList) {
            System.out.println("Sending to clients");

            /*game.confirmLetters(msg);
            isGameEnd = game.confirmWord(msg);*/


           if (msg.length() == 1) {

                game.confirmLetters(msg);
                isGameEnd = game.confirmWord(game.toString(game.getInvisibleLetters()).replaceAll("\\s",""));

            } else {
                isGameEnd = game.confirmWord(msg);
            }

            for (ServerThread client: clientList) {

                client.write(game.toString(game.getInvisibleLetters()) + "\n" + "failed letters: " + game.getFailedLetters());
            }
        }
    }



    //Getters && setters
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
