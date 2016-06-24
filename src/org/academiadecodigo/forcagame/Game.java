package org.academiadecodigo.forcagame;

import java.util.ArrayList;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private String word;
    private String[] letters;
    private String[] invisibleLetters;
    private ArrayList<String> failedLetters;
    private int randomIndex;

    private FileManager fileManager;

    public Game() {
        fileManager = new FileManager("resources/Words.txt");
        fileManager.readFile();
    }


    public void init() {









        System.out.println(toString(invisibleLetters));

    }


    public void start() {

        word = getRandomWord();
        letters = convertWordToLetters(word);
        invisibleLetters = hideLetters(letters);

        System.out.println(word);
        /*System.out.println(word);
        System.out.println(toString(invisibleLetters));*/
    }

    public int randomIndex(){
        return randomIndex = (int)(Math.random() * fileManager.getWords().size());
    }

    public String getRandomWord(){
        return fileManager.getWords().get(randomIndex());
    }

    public String[] convertWordToLetters(String word) {

        String[] letters = word.split("");

        return letters;
    }

    public String[] hideLetters(String[] letters) {

        String[] hideLetters = new String[letters.length];

        for (int i = 0; i < hideLetters.length; i++) {
            hideLetters[i] = "__";
        }
        return hideLetters;
    }

    public String toString(String[] hideLetters) {

        String line = "";

        for (int i = 0; i < hideLetters.length; i++) {
            line += hideLetters[i] + " ";
        }
        return line;
    }

    public void confirm(String msg) {
        for (int i = 0; i < letters.length ; i++) {
            if (msg.equals(letters[i])) {
                invisibleLetters[i] = letters[i];
            }
        }
    }

    /* GETTERS AND SETTERS */
    public String[] getLetters() {
        return letters;
    }

    public String[] getInvisibleLetters() {
        return invisibleLetters;
    }

    public ArrayList<String> getFailedLetters() {
        return failedLetters;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
