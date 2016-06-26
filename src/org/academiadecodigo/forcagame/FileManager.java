package org.academiadecodigo.forcagame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by codecadet on 20/06/16.
 */
public class FileManager {

    private String filePath;

    private FileReader fileReader;
    private BufferedReader breader;
    private ArrayList<String> words = new ArrayList<>();
    private String lines= "";

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public String readFile(){

        try {

            fileReader = new FileReader(filePath);
            breader = new BufferedReader(fileReader);

            String word = breader.readLine();

            while (word != null) {

                words.add(word);

                word = breader.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File is empty");
            //e.printStackTrace();

        } finally {

            try {

                if (fileReader != null) fileReader.close();
                if (breader != null) breader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String greeting(String filePath){


        try {
            String line = "";

            fileReader = new FileReader(filePath);
            breader = new BufferedReader(fileReader);

            line = breader.readLine();

            while (line != null) {

                lines+= line + "\n";
                line = breader.readLine();
            }

            return lines;


    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* GETTERS AND SETTERS */
    public ArrayList<String> getWords() {
        return words;
    }

    /*public int randomIndex(){
        return randomIndex = (int)(Math.random() * words.size());
    }

    public String getRandomWord(){
        return words.get(randomIndex());
    }*/

    /*public char[] convertWordToLetters(String word) {

        char[] chars = word.toCharArray();
        System.out.println(chars);
        return chars;
    }

    public String[] hideLetters(char[] chars) {

        String[] hideLetters = new String[chars.length];

        for (int i = 0; i < hideLetters.length; i++) {
            hideLetters[i] = "__";
        }

        System.out.println(toString(hideLetters));

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
