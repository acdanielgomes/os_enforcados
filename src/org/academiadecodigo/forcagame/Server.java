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

    private int portNumber;
    private int maxNumberPlayers = 5;

    private final String PATH_GREETINGS = "resources/hangmanGreetings.txt";
    private final String PATH_VICTORY = "resources/victory.txt";

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

    public Server(int i){
        portNumber = i;
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
            serverSocket = new ServerSocket(portNumber);

            ExecutorService pool = Executors.newFixedThreadPool(maxNumberPlayers);

            input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Insert the number of players between 1 and 5!");

            while (numberPlayers == 0) {
                numberPlayers = Integer.parseInt(input.readLine());

                if (numberPlayers <= maxNumberPlayers && numberPlayers >= 1) {
                    setMaxNumberPlayers(numberPlayers);

                } else {
                    System.out.println("The nº of players can't be " + numberPlayers + ". Please insert the number of players between 1 and 5!");
                    numberPlayers = 0;
                }
            }
            System.out.println("Waiting for players to connect...");

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
            Thread.sleep(5000);
            sendToAll(game.greeting(PATH_GREETINGS));
            //serverThread.write(game.greeting("resources/HangmanGreetings.txt"));

            Thread.sleep(30000);

            game.start();

            int indexCurrentPlayer = 0;
            currentPlayer = playerList.get(indexCurrentPlayer);

            while (!isGameEnd){

                sendLettersStatus();

                sendToken(indexCurrentPlayer, currentPlayer);

                Thread.sleep(10000);

                if (isGameEnd) {
                    System.out.println("The game is over");
                    sendLettersStatus();
                    sendToAll(currentPlayer.getName() + " won!");
                    currentPlayer.write(game.victory(PATH_VICTORY));

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

    /**
     * sends a token for the current player and sends a message to the rest of the players (clients)
     * @param indexCurrentPlayer - current player position from the array list
     * @param currentPlayer - player actually playing
     */
    public void sendToken(int indexCurrentPlayer, ServerThread currentPlayer){

        for (int i = 0; i < playerList.size(); i++) {

            if (i != indexCurrentPlayer) {
                playerList.get(i).write("It's " + currentPlayer.getName() + "'s turn!");
            } else {
                playerList.get(i).write("It's your turn!");
            }
        }
    }

    /**
     *  Method that is responsible to send the message to all clients connected
     **/
    public void sendToAll(String msg){
        synchronized (playerList) {
            System.out.println("Sending to clients");

            for (ServerThread client : playerList) {
                client.write(msg);
            }
        }
    }

    /**
     * sends the array of array with underscores and letters and it shows the failed letters
     */
    public void sendLettersStatus() {
        sendToAll(game.toString(game.getInvisibleLetters()) + "\n\n" + "failed letters: " + game.getFailedLetters() + "\n");
    }

    /**
     * it'll check if the player choice has one or more than one character and it calls the respective method
     * @param msg - player's input
     */
    public void checkPlayerChoice(String msg) {

        if (msg.length() == 1) {

            game.confirmLetter(msg);
            isGameEnd = game.confirmWord(game.toString(game.getInvisibleLetters()).replaceAll("\\s",""));

        }
        isGameEnd = game.confirmWord(msg);

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

        Server server = new Server(Integer.parseInt(args[0]));
        server.start();
    }
}
