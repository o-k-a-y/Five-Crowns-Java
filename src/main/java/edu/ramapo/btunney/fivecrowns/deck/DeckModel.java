package edu.ramapo.btunney.fivecrowns.deck;

/*******************************************************
 Holds the cards with 5 suits, and faces 3-13
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

import android.util.Log;

import java.util.Vector;
import java.util.Collections;
import edu.ramapo.btunney.fivecrowns.card.CardModel;

public class DeckModel
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /* These are the suits of the cards in this deck */
    private static final char[] SUITS = {'s', 'c', 'd', 'h', 't'};
    /* These are the faces of the cards in this deck */
    public static final int MIN_FACE = 3, MAX_FACE = 13;
    public static final int DECK_SIZE = 58;
    /* Value of a joker */
    public static final int JOKER_VALUE = 50;



    /* Number of jokers per deck */
    private static final int NUM_OF_JOKERS = 3;



    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The vector that holds all the cards in the deck */
    private Vector<CardModel> cardVector;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************


    /**
     * Purpose: Construct deck object and initialize all cards in the deck, then shuffle it
     */
    public DeckModel()
    {
        // Allocate space to the vector of cards.
        cardVector = new Vector<CardModel>(DECK_SIZE);

        // Create a card of each face and suit and add it to the deck.
        for (int suitIndex = 0; suitIndex < SUITS.length; suitIndex++)
        {
            for (int faceIndex = MIN_FACE; faceIndex <= MAX_FACE; faceIndex++) {
                cardVector.add(new CardModel(SUITS[suitIndex], faceIndex));
            }
        }

        // Create the 3 jokers
        for (int joker = 0, value = JOKER_VALUE; joker < NUM_OF_JOKERS; joker++, value++)
        {
//            Log.d(this.getClass().toString(), Integer.toString(value+ 1));
            cardVector.add(new CardModel('j', value + 1));
        }

        Collections.shuffle(cardVector);
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
     * Purpose: Check if deck is empty
     * @return a boolean, empty or not
     */
    public boolean isEmpty()
    {
        return cardVector.size() == 0;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    /**
     @param
     @return Card, Card object that is simultaneously removed from the Deck. Returns null if deck is empty
     */
    public CardModel dealCard()
    {
        // If Deck is empty, return null
        // if (cardVector.size() == 0)
        if (isEmpty())
        {
            return null;
        }

        // Get a reference to the first card
        CardModel returnCard = cardVector.get(0);

        // Remove card from card vector
        cardVector.remove(0);

        // Return the reference to the removed first card
        return returnCard;
    }


    // Update which cards are wild
    // This is called by CardDealer's updateWildCards function
    public void updateWildCards(int face)
    {
        // Update each card
        for (CardModel card: cardVector)
        {
            card.updateWildCard(face);
        }
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
     * Purpose: Print all cards in deck
     * @return a string, all cards in deck
     */
    public String toString()
    {
        String result = "";

        for (int count = 0; count < cardVector.size(); count++)
        {
            result += cardVector.get(count).toString() + " ";
            // New line every 11 cards.
            if (count % 11 == 0)
            {
                result += "\n";
            }
        }

        return result;
    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************


}

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************