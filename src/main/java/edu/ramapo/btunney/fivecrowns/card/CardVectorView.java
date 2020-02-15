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
//    private Vector<CardModel> cardVectorView;
    /** The model we care about */
    private CardVectorModel cardVectorModel;

    /** The canvas we care about */
    private LinearLayout layout;

    /** Default constructor */
    CardVectorView() {
        cardVectorModel = new CardVectorModel();
    }

    /** Constructor that takes in ImageButton and CardModel */
    public CardVectorView(CardVectorModel cardVectorModel, LinearLayout layout) {
        this.cardVectorModel = cardVectorModel;
        this.layout = layout;
    }

    /** Takes in a Vector of CardModels and a linear layout to draw the cards to  */
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

    /** Set the first card to be clickable if it's the draw/discard pile
     *
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

    /** Add card to front
     *
     * @param card the card to be added
     */
    public void addCardToFront(CardModel card) {
        cardVectorModel.insertElementAt(card, 0);
    }

    /** Remove and return the CardModel at specified index.
     * Called by RoundView
     *
     * @param index the index of the CardModel to remove
     * @return the CardModel that was removed
     */
    public CardModel discardCardAt(int index) {
        return cardVectorModel.discardCardAt(index);
    }


    /** Remove all onClick listeners of layout
     *
     */
    public void removeOnClicks() {
        for (int i = 0; i < layout.getChildCount();  i++) {
            layout.getChildAt(i).setOnClickListener(null);
        }
    }

}
