package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity created on app load
 @author Brendan Tunney
 @since 11/12/2019
  ******************************************************** */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make background purple
        setContentView(R.layout.activity_main);
        View someView = findViewById(R.id.startGameButton);
        View root = someView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,  R.color.holo_purple));
    }

    /**
     * Purpose: Starts a new game when start button is tapped
     * @param view the start button
     */
    public void startGame(View view) {
        Intent intent = new Intent(this, CoinTossActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    /**
     * Purpose: Load a previous game save when the load button is tapped
     * @param view the load button
     */
    public void loadGame(View view) {
//        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        requestPermissions(PERMISSIONS, 1);

        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
        finishAffinity();

    }

    /**
     * Purpose: Show game instructions when instructions button is tapped
     * @param view the instructions button
     */
    public void showInstructions(View view) {
        int bigSize = 30;
        int medSize = 20;
        int regSize = 15;
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(this);

        // Title
        textView.setText("Five Crowns");
        textView.setTextSize(bigSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("Five Crowns is a card game played by 2 players.\n");
        textView.setTextSize(regSize);
        layout.addView(textView);

        // Objective
        textView = new TextView(this);
        textView.setText("The Objective");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("The objective of this game is to score the fewest points after all the rounds.\n");
        textView.setTextSize(regSize);;
        layout.addView(textView);

        // Players
        textView = new TextView(this);
        textView.setText("The Players");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView =  new TextView(this);
        textView.setText("Two players play this game - one player will be the human user of your program, and the other player will be your program/computer.\n");
        textView.setTextSize(regSize);
        layout.addView(textView);

        // Setup
        textView = new TextView(this);
        textView.setText("The Setup");
        textView.setTextSize(medSize);
        textView.setHighlightColor(getResources().getColor(R.color.niceGreen));
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("Two 58-card decks are used. Each deck contains:\n" +
                "\t\t\u2022 Five suits: Spades, Clubs, Diamonds, Hearts and Tridents.\n" +
                "\t\t\u2022 Eleven cards in each suit: 3 through 10, Jack, Queen and King. Note no Ace, 1 or 2 cards.\n" +
                "\t\t\u2022 Three jokers. \n" +
                "\n" +
                "The values of the cards are as follows:" +
                "\n" +
                "\t\t\u2022 Cards 3-10 are worth their face value.\n" +
                "\t\t\u2022 Jack is worth 11 points, Queen is 12 and King is 13.\n" +
                "\t\t\u2022 Joker is worth 50 points.\n" +
                "\nEach round has a wild card." +
                "The wild card of a round is the card with the face value equal to the number of cards dealt in the round, e.g., in a round where players are dealt 7 cards each, 7 is the wild card.\n" +
                "The wild card is worth 20 points. \n");
        textView.setTextSize(regSize);
        layout.addView(textView);


        // Round
        textView = new TextView(this);
        textView.setText("A Round");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("A round proceeds as follows:\n" +
                "\t\t\u2022 The two decks are shuffled together.\n" +
                "\t\t\u2022 Each player is dealt n cards. " +
                "On the first round, n = 3, on the next round, n = 4, etc.\n" +
                "On the last round, n = 13.\n" +
                "\t\t\t\t\u25E6 For each round, the wild card is the card with the value of n. \n" +
                "\t\t\u2022 The remaining cards are placed in draw pile face down. The top card in the draw pile is removed from the draw pile and placed face up in discard pile.\n" +
                "\t\t\u2022 First player:\n" +
                "\t\t\t\t\u25E6 On the first round, a coin is tossed to determine who plays first. Human player is asked to call the toss.\n" +
                "\t\t\t\t\u25E6 On subsequent rounds, the player to go out first on the previous round plays first. \n" +
                "\t\t\u2022 The two players take alternate turns thereafter till the round ends.\n" +
                "\t\t\u2022 The round ends when one player goes out and the other player plays a turn to earn points.\n");
        textView.setTextSize(regSize);
        layout.addView(textView);

        // Turn
        textView = new TextView(this);
        textView.setText("A Turn");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("During her/his turn, a player plays as follows:\n" +
                "\t\t\u2022 Draws the top card from the draw pile or the top card from the discard pile.\n" +
                "\t\t\u2022 Tries to assemble the cards in hand as runs and books:\n" +
                "\t\t\t\t\u25E6 A run is a sequence of three or more cards of the same suit, e.g., 5, 6 and 7 of diamonds.\n" +
                "\t\t\t\t\u25E6 A book is three or more cards of the same value, e.g., 9 of clubs, clubs and tridents. \n" +
                "\t\t\u2022 Each card can be part of only one book or run.\n" +
                "\n" +
                "\t\t\u2022 Jokers and wild cards make it easier to assemble runs and books:\n" +
                "\t\t\t\t\u25E6 A joker can stand in for any card in a run or book, e.g., a run can be 5 of diamonds, a joker standing for 6 of diamonds and 7 of diamonds.\n" +
                "\t\t\t\t\u25E6 A wild card can stand in for any card in a run or book, e.g., on the first round when the wild card is 3, a book can be 9 of clubs, 3 of any suit and 9 of tridents. \n" +
                "\t\t\u2022 The player can have any number of wild cards and jokers in a run or book, and they can be anywhere in a run. A Joker or wild card can be moved from one book/run to another between turns, e.g., a Joker used as part of a 3-4-5 run on turn 1 can be re-purposed to be part of a 7-7-7 book on the next turn if the player picks up a 3, 4 or 5 card during turn 1.\n" +
                "\t\t\u2022 Discard one card on to the discard pile.\n" +
                "\t\t\u2022 If all the remaining cards in the player's hand can be arranged in runs and/or books, the player will go out by displaying the runs and books.\n" +
                "\t\t\u2022 If the other player has just gone out, the player will display all the runs and books in her/his hand. The sum of the values of the remaining cards in the player's hand will be the points awarded to the player.\n");
        textView.setTextSize(regSize);
        layout.addView(textView);


        // Game
        textView = new TextView(this);
        textView.setText("A Game");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("A game consists of 11 rounds:\n" +
                "\t\t\u2022 In the first round, each player is dealt 3 cards and the wild card is 3.\n" +
                "\t\t\u2022 In each subsequent round, the number of cards dealt to the players and therefore, the wild card increases by 1.\n" +
                "\t\t\u2022 In the last round, the cards dealt to each player is 13, and the wild card is King (13). \n");
        layout.addView(textView);

        // Score
        textView = new TextView(this);
        textView.setText("Score");
        textView.setTextSize(medSize);
        layout.addView(textView);
        textView = new TextView(this);
        textView.setText("\t\t\u2022 A player's score is the sum of the points earned by the player on all 11 rounds.\n" +
                "\t\t\u2022 Note that a player who goes out first in a round earns 0 points.\n" +
                "\t\t\u2022 The winner of the game is the player with the lowest score. ");
        textView.setTextSize(regSize);
        layout.addView(textView);

        // Add all text (linear layout) to display all game instructions
        scrollView.addView(layout);

        AlertDialog.Builder playerGoneOut;
        playerGoneOut = new AlertDialog.Builder(this).setTitle("How to play")
                .setView(scrollView)
                .setIcon(R.drawable.how_to_play)
                .setCancelable(false)
                .setPositiveButton("got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // todo
                    }
                });

        playerGoneOut.show();

    }



}
