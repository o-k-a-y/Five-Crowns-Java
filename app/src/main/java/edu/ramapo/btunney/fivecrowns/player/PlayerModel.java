package edu.ramapo.btunney.fivecrowns.player;

/*******************************************************
 Base class of Human and Computer
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

import java.util.Vector;

import edu.ramapo.btunney.fivecrowns.combination.CombinationFinder;
import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;

public abstract class PlayerModel
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    public enum PlayStatus {
        Begun, // begin of turn
        Drawing,
        Drawn,
        Discarding,
        Discarded,
        FailedGoneOut,
        GoneOut,
    }

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The current playing status of the player (i.e. Begun, Drawing, etc.) */
    private PlayStatus playStatus;

    /** Which pile to draw from */
    protected boolean pileToDrawFrom;

    /** The current score of the player */
    protected int score;

    /** The current score of the round */
    protected int roundScore;

    /** The current hand of the player */
    protected CardVectorModel hand;

    /** To check if we can go out */
    private CombinationFinder combinationFinder;

    /** The player's books */
    private Vector<CardVectorModel> books;

    /** The player's runs */
    private Vector<CardVectorModel> runs;

    /** Remaining cards after going out */
    private CardVectorModel remainingCards;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     For each function, one-line description of the function
     //@param parameterName Describe the parameter, starting with its data type
     @return What the function returns - don't include if void. Also list special cases, such as what is returned if error.
     */

    public PlayerModel()
    {
        score = 0;

        /** Pass true so CardVectorModel knows it's a player hand and not draw/discard pile */
        hand = new CardVectorModel(true);

        books = new Vector<CardVectorModel>();
        runs = new Vector<CardVectorModel>();

        remainingCards = new CardVectorModel();

        playStatus = PlayStatus.Begun;
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


    /** Get the player hand */
    public CardVectorModel getHand()
    {
        return hand;
    }

    /**
     * Purpose: Return the player's score
     * @return an int, the score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Purpose: Return the player's score for the round
     * @return an int, the score for current round
     */
    public int getRoundScore()
    {
        return roundScore;
    }

    /**
     * Purpose: Return the card player has after making books and runs
     * @return a CardVectorModel, cards remaining
     */
    public CardVectorModel getRemainingCards() {
        return remainingCards;
    }

    /**
     * Purpose: Return the player's books
     * @return a Vector<CardVectorModel> the books
     */
    public Vector<CardVectorModel> getBooks() {
        return combinationFinder.getBooks();
    }

    /**
     * Purpose: Return the player's books
     * @return a Vector<CardVectorModel> the runs
     */
    public Vector<CardVectorModel> getRuns() {
        return combinationFinder.getRuns();
    }

    /**
     * Purpose: Return the player's play status
     * @return a PlayStatus, enum representing current state of playing
     */
    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    /**
     * Purpose: Set the player's hand and sort it
     * @param hand the hand to set to
     */
    public void setHand(CardVectorModel hand) {
        this.hand = hand;
        this.hand.sort();
    }

    /**
     * Purpose: Clear the player's hand
     */
    public void clearHand() {
        this.hand.clear();
    }

    /**
     * Purpose: Set the player's score
     * @param score the score to set to
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Purpose: Set the player's score for the round
     */
    public void setRoundScore() {
        combinationFinder = new CombinationFinder(hand);

        combinationFinder.makeCombinationsInBestOrder();
        roundScore = combinationFinder.getScoreOfRemainingCards();

        remainingCards = combinationFinder.getRemainingCards();
    }

    /** Set the pile Player will be drawing from
     *
     * @param pile the pile to draw from, true if draw pile, false if discard pile
     */
    public void setPileToDrawFrom(boolean pile) {
        pileToDrawFrom = pile;
    }

    /**
     * Purpose: Add a card to player's hand
     * @param newCard the card to ad
     */
    public void addCard(CardModel newCard)
    {
        hand.add(newCard);
    }

    /**
     * Purpose: Draw a card, discard a card, and see if player can go out
     * @param drawPile the drawing pile
     * @param discardPile the discard pile
     */
    public void play(CardVectorModel drawPile, CardVectorModel discardPile)
    {
        // Draw a card from Draw Pile or Discard Pile
        drawCard(drawPile, discardPile);

        // Discard a card
        discardCard(discardPile);

        // Check if all books and runs
        if (goOut())
        {
            playStatus = PlayStatus.GoneOut;
        } else {
            playStatus = PlayStatus.FailedGoneOut;
        }

    }


    /**
     * Purpose: Draw a card from the draw pile or discard pile
     *      If the draw pile is empty, force draw from discard pile as that pile should NEVER be empty
     * @param drawPile
     * @param discardPile
     */
    public void drawCard(CardVectorModel drawPile, CardVectorModel discardPile)
    {

        /** If draw pile is empty, force draw from discard pile */
        CardVectorModel pile = !drawPile.isEmpty() && decidePile(drawPile, discardPile) ? drawPile : discardPile;

        // Draw first card from the correct pile and update it
        addCard(pile.get(0));
        pile.remove(0);
    }

    // Which pile to draw from
    protected abstract boolean decidePile(CardVectorModel drawPile, CardVectorModel discardPile);

    // Which card to discard
    protected abstract int decideIndexOfCardToDiscard();


    /**
     * Purpose: Discard a card from player's hand
     * @param discardPile where to put the discarded card
     */
    public void discardCard(CardVectorModel discardPile)
    {
        // Pick which card to remove and remove it
        int index = decideIndexOfCardToDiscard();
        discardPile.insertElementAt(hand.get(index), 0);
        hand.remove(index);
    }


    /**
     * Purpose: Check if we can go out
     * @return boolean representing if we can go out
     */
    public boolean goOut()
    {
        combinationFinder = new CombinationFinder(hand);
        combinationFinder.makeCombinationsInBestOrder();

        if (combinationFinder.getScoreOfRemainingCards() != 0) {
            playStatus = PlayStatus.FailedGoneOut;
            return false;
        }

        // Store the remaining cards so we can display remaining cards after going out
        remainingCards = combinationFinder.getRemainingCards();


        playStatus = PlayStatus.GoneOut;
        return true;
    }

    /**
     * Purpose: Set the status of playing the player is one
     * @param playStatus the player status
     */
    public void setPlayStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
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

    // all the cards in their hand
    public String toString()
    {
        return hand.toString();
    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

//    public static void main(String[] args)
//    {
//        DeckModel deck = new DeckModel();
//
//        ComputerModel player = new ComputerModel();
//
//        // add 8 cards to hand
//        for (int count = 0; count < 50; count++)
//        {
//            player.addCard(deck.dealCard());
//        }
//
//        // should print sorted order
//        System.out.println(player.toString());
//    }
}

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************

//    // When asking for card to discard
//    protected void printHand()
//    {
//        String result = "";
//        // Print each index of card
//        for (int index = 0; index < hand.size(); index++)
//        {
//            result += (index + 1) + ". " + hand.get(index) + " ";
//        }
//        System.out.println(result);
//    }
