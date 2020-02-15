package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 View for the Round; contains all cards displayed on screen
 @author Brendan Tunney
 @since 11/24/2019
  ******************************************************** */

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorView;

public class RoundView {
    /** The Views we care about */
    private CardVectorView computerHandView;
    private CardVectorView humanHandView;
    private CardVectorView drawPileView;
    private CardVectorView discardPileView;

    /** The model we care about */
    private RoundModel roundModel;

    /** The canvas we care about */
    private TextView currentRoundTextView;
    private TextView computerScoreTextView;
    private TextView humanScoreTextView;
    private TextView currentWildCardTextView; //
    private TextView currentPlayerTextView; //
    private View helpButton;
    private View goOutButton;

    /** Overloaded constructor that draws the player's hands, draw/discard pile, as well as updates the scores and current round number displayed */
    public RoundView(RoundModel roundModel, View helpButton, View goOutButton, TextView currentRoundTextView, TextView computerScoreTextView, TextView humanScoreTextView, TextView currentWildCardTextView) {
        /** Get the players' hand */
        CardVectorModel computerHand = roundModel.getComputerHand();
        CardVectorModel humanHand = roundModel.getHumanHand();

        /** Get the draw and discard piles */
        CardVectorModel drawPile = roundModel.getDrawPile();
        CardVectorModel discardPile = roundModel.getDiscardPile();

        /** Get the LinearLayouts of each card pile */
        LinearLayout computerHandLayout = ((Activity) currentRoundTextView.getContext()).findViewById(R.id.computerHand);
        LinearLayout humanHandLayout = ((Activity) currentRoundTextView.getContext()).findViewById(R.id.humanHand);
        LinearLayout drawPileLayout = ((Activity) currentRoundTextView.getContext()).findViewById(R.id.drawPile);
        LinearLayout discardPileLayout = ((Activity) currentRoundTextView.getContext()).findViewById(R.id.discardPile);


        /** Create and set each CardVectorView */
        this.computerHandView = new CardVectorView(computerHand, computerHandLayout);
        this.humanHandView = new CardVectorView(humanHand, humanHandLayout);
        this.drawPileView = new CardVectorView(drawPile, drawPileLayout);
        this.discardPileView = new CardVectorView(discardPile, discardPileLayout);

        this.roundModel = roundModel;

        /** Set TextViews */
        this.currentRoundTextView = currentRoundTextView;
        this.computerScoreTextView = computerScoreTextView;
        this.humanScoreTextView  = humanScoreTextView;
        this.currentWildCardTextView = currentWildCardTextView;

        /** Set the buttons */
        this.helpButton = helpButton;
        this.goOutButton = goOutButton;

        setCurrentRoundText();
        setCurrentScoreText();
        setCurrentWildCardTextView();
    }

    /** Update the current round being displayed */
    public void setCurrentRoundText() {
        int currentRound = roundModel.getRoundNumber();
        currentRoundTextView.setText(String.format("Current round: %d", currentRound));
    }

    /** Update the current scores being displayed */
    public void setCurrentScoreText() {
        int computerScore = roundModel.getPlayerScore(RoundModel.PlayerType.Computer);
        int humanScore = roundModel.getPlayerScore(RoundModel.PlayerType.Human);

        computerScoreTextView.setText(String.format("Score: %d", computerScore));
        humanScoreTextView.setText((String.format("Score: %d", humanScore)));
    }

    /** Update the current wild card being displayed */
    public void setCurrentWildCardTextView() {
        int wildCardNumber = roundModel.getRoundNumber() + CardModel.WILD_CARD_OFFSET;

        String wildCard = "";
        switch (wildCardNumber) {
            case 3:
                wildCard = "3";
                break;
            case 4:
                wildCard = "4";
                break;
            case 5:
                wildCard = "5";
                break;
            case 6:
                wildCard = "6";
                break;
            case 7:
                wildCard = "7";
                break;
            case 8:
                wildCard = "8";
                break;
            case 9:
                wildCard = "9";
                break;
            case 10:
                wildCard = "X";
                break;
            case 11:
                wildCard = "Jack";
                break;
            case 12:
                wildCard = "Queen";
                break;
            case 13:
                wildCard = "King";
                break;
            default:
                wildCard = "Error";
                break;
        }

        currentWildCardTextView.setText(String.format("Current wild card: %s", wildCard));
    }

    /**
     * Purpose: Set the top of the discard and draw pile to be  clickable
     * @param cardPile the card pile to make the first index clickable
     */
    public void setTopOfCardVectorClickable(RoundActivity.CardVector cardPile) {
        if (RoundActivity.CardVector.DiscardPile == cardPile) {
            discardPileView.setOnlyZerothCardClickable();
        }
        else if (RoundActivity.CardVector.DrawPile == cardPile) {
            drawPileView.setOnlyZerothCardClickable();
        }
    }

    /**
     * Purpose: Update all the Views for every card within the current RoundModel
     * ComputerHandView and HumanHandView pass an OnClickListener lambda object to bind to each
     * card in the hand so that when the player taps "Discard" they can tap which card in their
     * hand will be discarded
     */
    public void updateGameCards() {

        computerHandView.updateLinearLayout(null);
        humanHandView.updateLinearLayout(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int indexOfCard = ((ViewGroup) v.getParent()).indexOfChild(v);
                discardCardAt(indexOfCard);
                roundModel.makeMove(RoundModel.MoveType.DiscardFromHand);

//                v.setBackgroundColor(Color.GREEN);
                updateGameCards();

                // Disallow player to remove more cards than they should be able to
                removeHumanHandOnClicks();

                // Enable help and go out buttons
                helpButton.setEnabled(true);
                goOutButton.setEnabled(true);
                goOutButton.setBackgroundColor(Color.GREEN);
                Log.d("Human hand index:", Integer.toString(indexOfCard));
            }
        });
        drawPileView.updateLinearLayout(null);
        discardPileView.updateLinearLayout(null);

    }

    /**
     * Purpose: Return and discard the card at the specified index
     *      and then add that card to the front of the discard pile
     * @param index the index of the card to discard and return
     */
    private void discardCardAt(int index) {
        CardModel card = humanHandView.discardCardAt(index);
        Log.d("discard card from human hand:", card.toString());
        discardPileView.addCardToFront(card);
    }


    /**
     * Purpose: Remove all onClick listeners on the Human hand
     */
    private void removeHumanHandOnClicks() {
        humanHandView.removeOnClicks();
    }
}
