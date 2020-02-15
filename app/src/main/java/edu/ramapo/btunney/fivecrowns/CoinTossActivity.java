package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity that lets user toss a coin to determine if they will play first
 @author Brendan Tunney
 @since 11/12/2019
  ******************************************************** */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CoinTossActivity extends AppCompatActivity {

    // The intent we will pass to RoundActivity
    private Intent intent;

    // The Button that will start a new game
    private View startGameButton;

    // The TextView where we are displaying who will go first
    private TextView first;

    // The TextView that explains why the call was correct/not
    private TextView result;

    // The TextView informing user to make a coin toss
    private TextView coinTossTextView;

    // The head and tails buttons
    private View headsButton;
    private View tailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        // Make background purple
        setContentView(R.layout.activity_coin_toss);
        View someView = findViewById(R.id.headsButton);
        View root = someView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,  R.color.holo_purple));

        /** Make start game button invisible */
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setVisibility(View.GONE);

        // Create intent to pass to RoundActivity to start Round 1
        intent = new Intent(this, RoundActivity.class);

        coinTossTextView = findViewById(R.id.coinTossText);
        headsButton = findViewById(R.id.headsButton);
        tailsButton = findViewById(R.id.tailsButton);

        // The TextView where we are displaying who will go first
        first = findViewById(R.id.firstPlayer);
        result = findViewById(R.id.tossResultTextView);

        makeTextGolden();
    }

    /**
     * Purpose: Toss a coin to determine first player
     * @param view
     */
    public void tossCoin(View view) {
        // Randomly choose first player
        Random rand = new Random();
        int firstPlayer = rand.nextInt(2);
        Log.d("firstPlayer", Integer.toString(firstPlayer));

        String[] headsOrTails = {"heads", "tails"};
        String call = ((Button) view).getText().toString();


        result.setText(String.format("Call was %s, result was %s.", call, headsOrTails[firstPlayer]));

        if (call.equals(headsOrTails[firstPlayer])) {
            // human goes first
            intent.putExtra(RoundActivity.firstPlayerExtra, 1);
            first.setText("Human will play first!");
        } else {
            // computer goes first
            intent.putExtra(RoundActivity.firstPlayerExtra, 0);
            first.setText("Computer will play first!");

        }

        // Set the startGameButton to visible, and the rest to invisible
        startGameButton.setVisibility(View.VISIBLE);
        coinTossTextView.setVisibility(View.INVISIBLE);
        headsButton.setVisibility(View.INVISIBLE);
        tailsButton.setVisibility(View.INVISIBLE);

    }

    /**
     * Purpose: Start a new game
     * @param view button tapped
     */
    public void startGame(View view) {
        startActivity(intent);
        finishAffinity();
    }


    /**
     * Purpose: Make textviews on screen golden
     */
    private void makeTextGolden() {
        first.setTextColor(getResources().getColor(R.color.golden));
        result.setTextColor(getResources().getColor(R.color.golden));
        coinTossTextView.setTextColor(getResources().getColor(R.color.golden));
    }

}
