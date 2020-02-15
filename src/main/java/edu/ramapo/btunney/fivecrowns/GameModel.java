package edu.ramapo.btunney.fivecrowns;


import java.util.Observable;

import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;
import edu.ramapo.btunney.fivecrowns.player.ComputerModel;
import edu.ramapo.btunney.fivecrowns.player.HumanModel;

/*******************************************************
 The Game class to hold the round number, and provide a way to get a Round object
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

public class GameModel extends Observable
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The round number for the current round */
    private int roundNumber;

    /** The current player whose turn it is */
    private int currentPlayerIndex;

    /** The player of this game */
    private HumanModel human;
    private ComputerModel computer;

    /** One line description of the variable */

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     * Purpose: Constructor for the GameModel
     * @param inCurrentPlayerIndex the player to go first
     */
    public GameModel(int inCurrentPlayerIndex)
    {
        // Create the two players
        human = new HumanModel();

        computer = new ComputerModel();

        // Initialize round number
        roundNumber = 1;

        currentPlayerIndex = inCurrentPlayerIndex;


        // Declare the winner of the game
    }

    /**
     * Purpose: Constructor
     * @param roundNumber the round number
     * @param computerScore the Computer's score
     * @param humanScore the Human's score
     * @param computerHand the Computer's hand
     * @param humanHand the Human's hand
     * @param nextPlayer the index of the next player
     */
    public GameModel(int roundNumber, int computerScore, int humanScore, CardVectorModel computerHand, CardVectorModel humanHand, int nextPlayer)
    {
        // Create the two players
        this.human = new HumanModel();
        this.human.setScore(humanScore);
        this.human.setHand(humanHand);

        this.computer = new ComputerModel();
        this.computer.setScore(computerScore);
        this.computer.setHand(computerHand);

        this.roundNumber = roundNumber;

        this.currentPlayerIndex = nextPlayer;
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
     * Purpose: Get a RoundModel
     * @param loadGame whether we are loading from a game save file
     * @return a RoundModel
     */
    public RoundModel getRound(boolean loadGame)
    {
        if (loadGame)
        {
            return new RoundModel(human, computer, roundNumber, currentPlayerIndex, true);
        }
        return new RoundModel(human, computer, roundNumber, currentPlayerIndex, false);
    }

    /**
     * Purpose: Get the round number
     * @return an int, the round number
     */
    public int getRoundNumber()
    {
        return roundNumber;
    }


    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    /**
     * Purpose: Set the round number
     * @param inRoundNumber Number to set it to
     */
    public void setRoundNumber(int inRoundNumber)
    {
        roundNumber = inRoundNumber;
    }

    /**
     * Purpose: Clear the players' hand
     */
    public void clearPlayerHands() {
        computer.clearHand();
        human.clearHand();
    }

    /**
     * Purpose: Update the players' scores
     */
    public void updatePlayersScores() {
        computer.setRoundScore();
        computer.setScore(computer.getRoundScore() + computer.getScore());
        human.setRoundScore();
        human.setScore(human.getRoundScore() + human.getScore());
    }

    /**
     * Purpose: Set the index of the player whose turn it currently is
     * @param currentPlayerIndex the index to set
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
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

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

//    public static void main( String args[] )
//    {
//        GameModel gameModel = new GameModel();
//
//        // Probably want to pass the number of players, the number of decks, etc (in future)
//        gameModel.play();
//    }
}

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************