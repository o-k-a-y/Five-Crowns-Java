package edu.ramapo.btunney.fivecrowns.player;

import android.util.Pair;

import java.util.Vector;

import edu.ramapo.btunney.fivecrowns.GameHistoryModel;
import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;
import edu.ramapo.btunney.fivecrowns.combination.CombinationFinder;

/*******************************************************
 Represents computer player
 @author Brendan Tunney
 @since 10/27/2019
  ******************************************************** */

public class ComputerModel extends PlayerModel
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /** The type of help ComputerModel can provide */
    public enum Help {
        DrawHelp,
        DiscardHelp,
        GoOutHelp
    }

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************


    /**
     * Purpose: Constructs object
     */
    public ComputerModel()
    {
        // Call base class cons
        super();
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

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************


    // *********************************************************
    // ******************** Code Generation ********************
    // *********************************************************

    // *********************************************************
    // ******************** Code Explanation *******************
    // *********************************************************

    // *********************************************************
    // ******************** Utility Methods ********************
    // *********************************************************


    /**
     * Purpose: Provide the Human player with suggestions on what move to make
     * @param hand the Human's hand
     * @param discardPile the discard pile
     * @param typeOfHelp type of help Human is asking for
     * @return a String, the suggestion on what to do
     */
    public static String help(CardVectorModel hand, CardVectorModel drawPile, CardVectorModel discardPile, Help typeOfHelp)
    {
        String suggestion = "";
        String reason = "";

        switch(typeOfHelp)
        {
            case DrawHelp:
                reason = "";
                String pile = "";
                CombinationFinder combinationFinder;

                // To compare if we are left with less cards after drawing from discard
                int numLeftOverWithoutDiscard = -1;
                int numLeftOverWithDiscard = -1;

                combinationFinder = new CombinationFinder(hand);
                numLeftOverWithoutDiscard = combinationFinder.getNumberOfRemainingCards();

                CardModel topOfDraw = new CardModel(drawPile.get(0));

                // Check if top of discard pile helps us making books and runs more effectively
                CardModel topOfDiscard = new CardModel(discardPile.get(0));
                CardVectorModel handWithDiscard = new CardVectorModel(hand);
                handWithDiscard.add(topOfDiscard);
                combinationFinder = new CombinationFinder(handWithDiscard);
                numLeftOverWithDiscard = combinationFinder.getNumberOfRemainingCards();


                if (topOfDiscard.isJoker() || topOfDiscard.isWildCard()) {
                    reason = "jokers and wild cards make books and runs easier";
                    pile = "discard";
                    suggestion = String.format("Computer suggests you draw %s from the %s pile because %s ", topOfDiscard, pile, reason);
                    break;
                }

                // if top of discard pile helps us making books or runs, take it (return false)
                // otherwise draw from draw pile (return true)
                if (numLeftOverWithDiscard <= numLeftOverWithoutDiscard) {
                    reason = "it left it with fewer remaining cards after making books/runs";
                    pile = "discard";
                    suggestion = String.format("Computer suggests you draw %s from the %s pile because %s ", topOfDiscard.toString(), pile, reason);

                } else {
                    reason = "the discard card did not leave fewer cards remaining";
                    pile = "draw";
                    suggestion = String.format("Computer suggests you draw %s from the %s pile because %s ", topOfDraw, pile, reason);

                }
                break;
            case DiscardHelp:
                combinationFinder = new CombinationFinder(hand);
                combinationFinder.makeCombinationsInBestOrder();

                // Get the card to remove and reason why
                Pair<Integer, String> cardAndReason = combinationFinder.getIndexOfBestCardToDiscard();
                int cardIndex = cardAndReason.first;
                reason = cardAndReason.second;
                String card = hand.get(cardIndex).toString();

                suggestion = String.format("Computer suggests you discard %s because %s", card, reason);
                break;
            case GoOutHelp:
                combinationFinder = new CombinationFinder(hand);
                combinationFinder.makeCombinationsInBestOrder();
                Vector<CardVectorModel> books = combinationFinder.getBooks();
                Vector<CardVectorModel> runs = combinationFinder.getRuns();

                // If Human can go out, inform them
                if (combinationFinder.getScoreOfRemainingCards() == 0) {
                    suggestion = "Computer suggests you go out with theses books and runs: \n";
                } else {
                    suggestion = "Computer suggests you create these books and runs: \n";

                }
                // Add each book
                suggestion += "Books: ";
                for (int book = 0; book < books.size(); book++) {
                    suggestion += books.get(book).toString() + ",  ";
                }
                suggestion += "\n";
                // Add each run
                suggestion += "Runs: ";
                for (int run = 0; run < runs.size(); run++) {
                    suggestion += runs.get(run).toString() + ",  ";
                }
                break;
        }

        GameHistoryModel gameHistoryModel = GameHistoryModel.getInstance();
        gameHistoryModel.addHistoryEvent(suggestion);

        return suggestion;
    }

    /**
     * Purpose: Return the pile Computer will be drawing from
     * @return true if drawing from draw pile, false if discard pile
     */
    public boolean decidePile(CardVectorModel drawPile, CardVectorModel discardPile)
    {
        String reason = "";
        String pile = "";
        CombinationFinder combinationFinder;

        // To compare if we are left with less cards after drawing from discard
        int numLeftOverWithoutDiscard = -1;
        int numLeftOverWithDiscard = -1;


        combinationFinder = new CombinationFinder(hand);
        combinationFinder.makeCombinationsInBestOrder();
        numLeftOverWithoutDiscard = combinationFinder.getNumberOfRemainingCards();

        CardModel topOfDiscard = new CardModel(discardPile.get(0));
        CardVectorModel handWithDiscard = new CardVectorModel(hand);
        handWithDiscard.add(topOfDiscard);
        combinationFinder = new CombinationFinder(handWithDiscard);
        combinationFinder.makeCombinationsInBestOrder();
        numLeftOverWithDiscard = combinationFinder.getNumberOfRemainingCards();


        GameHistoryModel historyModel = GameHistoryModel.getInstance();

        if (topOfDiscard.isJoker() || topOfDiscard.isWildCard()) {
            reason = "jokers and wild cards make books and runs easier";
            pile = "discard";
            historyModel.addHistoryEvent(String.format("Computer drew %s from the %s pile because %s", topOfDiscard.toString(), pile, reason));
            return false;
        }

        // if top of discard pile helps us making books or runs, take it (return false)
        // otherwise draw from draw pile (return true)
        if (numLeftOverWithDiscard <= numLeftOverWithoutDiscard) {
            reason = "it left it with fewer remaining cards after making books/runs";
            pile = "discard";

            historyModel.addHistoryEvent(String.format("Computer drew %s from the %s pile because %s", topOfDiscard.toString(), pile, reason));
            return false;
        } else {
            // Draw from draw piile
            reason = "the discard card did not leave fewer cards remaining";
            pile = "draw";
            CardModel topOfDraw = new CardModel(drawPile.get(0));


            historyModel.addHistoryEvent(String.format("Computer drew %s from the %s pile because %s", topOfDraw.toString(), pile, reason));
            return true;
        }

    }


    /**
     * Purpose: Find out which card to get rid of
     * @return the index of the card to remove
     */
    public int decideIndexOfCardToDiscard()
    {
        // find the highest value single card
        // if not, find the highest value card in partial book/run ACTUALLY NAH
        // if not, find highest value card in a book/run
        // if not, find the highest value card

        CombinationFinder combinationFinder = new CombinationFinder(hand);
        combinationFinder.makeCombinationsInBestOrder();

        // To store reason of discard
        GameHistoryModel historyModel = GameHistoryModel.getInstance();


        // Find best card to discard
        Pair<Integer, String> cardAndReason = combinationFinder.getIndexOfBestCardToDiscard();
        int index = cardAndReason.first;
        String reason = cardAndReason.second;
//        int index = combinationFinder.getIndexOfHighestValuedRemainingCard();
        if (index != -1) {
            historyModel.addHistoryEvent(String.format("Computer got rid of %s because %s", hand.get(index), reason));
            return index;

        }

        String card = hand.get(0).toString();
        historyModel.addHistoryEvent(String.format("Computer got rid of %s because it's dumb", card));
        return 0;
    }


    // *********************************************************
    // ******************** Printing Methods *******************
    // *********************************************************

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

//    public static void main( String args[] )
//    {
//
//    }
}

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************


