package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Deals card from two deck
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

import java.util.Random;

import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.deck.DeckModel;

public class CardDealerModel
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /** The number of decks CardDealer has */
    public static final int NUM_DECKS = 2;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The two decks from which cards are dealt */
    private DeckModel firstDeck, secondDeck;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************


    /**
     * Purpose: Constructor for class
     */
    public CardDealerModel()
    {
        // Create and shuffle the two decks
        firstDeck = new DeckModel();
        secondDeck = new DeckModel();
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
     * Purpose: Return a card to the Player who is drawing
     * @return a CardModel, the card randomly chosen by CardDealerModel
     */
    public CardModel dealCard()
    {
        // Randomly deal a card from one of the two decks until both are empty
        if (firstDeck.isEmpty())
        {
            if (secondDeck.isEmpty())
            {
                return null;
            }
            else
            {
                return secondDeck.dealCard();
            }
        }
        else
        {
            if (secondDeck.isEmpty())
            {
                return firstDeck.dealCard();
            }
            else
            {
                // Randomly choose which non-empty deck to draw from
                int choice = new Random().nextInt() % 2;

                if (0 == choice)
                {
                    return firstDeck.dealCard();
                }
                else
                {
                    return secondDeck.dealCard();
                }
            }
        }
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    /**
     * Purpose: Update which cards are wild depending on round
     * @param face the face of the cards that are now wild (i.e. 11 means Jacks are wild)
     */
    public void updateWildCards(int face)
    {
        firstDeck.updateWildCards(face);
        secondDeck.updateWildCards(face);
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
     * Purpose: To allow printing of decks owned by CardDealerModel
     * @return a string, with both decks concatenated
     */
    public String toString()
    {
        return "Deck 1: " + firstDeck.toString() + "\n" +
                "Deck 2: " + secondDeck.toString();
    }

}
