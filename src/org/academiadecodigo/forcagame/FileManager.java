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
    private BufferedReader bReader;
    private ArrayList<String> words = new ArrayList<>();

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    /**
     * reads the file saved from a provided file path
     * if there's a file available, it'll read us every line from it and it'll save on a variable
     * if there's no file, it'll send us an error message
     * each word found on the file, it'll be saved on array list of strings
     */
    public String readFile(){

        try {

            fileReader = new FileReader(filePath);
            bReader = new BufferedReader(fileReader);

            String word = bReader.readLine();

            while (word != null) {

                words.add(word);

                word = bReader.readLine();
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
                if (bReader != null) bReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String greeting(String filePath){
        String lines = "";

        try {

            fileReader = new FileReader(filePath);
            bReader = new BufferedReader(fileReader);

            String line = bReader.readLine();

            while (line != null) {

                lines += line + "\n";
                line = bReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (fileReader != null) fileReader.close();
                if (bReader != null) bReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public String victory(String filePath){
        String lines = "";
        try {

            fileReader = new FileReader(filePath);
            bReader = new BufferedReader(fileReader);

            String line1 = bReader.readLine();

            while (line1 != null) {

                lines += line1 + "\n";
                line1 = bReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (fileReader != null) fileReader.close();
                if (bReader != null) bReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    //Getters && setters
    public ArrayList<String> getWords() {
        return words;
    }
}
