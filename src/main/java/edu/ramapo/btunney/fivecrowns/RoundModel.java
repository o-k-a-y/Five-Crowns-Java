package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 One round of 5Crowns containing the Players, and the draw/discard piles
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

import android.util.Log;

import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;
import edu.ramapo.btunney.fivecrowns.deck.DeckModel;
import edu.ramapo.btunney.fivecrowns.player.ComputerModel;
import edu.ramapo.btunney.fivecrowns.player.HumanModel;
import edu.ramapo.btunney.fivecrowns.player.PlayerModel;

public class RoundModel
{

    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************


    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    public enum PlayerType {
        Computer,
        Human
    }

    /** The types of moves a player can make */
    public enum MoveType {
        Draw,
        DrawFromDrawPile,
        DrawFromDiscardPile,
        Discard,
        DiscardFromHand,
        Help,
        GoOut,
        Save,
        NextTurn,
    }

    /** The number of players in this game */
    private static final int TOTAL_PLAYERS = 2;

    /** The offset of wild card from round number */
    private static final int WILD_CARD_OFFSET = 2;

    /** The offset of cards to deal from round number */
    private static final int CARDS_GIVEN_OFFSET = 2;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    // The array of players of this round
    PlayerModel[] playerList;

    // The number of this round, which determines the size of players hand */
    private int roundNumber;

    // The current player whose turn it is
    private int currentPlayerIndex;

    // The card deal for this round
    private CardDealerModel cardDealer;

    // The draw pile for this round
    private CardVectorModel drawPile;

    // The discard pile for this round
    private CardVectorModel discardPile;


    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************


    /**
     * Purpose: Constructor to create a new RoundModel
     * @param human the human player
     * @param computer the computer player
     * @param inRoundNumber the round number
     * @param inCurrentPlayerIndex the current player index
     * @param loadGame whether or not we are loading from a game save file
     */
    public RoundModel(HumanModel human, ComputerModel computer, int inRoundNumber, int inCurrentPlayerIndex, boolean loadGame)
    {
        // Set a local copy of round number
        roundNumber = inRoundNumber;

        currentPlayerIndex = inCurrentPlayerIndex;

        // Create the two players
        //    Player human = new Human();
        //    Player computer = new Player();

        // Add them to iteration array
        playerList = new PlayerModel[TOTAL_PLAYERS];
        playerList[0] = computer;
        playerList[1] = human;

        // Create CardDealer
        cardDealer = new CardDealerModel();

        // Update wild cards
        /**(DELEGATION) */
        cardDealer.updateWildCards(roundNumber + WILD_CARD_OFFSET);

        if (!loadGame)
        {
            /** Deal cards to the players */
            for (int count = 0; count < roundNumber + CARDS_GIVEN_OFFSET; count++)
            {
                playerList[0].addCard(cardDealer.dealCard());
                playerList[1].addCard(cardDealer.dealCard());
            }

            //    System.out.println("Player 1: " + playerList[0].toString());
            //    System.out.println("Player 2: " + playerList[1].toString());

            /** Deal rest of the cards to draw pile */
            drawPile = new CardVectorModel (DeckModel.DECK_SIZE * CardDealerModel.NUM_DECKS);

            /** While there are cards in the CardDealer decks, add them to the draw pile! */
            CardModel nextCard = cardDealer.dealCard();
            while (nextCard != null)
            {
                drawPile.add(nextCard);
                nextCard = cardDealer.dealCard();
            }

            /** Take the first card from the draw pile and place it in discard pile*/
            discardPile = new CardVectorModel (DeckModel.DECK_SIZE * CardDealerModel.NUM_DECKS);
            discardPile.add(drawPile.get(0));         // discardPile.insertElementAt(drawPile.get(0), 0); (to add to beginning of discard pile)
            drawPile.remove(0);
        }

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

    /** Return the current round nunumber */
    int getRoundNumber() {
        return roundNumber;
    }

    /** Return the specified player's score */
    int getPlayerScore(PlayerType playerType) {
        return playerList[playerType.ordinal()].getScore();
    }

    /**
     * Purpose: Return the draw pile
     * @return a CardVectorModel representing the draw pile
     */
    CardVectorModel getDrawPile()
    {
        return drawPile;
    }

    /**
     * Purpose: Return the discard pile
     * @return a CardVectorModel representing the discard pile
     */
    CardVectorModel getDiscardPile()
    {
        return discardPile;
    }

    /**
     * Purpose: Return the Computer's hand
     * @return a CardVectorModel representing the Computer's hand
     */
    CardVectorModel getComputerHand()
    {
        return playerList[0].getHand();
    }

    /**
     * Purpose: Return the Human's hand
     * @return a CardVectorModel representing the Humans's hand
     */
    CardVectorModel getHumanHand()
    {
        return playerList[1].getHand();
    }

    /**
     * Purpose: Return the index of the current player
     * @return an int, the index
     */
    int getCurrentPlayerIndex()
    {
        return currentPlayerIndex;
    }


    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    /**
     * Purpose: Determine if move was valid and perform the move passed in through the
     * button click from the RoundActivity.
     * @param moveType the type of Move passed from the button click
     * @return true or false depending on if the move was valid
     */
    public boolean makeMove(MoveType moveType) {

        /** If Computer's turn and  */
        if (currentPlayerIndex == 0 && MoveType.NextTurn.equals(moveType)) {
            playerList[currentPlayerIndex].play(drawPile, discardPile);

            incrementPlayerIndex();
            return true;
        }
        Log.d("current player in roundmodel", Integer.toString(currentPlayerIndex));

        /** If Human's turn and valid move */
        if (currentPlayerIndex == 1 && moveType != MoveType.NextTurn) {
            switch(moveType) {
                case Draw:
                    playerList[currentPlayerIndex].setPlayStatus(PlayerModel.PlayStatus.Drawing);
                    break;
                case DrawFromDrawPile:
                case DrawFromDiscardPile:
                    playerList[currentPlayerIndex].setPileToDrawFrom(moveType == MoveType.DrawFromDrawPile);
                    playerList[currentPlayerIndex].drawCard(drawPile, discardPile);
                    playerList[currentPlayerIndex].setPlayStatus(PlayerModel.PlayStatus.Drawn);
                    break;
                case Discard:
                    playerList[currentPlayerIndex].setPlayStatus(PlayerModel.PlayStatus.Discarding);
                    break;
                case DiscardFromHand:
                    playerList[currentPlayerIndex].setPlayStatus(PlayerModel.PlayStatus.Discarded);
                    break;
                case GoOut:
                    if (playerList[currentPlayerIndex].goOut()) {
//                        playerList[currentPlayerIndex].playStatus = PlayerModel.PlayStatus.GoneOut;
                    }
                    incrementPlayerIndex();
                case Help:
                    break;
            }
            return true;
        }

        return false;
    }

    /**
     * Purpose: Set the draw pile
     * @param drawPile the pile to set it to
     */
    public void setDrawPile(CardVectorModel drawPile) {
        this.drawPile = drawPile;
    }

    /**
     * Purpose: Set the discard pile
     * @param discardPile the pile to set it to
     */
    public void setDiscardPile(CardVectorModel discardPile) {
        this.discardPile = discardPile;
    }

    /**
     * Purpose: Set the Computer's hand
     * @param computerHand the hand to set it to
     */
    public void setComputerHand(CardVectorModel computerHand) {
        this.playerList[0].setHand(computerHand);
    }

    /**
     * Purpose: Set the Human's hand
     * @param humanHand the hand to set it to
     */
    public void setHumanHand(CardVectorModel humanHand) {
        this.playerList[1].setHand(humanHand);
    }

    /**
     * Purpose: Reset the player's status so we don't think the player has already gone out at the beginning of a turn
     */
    public void resetPlayerStatus() {
        for (int i = 0; i < playerList.length; i++) {
            playerList[i].setPlayStatus(PlayerModel.PlayStatus.Begun);
        }
    }

    /**
     * Purpose: Increment to the next player index and mod by 2
     */
    private void incrementPlayerIndex() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
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

    /**
     * Purpose: Allow serialization of game by making a string representation of the current round and its state
     * @return a string, containing round number, scores, hands, draw/discard pile, and next player
     */
    public String toString()
    {
        // Round number
        String result = "Round: " + roundNumber + "\n";

        // Computer score
        result += "Computer\n\tScore: " + playerList[0].getScore() + "\n\t"
                + "Hand: " + playerList[0].toString() + "\n";

        // Human score
        result += "Human\n\tScore: " + playerList[1].getScore() + "\n\t"
                + "Hand: " + playerList[1].toString() + "\n";

        // Draw pile
        result += "Draw Pile: ";
        result += drawPile.toString();
        result += "\n";

        // Discard pile
        result += "Discard Pile: ";
        result += discardPile.toString();
        result += "\n";

        // Next player
        result += "Next Player: ";
        if (currentPlayerIndex == 0) {
            result += "Computer";
        } else {
            result += "Human";
        }
        result += "\n";

        return result;
    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

//    public static void main( String args[] )
//    {
//        Round round = new Round(new Human(), new Computer(), 1);
//        System.out.println(round.toString());
//
//        round.play();
//    }
}

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************

