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

    private final int PORT_NUMBER = 8080;
    private int maxNumberPlayers = 5;
    private final int MINNUMBERPLAYERS = 1;

    private ServerSocket serverSocket = null;
    private Socket playerSocket = null;
    private ServerThread serverThread;
    private Thread playerThread;
    private BufferedReader input;


    private List<ServerThread> playerList = Collections.synchronizedList(new ArrayList<>(maxNumberPlayers));

    private int numberPlayers = 0;

    private Game game;
    private boolean isGameEnd;

    private ServerThread currentPlayer;




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

                if (numberPlayers <= maxNumberPlayers && numberPlayers >= MINNUMBERPLAYERS) {
                    setMaxNumberPlayers(numberPlayers);

                } else {
                    System.out.println("The nº of players can't be " + numberPlayers + ". Please insert the number of players between 1 and 5!");
                    numberPlayers = 0;
                }
            }
            System.out.println("Waiting for players to connect...");

            /* while (true) {
                    input = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                }*/



             /* Verify the size of the List and if is less to maxNumberPlayers, it's add to the List */
            while (playerList.size() < maxNumberPlayers) {

                playerSocket = serverSocket.accept();
                System.out.println("Player accepted");

                /* Create Threads and put them in the pool */
                serverThread = new ServerThread(playerSocket, this);

                playerList.add(serverThread);

                playerThread = new Thread(serverThread);

                pool.submit(playerThread);


                serverThread.write("Introduce your name please: ");

            }
            Thread.sleep(2000);
            sendToAll(game.greeting("resources/HangmanGreetings.txt"));
            //serverThread.write(game.greeting("resources/HangmanGreetings.txt"));

            Thread.sleep(3000);

            game.start();



            int indexCurrentPlayer = 0;
            currentPlayer = playerList.get(indexCurrentPlayer);

            while (!isGameEnd){

                sendLettersStatus();

                sendToken(indexCurrentPlayer, currentPlayer);


                Thread.sleep(5000);

                if (isGameEnd) {
                    System.out.println("The game is over");
                    sendLettersStatus();
                    sendToAll(currentPlayer.getName() + " won!");
                    currentPlayer.write(game.victory("resources/victory.txt"));

                    Thread.sleep(5000);
                    serverSocket.close();

                    break;
                }


                if (indexCurrentPlayer < playerList.size() - 1) {
                    indexCurrentPlayer++;
                } else {
                    indexCurrentPlayer = 0;
                }
                currentPlayer = playerList.get(indexCurrentPlayer);
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

        for (int i = 0; i < playerList.size(); i++) {

            if (i != indexCurrentPlayer) {
                playerList.get(i).write("It's " + currentPlayer.getName() + "'s turn!");
            } else {
                currentPlayer.write("TOKEN - It's your turn!");
            }
        }
    }


    /* Method that is responsible to send the message to all clients connected */
    public void sendToAll(String msg){
        synchronized (playerList) {
            System.out.println("Sending to clients");

            for (ServerThread client : playerList) {
                client.write(msg);
            }
        }
    }



    public void sendLettersStatus() {
        sendToAll(game.toString(game.getInvisibleLetters()) + "\n\n" + "failed letters: " + game.getFailedLetters() + "\n");
    }


    public void checkPlayerChoice(String msg) {

        if (msg.length() == 1) {

            game.confirmLetter(msg);
            isGameEnd = game.confirmWord(game.toString(game.getInvisibleLetters()).replaceAll("\\s",""));

            /*if (game.confirmLetter(msg) == false) {
                serverThread.setLife();
                if (serverThread.getLife() == 0) {
                    playerList.remove(currentPlayer);
                    System.out.println(playerList.size());
                }
            }*/
        }
        isGameEnd = game.confirmWord(msg);

        /*else {
            isGameEnd = game.confirmWord(msg);

            if (game.confirmWord(msg) == false) {
                playerList.remove(currentPlayer);
                System.out.println(playerList.size());
            }
        }*/
    }


    //Getters && setters
    public void setMaxNumberPlayers(int maxNumberPlayers) {
        this.maxNumberPlayers = maxNumberPlayers;
    }

    public List<ServerThread> getPlayerList() {
        return playerList;
    }

    public Game getGame() {
        return game;
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.start();
    }
}
