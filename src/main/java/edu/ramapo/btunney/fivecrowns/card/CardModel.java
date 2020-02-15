package edu.ramapo.btunney.fivecrowns.card;

/*******************************************************
 Card class to hold one card
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

import java.lang.Comparable; // to compare card values
import java.util.Observable;

import edu.ramapo.btunney.fivecrowns.deck.DeckModel;

public class CardModel implements Comparable<CardModel> {
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    private static final char DEFAULT_SUIT = 'X';
    private static final int DEFAULT_FACE = 0;

    /** The difference between a wild card's face value and the round number */
    public static final int WILD_CARD_OFFSET = 2;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /**
     * suit char representing the suit of the card: s/c/d/h/t
     */
    private char suit;

    /**
     * face int representing the face of the card: 3-13
     */
    private int face;

    /**
     * Whether this is a wild card for current round
     */
    private boolean wildCard;


    // Whether this is a joker
    private boolean joker = false;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     * Purpose: Constructor for CardModel that takes in a suit and face
     * @param inSuit the suit to set the card to
     * @param inFace the face to set the card to
     */
    public CardModel(char inSuit, int inFace) {
        if (!setSuit(inSuit)) {
            System.out.println("Error setting suit.");
        }
        if (!setFace(inFace)) {
            System.out.println("Error setting face.");
        }

        // By default, this is not a wild card
        wildCard = false;
    }

    /**
     * Purpose: Constructor for when making a copy of a card
     * @param card the string version of card
     */
    public CardModel(CardModel card) {
        // If Jokers
        if (card.toString().equals("J1") || card.toString().equals("J2") || card.toString().equals("J3")) {
            setSuit('J');
            setFace(Integer.parseInt(card.toString().substring(1, 2)) + DeckModel.JOKER_VALUE);
            return;
        }

        int face;
        switch (card.toString().substring(0, 1))
        {
            case "X":
                face = 10;
                break;
            case "J":
                face = 11;
                break;
            case "Q":
                face = 12;
                break;
            case "K":
                face = 13;
                break;
            default:
                face = Integer.parseInt(card.toString().substring(0, 1));
        }

        if (card.isWildCard())
        {
            wildCard = true;
        }

        char suit = card.toString().charAt(card.toString().indexOf(card.toString().substring(1, 2)));

        if (!setSuit(suit)) {
            System.out.println("Error setting suit.");
        }

        if (!setFace(face))
        {
            System.out.println("Error setting face.");
        }
    }

    /**
     * Purpose: Constructor for CardModel that takes in a card as a string and converts to CardModel
     * @param card
     */
    public CardModel(String card, int roundNumber) {
        // If Jokers
        if (card.equals("J1") || card.equals("J2") || card.equals("J3")) {
            setSuit('J');
            setFace(Integer.parseInt(card.substring(1, 2)) + DeckModel.JOKER_VALUE);
            return;
        }

        int face;
        switch (card.substring(0, 1))
        {
            case "X":
                face = 10;
                break;
            case "J":
                face = 11;
                break;
            case "Q":
                face = 12;
                break;
            case "K":
                face = 13;
                break;
            default:
                face = Integer.parseInt(card.substring(0, 1));
        }

        if ((face - WILD_CARD_OFFSET) == roundNumber)
        {
            wildCard = true;
        }

        char suit = card.charAt(card.indexOf(card.substring(1, 2)));

        if (!setSuit(suit)) {
            System.out.println("Error setting suit.");
        }

        if (!setFace(face))
        {
            System.out.println("Error setting face.");
        }
    }


    /**
     * Purpose: Default constructor for the CardModel
     *
     */
    public CardModel() {
        if (!setSuit('i')) {
            System.out.println("Error setting suit.");
        }
        if (!setFace(-1)) {
            System.out.println("Error setting face.");
        }


        // By default, this is not a wild card
        wildCard = false;
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
     * Purpose: Return the card's suit
     * @return a char, the suit
     */
    public char getSuit() {
        return suit;
    }

    /**
     * Purpose: Return the card's face
     * @return an int, the face
     */
    public int getFace() {
        return face;
    }

    /**
     * Purpose: Return the card's value
     * @return an int, the card's value
     */
    public int getValue() {
        if (joker)
        {
            return 50;
        }
        else if (wildCard)
        {
            return 20;
        }
        else
        {
            return face;
        }
    }

    /**
     * Purpose: Allow us to sort cards with Collections.sort() when contained in a Vector
     * @param right the card being compared to
     * @return an int, -1 if smaller, 1 if larger, 0 if equal
     */
    public int compareTo(CardModel right) {
        if (face < right.face) {
            return -1;
        }

        if (face > right.face) {
            return 1;
        }

        if (face == right.face) {
            if (suit > right.suit)
            {
                return 1;
            }
            else if (suit < right.suit)
            {
                return -1;
            }
            return 0;
        }

        return 0;
    }

    /**
     * Purpose: Determine if card is wild
     * @return a boolean, wild or not
     */
    public boolean isWildCard() {
        return wildCard;
    }

    /**
     * Purpose: Determine if a card is a joker
     * @return a boolean, joker or not
     */
    public boolean isJoker() {
        return joker;
    }

    /**
     * Purpose: Determine if two card's have the same suit
     * @param card the card to compare to
     * @return a boolean, equal or not
     */
    public boolean sameSuit(CardModel card) {
        return suit == card.getSuit();
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    /**
     * Purpose: Set the suit of the card
     * @param inSuit the suit to set it to
     * @return
     */
    public boolean setSuit(char inSuit) {
        boolean status = false;
        inSuit = Character.toUpperCase(inSuit);

        // For jokers
        if (inSuit == 'J') {
            suit = inSuit;
            return true;
        }

        switch (inSuit) {
            case 'S':
            case 'H':
            case 'C':
            case 'D':
            case 'T':
                suit = inSuit;
                status = true;
                break;
            default:
                suit = DEFAULT_SUIT;
                status = false;
        }

        return status;
    }

    /**
     * Purpose: Set the face value of the card
     * @param inFace the face to set it to
     * @return
     */
    public boolean setFace(int inFace) {
        boolean status = false;


        // For jokers
        if (inFace > DeckModel.JOKER_VALUE && inFace < 54) {
            face = inFace;
            joker = true;
//            Log.d(this.getClass().toString(), "here");
            return true;
        }

        // make 3 and 13 symbolic constants (static final)
        if (inFace >= DeckModel.MIN_FACE && inFace <= DeckModel.MAX_FACE) {
            face = inFace;

            status = true;
        } else {
            face = DEFAULT_FACE;
            status = false;
        }

        return status;
    }

    /**
     * Purpose: Update which cards are wild.
     *      The card passed in will be called from Deck's updateWildCards function
     * @param inFace the face of cards that will be updated to wild
     */
    public void updateWildCard(int inFace) {
        if (face == inFace && !joker) {
            wildCard = true;
        }

//        Log.d("card", toString());
//        Log.d("is wild: ", Boolean.toString(wildCard));
//        Log.d("is joker:", Boolean.toString(joker));
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

    public String toString() {
        if (joker) {
//            Log.d(this.getClass().toString(), "J" + (face - 50));
            return "J" + (face - DeckModel.JOKER_VALUE);
        }

        switch (face) {
            case 10:
                return 'X' + Character.toString(suit);
            case 11:
                return 'J' + Character.toString(suit);
            case 12:
                return 'Q' + Character.toString(suit);
            case 13:
                return 'K' + Character.toString(suit);
            default:
                return face + Character.toString(suit);
        }
    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

//    public static void main( String args[] )
//    {
//        char suitArray[] = {'s','S', 'c', 'd', 'T', 'h', 'a', 'b'};
//        int faceArray[] = {3, 2, 5, 10, 13, 8, 4, 9};
//
//        for (int counter = 0; counter < suitArray.length; counter++)
//        {
//            CardModel card = new CardModel (suitArray[counter], faceArray[counter]);
//            System.out.println(card.toString());
//        }
//    }
}


// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************



