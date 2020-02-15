package edu.ramapo.btunney.fivecrowns;

import java.util.ArrayList;

/*******************************************************
 Singleton class to hold game events and scores per Round played
 @author Brendan Tunney
 @since 12/08/2019
  ******************************************************** */

public class GameHistoryModel {
    private static final GameHistoryModel ourInstance = new GameHistoryModel();

    public static GameHistoryModel getInstance() {
        return ourInstance;
    }

    /** Keeps track of what has happened in the game so far except Human moves */
    private ArrayList<String> gameHistory;

    /** Keeps track of the score of each player on each played round */
    private ArrayList<String> scoreHistory;

    /**
     * Purpose: Instantiate an object
     */
    private GameHistoryModel() {
        gameHistory = new ArrayList<String>();
        scoreHistory = new ArrayList<String>();
    }

    /**
     * Purpose: Add a game history event to the list
     * @param event the event that occured: i.e. Computer move/suggestion to Human
     */
    public void addHistoryEvent(String event) {
        gameHistory.add(event);
    }

    /**
     * Purpose: Add round scores to the score history
     *      Should be displayed after game is over
     * @param scores
     */
    public void addHistoryScore(String scores) {
        scoreHistory.add(scores);
    }

    /**
     * Purpose: Return the game history so far
     * @return an ArrayList<String> each event in the game related to Computer
     */
    public ArrayList<String> getGameHistory() {
        return this.gameHistory;
    }


    /**
     * Purpose: Return the history of round scores of rounds played
     * @return an ArrayList<String>, each containing the score of both human and computer on a round
     */
    public ArrayList<String> getScoreHistory() {
        return this.scoreHistory;
    }
}
