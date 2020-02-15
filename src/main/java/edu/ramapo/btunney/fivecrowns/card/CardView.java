package edu.ramapo.btunney.fivecrowns.card;

/*******************************************************
 Class responsible for showing what each card is displayed as
 @author Brendan Tunney
 @since 11/13/2019
  ******************************************************** */

import android.widget.ImageButton;
import edu.ramapo.btunney.fivecrowns.R;

class CardView {
    /**
     * The CardModel we are observing
     */
    private CardModel cardModel;

    /**
     * The image for the CardView
     */
    private ImageButton cardButton;


    /**
     * Default constructor
     */
    CardView() {

        cardModel = new CardModel();
    }

    /**
     * Constructor that takes in ImageButton and CardModel
     */
    CardView(ImageButton cardButton, CardModel cardModel) {
        this.cardButton = cardButton;
        this.cardModel = cardModel;
    }

    /**
     * Purpose: Change the image the card displays
     */
    void drawCardImage() {

        int face = cardModel.getFace();
        char suit = cardModel.getSuit();


        // If it's a joker
        if (cardModel.toString().equals("J1")) {
            cardButton.setImageResource(R.drawable.joker_1);
            cardButton.setPadding(10, 2, 10,2);
            return;
        } else if (cardModel.toString().equals("J2")) {
            cardButton.setImageResource(R.drawable.joker_2);
            cardButton.setPadding(10, 2, 10,2);
            return;
        } else if (cardModel.toString().equals("J3")) {
            cardButton.setImageResource(R.drawable.joker_3);
            cardButton.setPadding(10, 2, 10,2);
            return;
        }


        switch (face) {
            case 3:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.three_c));
                        //cardButton.setTag(R.integer.cardKey, "3C");
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.three_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.three_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.three_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.three_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.three_err));
                        break;
                }
                break;
            case 4:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.four_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.four_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.four_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.four_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.four_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.four_err));
                        break;
                }
                break;
            case 5:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.five_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.five_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.five_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.five_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.five_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.five_err));
                        break;
                }
                break;
            case 6:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.six_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.six_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.six_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.six_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.six_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.six_err));
                        break;
                }
                break;
            case 7:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.seven_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.seven_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.seven_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.seven_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.seven_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.seven_err));
                        break;
                }
                break;
            case 8:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.eight_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.eight_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.eight_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.eight_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.eight_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.eight_err));
                        break;
                }
                break;
            case 9:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.nine_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.nine_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.nine_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.nine_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.nine_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.nine_err));
                        break;
                }
                break;
            case 10:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.ten_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.ten_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.ten_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.ten_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.ten_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.ten_err));
                        break;
                }
                break;
            case 11:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.jack_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.jack_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.jack_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.jack_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.jack_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.jack_err));
                        break;
                }
                break;
            case 12:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.queen_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.queen_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.queen_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.queen_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.queen_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.queen_err));
                        break;
                }
                break;
            case 13:
                switch (suit) {
                    case 'C':
                        cardButton.setImageResource((R.drawable.king_c));
                        break;
                    case 'D':
                        cardButton.setImageResource((R.drawable.king_d));
                        break;
                    case 'H':
                        cardButton.setImageResource((R.drawable.king_h));
                        break;
                    case 'S':
                        cardButton.setImageResource((R.drawable.king_s));
                        break;
                    case 'T':
                        cardButton.setImageResource((R.drawable.king_t));
                        break;
                    default:
                        cardButton.setImageResource((R.drawable.king_err));
                        break;
                }
                break;
            default:
                cardButton.setImageResource((R.drawable.ic_launcher_background));
                break;


        }
//        cardButton.setBackgroundColor(Color.GREEN);
        cardButton.setPadding(10, 2, 10,2);

    }


}
