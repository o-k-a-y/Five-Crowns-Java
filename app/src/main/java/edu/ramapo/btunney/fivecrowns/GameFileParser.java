package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Parses a game save file and extracts and sets what objects it represents
 @author Brendan Tunney
 @since Since 12/1/2019
  ******************************************************** */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;


public class GameFileParser {

    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /**
     * Purpose: The possible values in the game save file
     */
    private enum Values {
        RoundNumber,
        ComputerScore,
        ComputerHand,
        HumanScore,
        HumanHand,
        DrawPile,
        DiscardPile,
        NextPlayer
    }

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /**
     * The name of the game file
     */
    private String fileName;

    /** The contents of the file to save */
    private String file;

    /** The values after each colon (:) in the file */
    private Vector<String> values;

    /**
     * The round number in the game file
     */
    private int roundNumber;

    /**
     * The Computer's score in the game file
     */
    private int computerScore;

    /**
     * The Human's score in the game file
     */
    private int humanScore;

    /**
     * The Computer's hand in the game file
     */
    private CardVectorModel computerHand;

    /**
     * The Human's hand in the game file
     */
    private CardVectorModel humanHand;

    /**
     * The Draw Pile in the game file
     */
    private CardVectorModel drawPile;

    /**
     * The Discard Pile in the game file
     */
    private CardVectorModel discardPile;

    /** The index of the Player who will play next */
    private int nextPlayer;

    /** Context to access internal storage (passed from LoadGameActivity) */
    private Context context;

    private boolean fileFromAssets;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     * Purpose: Constructor for when loading game save file
     * @param context so we can access files in internal storage
     * @param fileName the name of the file to load
     */
    public GameFileParser(Context context, String fileName, boolean fileFromAssets) {
        this.context = context;
        this.fileName = fileName;

        this.values = new Vector<>(8, 2);
        this.computerHand = new CardVectorModel(true);
        this.humanHand = new CardVectorModel(true);
        this.drawPile = new CardVectorModel();
        this.discardPile = new CardVectorModel();
        this.roundNumber = -1;
        this.computerScore = -1;
        this.humanScore = -1;
        this.nextPlayer = -1;

        this.fileFromAssets = fileFromAssets;
    }

    /**
     * Purpose: Constructor for when saving a game save file
     * @param context so we can access files in internal storage
     * @param fileName the name of the file we're writing to
     * @param file the file we're saving
     */
    public GameFileParser(Context context, String fileName, String file, boolean fileFromAssets) {
        this.context = context;
        this.fileName = fileName;
        this.file = file;
        this.fileFromAssets = fileFromAssets;
    }

    // *********************************************************
    // ******************** Paint - View ***********************
    // *********************************************************

    // *********************************************************
    // ******************** actionPerformed - Controller *******
    // *********************************************************

    // *********************************************************
    // ******************** Selectors **************************
    // *********************************************************

    /**
     * Purpose: Return the round number
     * @return an int, the round number
     */
    int getRoundNumber() {
        return this.roundNumber;
    }

    /**
     * Purpose: Return the Computer's score
     * @return an int, the Computer's score
     */
    int getComputerScore() {
        return this.computerScore;
    }

    /**
     * Purpose: Return the Human's score
     * @return an int, the Human's score
     */
    int getHumanScore() {
        return this.humanScore;
    }

    /**
     * Purpose: Return the Computer's hand
     * @return a CardVectorModel, the Computer's Hand
     */
    CardVectorModel getComputerHand() {
        return this.computerHand;
    }

    /**
     * Purpose: Return the Human's hand
     * @return a CardVectorModel, the Human's Hand
     */
    CardVectorModel getHumanHand() {
        return this.humanHand;
    }

    /**
     * Purpose: Return the draw pile
     * @return a CardVectorModel, the draw pile
     */
    CardVectorModel getDrawPile() {
        return this.drawPile;
    }

    /**
     * Purpose: Return the discard pile
     * @return a CardVectorModel, the discard pile
     */
    CardVectorModel getDiscardPile() {
        return this.discardPile;
    }

    /**
     * Purpose: Return the index of the next Player
     * @return an int, the index of next Player
     */
    int getNextPlayer() {
        return this.nextPlayer;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    /**
     * Purpose: Parse the file when a user taps a save file button from LoadGameActivity
     * @throws IOException
     */
    void parseFile() throws IOException {
        BufferedReader reader = null;
        if (fileFromAssets) {

            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
        } else {
            FileInputStream is;

            final File file = new File(this.context.getFilesDir(), "" + fileName);
            if (file.exists()) {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
            }
        }

        // Line with no whitespace
        String newLine = "";
        // Location of the colon on each line if exists
        int colonLocation = -1;
        // The value after the colon
        String value = "";

        String line = reader.readLine();
        while (line != null) {
            // Remove all white space on each line
            newLine = line.replaceAll("\\s", "");

            // Find location of the colon, and add value of exists
            colonLocation = newLine.indexOf(":");
            if (colonLocation != -1 && !newLine.equals("Computer:") && !newLine.equals("Human:")) {
                value = newLine.substring(colonLocation + 1);
                values.add(value);
            }

            line = reader.readLine();
        }

        // Set all the values
        setRoundNumber(values.get(Values.RoundNumber.ordinal()));
        setScore(values.get(Values.ComputerScore.ordinal()), false);
        setHand(values.get(Values.ComputerHand.ordinal()), false);
        setScore(values.get(Values.HumanScore.ordinal()), true);
        setHand(values.get(Values.HumanHand.ordinal()), true);
        setDrawPile(values.get(Values.DrawPile.ordinal()));
        setDiscardPile(values.get(Values.DiscardPile.ordinal()));
        setNextPlayer(values.get(Values.NextPlayer.ordinal()));

    }

    /**
     * Purpose: Save the current game state to given file name
     */
    public void saveToFile() {
        this.fileName = fileName + ".txt";
        Log.d("the file", file);
        File directory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            FileWriter fileWriter = new FileWriter(new File(directory, fileName));
            fileWriter.write(this.file);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose: Set round number of the game
     * @param roundNumber the round number as a string
     */
    private void setRoundNumber(String roundNumber) {
        this.roundNumber = Integer.parseInt(roundNumber);
    }

    /**
     * Purpose: Set the score of Human and Computer player
     * @param score the score of the Player as a string
     * @param humanScore whether or not it's a Human player's score
     */
    private void setScore(String score, Boolean humanScore) {
        if (humanScore) {
            this.humanScore = Integer.parseInt(score);
        } else {
            this.computerScore = Integer.parseInt(score);
        }
    }

    /**
     * Purpose: Set the Human and Computer's hand
     * @param hand the hand as a string
     * @param isHumanHand whether or not it's a Human player's hand
     */
    private void setHand(String hand, Boolean isHumanHand) {
        // Get all the cards as strings
        ArrayList<String> cardsAsStrings = stringToStringArray(hand);

        for (int i = 0; i < cardsAsStrings.size(); i++) {
            // If human hand, add cards to their hand, otherwise add to computer's
            if (isHumanHand) {
                this.humanHand.add(stringToCard(cardsAsStrings.get(i)));
            } else {
                this.computerHand.add(stringToCard(cardsAsStrings.get(i)));
            }
        }
//        CardModel card = stringToCard(hand);
    }

    /**
     * Purpose: Set the index of the next player
     * @param nextPlayer the index as a string
     */
    private void setNextPlayer(String nextPlayer) {
        if (nextPlayer.equals("Computer")) {
            this.nextPlayer = 0;
        } else {
            this.nextPlayer = 1;
        }
    }

    /**
     * Purpose: Set the draw pile of the game
     * @param drawPile the draw pile as a string
     */
    private void setDrawPile(String drawPile) {
        ArrayList<String> cardsAsStrings = stringToStringArray(drawPile);

        for (int i = 0; i < cardsAsStrings.size(); i++) {
            this.drawPile.add(stringToCard(cardsAsStrings.get(i)));
        }

    }

    /**
     * Purpose: Set the discard pile of the game
     * @param discardPile the discard pile as a string
     */
    private void setDiscardPile(String discardPile) {
        ArrayList<String> cardsAsStrings = stringToStringArray(discardPile);

        for (int i = 0; i < cardsAsStrings.size(); i++) {
            this.discardPile.add(stringToCard(cardsAsStrings.get(i)));
        }

    }

    /**
     * Purpose: Create a CardModel from a string
     * @param card the string to make the card of
     * @return a CardModel, representing the card of the string
     */
    private CardModel stringToCard(String card) {
        String cardString = card.substring(0, 2);
//        int face = card.charAt(card.indexOf(card.substring(0, 1)));
        return new CardModel(cardString, roundNumber);
    }

    /**
     * Purpose: Turn a string into an array of strings of 2 characters long
     * @param string the string to split
     * @return and array of the string where each string is 2 characters long
     */
    private ArrayList<String> stringToStringArray(String string) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < string.length(); i+=2) {
            strings.add(string.substring(i, i+2));
        }
//        return string.split("(?<=\\G..)");
        return strings;
    }



    // *********************************************************
    // ******************** Code Generation ********************
    // *********************************************************

    // *********************************************************
    // ******************** Code Explanation *******************
    // *********************************************************

    // *********************************************************
    // ******************** Utility Methods ********************
    // *********************************************************

    // *********************************************************
    // ******************** Printing Methods *******************
    // *********************************************************
//    public void printEverything() {
//        Log.d("round", Integer.toString(this.roundNumber));
//        Log.d("computer score", Integer.toString(this.computerScore));
//        Log.d("human score", Integer.toString(this.humanScore));
//        Log.d("computer hand",this.computerHand.toString());
//        Log.d("human hand", this.humanHand.toString());
//        Log.d("draw pile" , this.drawPile.toString());
//        Log.d("discard pile", this.discardPile.toString());
//        Log.d("next player",  Integer.toString(this.nextPlayer));
//    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

    // *********************************************************
    // ******************** Trash Methods **********************
    // *********************************************************
}
//
//    public void parseFileFromInternal() {
//        String file = "";
//        BufferedReader reader = null;
//
//        // Line with no whitespace
//        String newLine = "";
//        // Location of the colon on each line if exists
//        int colonLocation = -1;
//        // The value after the colon
//        String value = "";
//
//        try {
//            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
//
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                // Remove all white space on each line
//                newLine = line.replaceAll("\\s", "");
//
//                // Find location of the colon, and add value of exists
//                colonLocation = newLine.indexOf(":");
//                if (colonLocation != -1 && !newLine.equals("Computer:") && !newLine.equals("Human:")) {
//                    value = newLine.substring(colonLocation + 1);
//                    values.add(value);
//                }
//
//
//                file += line;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        setRoundNumber(values.get(Values.RoundNumber.ordinal()));
//        setScore(values.get(Values.ComputerScore.ordinal()), false);
//        setHand(values.get(Values.ComputerHand.ordinal()), false);
//        setScore(values.get(Values.HumanScore.ordinal()), true);
//        setHand(values.get(Values.HumanHand.ordinal()), true);
//        setDrawPile(values.get(Values.DrawPile.ordinal()));
//        setDiscardPile(values.get(Values.DiscardPile.ordinal()));
//        setNextPlayer(values.get(Values.NextPlayer.ordinal()));
//    }
