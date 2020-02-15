package edu.ramapo.btunney.fivecrowns.card;

/*******************************************************
 Class responsible for display what a Vector of CardViews are displayed as.
        An example of a CardVectorView is for player hands and drawing piles
 @author Brendan Tunney
 @since 11/17/2019
  ******************************************************** */

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CardVectorView  {
    /** The model we care about */
    private CardVectorModel cardVectorModel;

    /** The canvas we care about */
    private LinearLayout layout;

    /**
     * Purpose: Default constructor
     */
    CardVectorView() {
        cardVectorModel = new CardVectorModel();
    }

    /**
     * Purpose: Constructor that takes in a CardVectorModel and LinearLayout
     * @param cardVectorModel either the underlying data of player's hand, the draw pile or discard pile
     * @param layout the linear layout that data exists on (linear layout of ImageButtons with image of card)
     */
    public CardVectorView(CardVectorModel cardVectorModel, LinearLayout layout) {
        this.cardVectorModel = cardVectorModel;
        this.layout = layout;
    }

    /**
     * Purpose: Takes in a Vector of CardModels and a linear layout to draw the cards to
     * @param onClickListener a click listener to set the cards clickable if its the Human's hand
     */
    public void updateLinearLayout(View.OnClickListener onClickListener) {
        /** Remove all the Views within the linear layout */
        layout.removeAllViews();


        /** Draw each card in the vector of cards */
        for (int i = 0; i < cardVectorModel.size(); i++)
        {
            ImageButton cardButton = new ImageButton(layout.getContext());
            cardButton.setOnClickListener(onClickListener);

            CardView cardView = new CardView(cardButton, cardVectorModel.get(i));
            cardView.drawCardImage();
            layout.addView(cardButton);
        }
    }

    /**
     * Purpose: Set the first card to be clickable if it's the draw/discard pile
     */
    public void setOnlyZerothCardClickable() {
        if (!cardVectorModel.isPlayerHand()) {
            /** Make sure we have at least 1 card */
            if (layout.getChildCount() < 1) {
                return;
            }

            layout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
//                    Log.d("CARDVECTORVIEW 0th", "button clicked!");
//                    (LinearLayout) v.getParent()

                }
            });
        }

    }

    /**
     * Purpose: Add card to front
     * @param card the card to be added
     */
    public void addCardToFront(CardModel card) {
        cardVectorModel.insertElementAt(card, 0);
    }

    /**
     * Purpose: Remove and return the CardModel at specified index.
     *      Called by RoundView
     * @param index the index of the CardModel to remove
     * @return the CardModel that was removed
     */
    public CardModel discardCardAt(int index) {
        return cardVectorModel.discardCardAt(index);
    }


    /**
     * Purpose: Remove all onClick listeners of layout
     *      Main purpose is so we can't click a card in human's hand to accidentally discard at the wrong time
     */
    public void removeOnClicks() {
        for (int i = 0; i < layout.getChildCount();  i++) {
            layout.getChildAt(i).setOnClickListener(null);
        }
    }

}
