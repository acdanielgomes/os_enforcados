package org.academiadecodigo.forcagame;

import java.util.ArrayList;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private String word;
    private String[] letters;
    private String[] invisibleLetters;
    private String failedLetters = "";
    private FileManager fileManager;
    private int randomIndex;



    public Game() {
        fileManager = new FileManager("resources/mostCommonlyUsedNounsEnglish.txt");
        fileManager.readFile();
    }



    public void start() {
        word = getRandomWord();
        letters = convertWordToLetters(word);
        invisibleLetters = hideLetters(letters);

        System.out.println(word);
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



    public boolean confirmLetter(String msg){

        for (int i = 0; i < letters.length ; i++) {

            if (msg.equals(letters[i])) {
                invisibleLetters[i] = letters[i];
            }
        }

        if (toString(letters).contains(msg)){
            return true;
        }
        failedLetters += msg + " ";
        return false;
    }



    public boolean confirmWord(String msg){

        if (msg.equals(word)) {
            invisibleLetters = letters;
            return true;
        }
        return false;
    }


    public String greeting(String filepath){

        return fileManager.greeting(filepath);
    }




   // Getters && setters
    public String[] getLetters() {
        return letters;
    }

    public String[] getInvisibleLetters() {
        return invisibleLetters;
    }


    public String getFailedLetters() {
        return failedLetters;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
