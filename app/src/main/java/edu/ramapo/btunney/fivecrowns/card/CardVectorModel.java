package edu.ramapo.btunney.fivecrowns.card;

/*******************************************************
 Represents computer hand, human hand, draw pile, and discard pile
 @author Brendan Tunney
 @since 11/17/2019
  ******************************************************** */

import android.util.Log;
import java.util.Collections;
import java.util.Observable;
import java.util.Vector;

public class CardVectorModel {

    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************


    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** Underyling data being represented */
    private Vector<CardModel> cardModelVector;

    /** Whether or not it's a player's hand */
    private boolean playerHand = false;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     For each function, one-line description of the function
     @param parameterName Describe the parameter, starting with its data type
     @return What the function returns - don't include if void. Also list special cases, such as what is returned if error.
     */
    /** Default constructor */
    public CardVectorModel() {
        cardModelVector = new Vector<CardModel>(3,3);

    }


    /** Overloaded constructor that takes in whether or not it's a player hand (computer or human)
     *
     * @param inPlayerHand whether or not it's a player's hand
     */
    public CardVectorModel(boolean inPlayerHand) {
        playerHand = true;
        cardModelVector = new Vector<CardModel>(3,3);
    }

    /** Overloaded constructor that takes in initialCapacity and capacitiyIncrement.
     *
     * @param initialCapacity the initial capacity of the Vector
     * @param capacityIncrement how much to increment by when Vector reaches capacity
     */
    public CardVectorModel(int initialCapacity, int capacityIncrement) {
        cardModelVector = new Vector<CardModel>(initialCapacity, capacityIncrement);
    }

    /** Overloaded constructor that takes in initialCapacity and capacitiyIncrement.
     *
     * @param initialCapacity the initial capacity of the Vector
     */
    public CardVectorModel(int initialCapacity) {
        cardModelVector = new Vector<CardModel>(initialCapacity);
    }

    /**
     * Purpose: Copy constructor
     * @return CardVectorModel
     */
    public CardVectorModel(CardVectorModel o) {
        cardModelVector = new Vector<CardModel>(3,3);

        Vector<CardModel> cards = o.getCards();
        for (int i = 0; i < cards.size(); i++) {
            cardModelVector.add(new CardModel(cards.get(i)));
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

    /**
     * Purpose: Returns the cards in the Vector<CardModel>
     * @return a Vector<CardModel> the cards
     */
    public Vector<CardModel> getCards() {
        return cardModelVector;
    }

    /** Return CardModel at index */
    public CardModel get(int index) {
        return cardModelVector.get(index);
    }

    /** Is the Vector of CardModels empty */
    public boolean isEmpty() {
        return cardModelVector.isEmpty();
    }

    /** Return the size of the vector */
    public int size() {
        return cardModelVector.size();
    }

    /**
     * Purpose: Return the CardModel at given element
     * @param index index of the CardVectorModel
     * @return
     */
    public CardModel elementAt(int index) {
        return cardModelVector.elementAt(index);
    }

    /** Is the CardVectorModel a Player hand*/
    public boolean isPlayerHand() {
        return playerHand;
    }

    /**
     * Purpose: Return the last element
     * @return CardModel at the last element
     */
    public CardModel lastElement() {
        return cardModelVector.lastElement();
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    /**
     * Purpose: Clear all the cards in the list
     */
    public void clear() {
        cardModelVector.clear();
    }

    /**
     * Purpose: Sort the list of cards
     */
    public void sort()
    {
        Collections.sort(cardModelVector);
    }

    /**
     * Purpose: Add a card to CardVectorModel
     * @param newCard card to add
     */
    public void add(CardModel newCard)
    {
        cardModelVector.add(newCard);

        /** Sort the cards if it's the player's hand (we don't want to sort the draw pile */
        if (playerHand) {
            Collections.sort(cardModelVector);
        }
    }


    /**
     * Purpose: Add a card to CardVectorModel
     * @param index index to add it at
     * @param card card to add
     */
    public void add(int index, CardModel card) {
        cardModelVector.add(index, card);
    }


    /** Remove and return the CardModel at specified index.
     * Called by CardVectorView
     *
     * @param index the index of the CardModel to remove
     * @return the CardModel that was removed
     */
    public CardModel discardCardAt(int index) {
        CardModel card = cardModelVector.get(index);
        cardModelVector.remove(index);
        return card;
    }

    /** Remove CardModel at index */
    public void remove(int index) {
        // TODO: add validation to see if it's valid
        if (index < 0 || index > cardModelVector.size())
        {
            Log.d(this.getClass().getSimpleName(), "index out of bounds when discarding");
            return;
        }
        cardModelVector.remove((index));
    }

    /** Add a CardModel to Vector at index */
    public void insertElementAt(CardModel cardModel, int index) {
        cardModelVector.insertElementAt(cardModel, index);
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

    /** Return a string which contains all of the cards in the Vector of CardModel */
    public String toString() {
        String result = "";

        for (CardModel card: cardModelVector)
        {
            result += card.toString() + " ";
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
/** Constructor that takes in a Vector<CardModel> and sets to private member */
//    public CardVectorModel(Vector<CardModel> cardModelVector) {
//        /** Deep copy */
//        this.cardModelVector = new Vector<CardModel>(cardModelVector);
//    }
