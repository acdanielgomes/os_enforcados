package org.academiadecodigo.forcagame;

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

    /**
     * starts the game
     */
    public void start() {
        word = getRandomWord();
        letters = convertWordToLetters(word);
        invisibleLetters = hideLetters(letters);

        System.out.println(word);
    }

    /**
     * returns a number between zero and the maximum number possible from the array list of words
     * @return
     */
    public int randomIndex(){
        return randomIndex = (int)(Math.random() * fileManager.getWords().size());
    }

    /**
     * it'll get us a certain word, based on a random generated index from the array list
     * @return
     */
    public String getRandomWord(){
        return fileManager.getWords().get(randomIndex());
    }

    /**
     * receives the "choosen" word and it splits every letter from the word, saving it on an array of strings
     * @param word
     * @return
     */
    public String[] convertWordToLetters(String word) {

        String[] letters = word.split("");

        return letters;
    }

    /**
     * receives an array of strings with each position representing a letter
     * creates a new array of strings with the size of the array received as an argument
     * based on the length provided, it'll create an underscore in each position of that array
     * @param letters
     * @return
     */
    public String[] hideLetters(String[] letters) {

        String[] hideLetters = new String[letters.length];

        for (int i = 0; i < hideLetters.length; i++) {
            hideLetters[i] = "__";
        }
        return hideLetters;
    }

    /**
     * it "teaches" the array of underscores to print itself
     *
     * @param hideLetters
     * @return
     */
    public String toString(String[] hideLetters) {

        String line = "";

        for (int i = 0; i < hideLetters.length; i++) {
            line += hideLetters[i] + " ";
        }
        return line;
    }

    /**
     * it'll iterate the array of letters and check if there's a match
     * the method is triggered in case of one single letter
     * @param msg - letter provided by the player
     * @return true if the letter exists on the array, false if not
     */
    public boolean confirmLetter(String msg){

        for (int i = 0; i < letters.length ; i++) {

            if (msg.equals(letters[i])) {
                invisibleLetters[i] = letters[i];
            }
        }

        if (toString(letters).contains(msg)) {
            return true;
        }
        failedLetters += msg + " ";
        return false;
    }

    /**
     * this method it'll be triggered in case it's more than one character as an argument
     * it'll compare the provided string with the word that the game generated
     * @param msg - word provided by the player
     * @return true if the word is the correct one, false if it is not
     */
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

    public String victory(String filepath){
        return fileManager.victory(filepath);
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
