package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity that displays all the scores of rounds played and winner of game
 @author Brendan Tunney
 @since 12/09/2019
  ******************************************************** */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EndScreenActivity extends AppCompatActivity {

    private TextView winnerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        Intent intent = getIntent();
        int computerScore = intent.getIntExtra("computerScore", -1);
        int humanScore = intent.getIntExtra("humanScore", -1);

        // Make background purple
        setContentView(R.layout.activity_end_screen);
        View someView = findViewById(R.id.homeButton);
        View root = someView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,  R.color.holo_purple));

        // Make text golden
        winnerTextView = findViewById(R.id.winnerTextView);
        winnerTextView.setTextColor(getResources().getColor(R.color.golden));

        displayWinnerAndScores(computerScore, humanScore);
    }

    /**
     * Purpose: Go to main activity and allow user to play/load another game
     * @param view
     */
    public void homeOnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    /**
     * Purpose: Display who won the game and all previous scores
     * @param computerScore
     * @param humanScore
     */
    private void displayWinnerAndScores(int computerScore, int humanScore) {
        GameHistoryModel gameHistoryModel = GameHistoryModel.getInstance();

        ArrayList<String> scoreHistory = gameHistoryModel.getScoreHistory();

        TextView winner = findViewById(R.id.winnerTextView);

        if (humanScore < computerScore) {
            winner.setText("Human is the winner!");
        } else if (humanScore > computerScore) {
            winner.setText("Computer is the winner!");
        } else {
            winner.setText("Game was a tie!");
        }

        LinearLayout layout = findViewById(R.id.scoreHistoryLayout);

        for (int i = scoreHistory.size() -1; i >= 0; i--) {
            TextView roundScore = new TextView(this);
            roundScore.setText(scoreHistory.get(i));
            roundScore.setTextColor(getResources().getColor(R.color.golden));
            layout.addView(roundScore);
        }
    }
}
