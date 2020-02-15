package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity for each Round that's created when game starts.
 @author Brendan Tunney
 @since 11/12/2019
  ******************************************************** */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;
import edu.ramapo.btunney.fivecrowns.player.ComputerModel;
import edu.ramapo.btunney.fivecrowns.player.PlayerModel;

public class RoundActivity extends AppCompatActivity {

    public enum CardVector {
        DrawPile,
        DiscardPile,
        ComputerHand,
        HumanHand
    }

    // Where we are displaying the lists of cards
    private LinearLayout humanHandLayout;
    private LinearLayout computerHandLayout;
    private LinearLayout drawPileLayout;
    private LinearLayout discardPileLayout;

    // All the buttons that aren't in a Linear Layout
    private Vector<View> roundButtons;

    /* View for the current Round
        Contains the player hands, and draw/discard pile
    */
    private RoundView roundView;

    /** Current round of game */
    private RoundModel roundModel;

    /** Game object containing functionality to get next round */
    private GameModel gameModel;

    // The first player determined by CoinTossActivity and passed through the Intent
    public static final String firstPlayerExtra = "firstPlayer";

    // To check if we are loading a game or starting a new one
    public static final String loadedGameExtra = "loadedGame";

    // The name of a file if any exists of the loaded game file
    public static final String fileNameExtra = "fileName";

    public static final String fileFromAssetsExtra = "fileFromAssets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);


        // Linear layouts that hold the cards as ImageButtons
        humanHandLayout = findViewById(R.id.humanHand);
        computerHandLayout = findViewById(R.id.computerHand);
        drawPileLayout = findViewById(R.id.drawPile);
        discardPileLayout = findViewById(R.id.discardPile);

        // The buttons/game options
        roundButtons = new Vector<View>(6,4);
        roundButtons.add(findViewById(R.id.drawButton));
        roundButtons.add(findViewById(R.id.discardButton));
        roundButtons.add(findViewById(R.id.saveButton));
        roundButtons.add(findViewById(R.id.helpButton));
        roundButtons.add(findViewById(R.id.nexTurnButton));
        roundButtons.add(findViewById(R.id.goOutButton));


        // Check intent passed
        Intent intent = getIntent();
        Boolean loadedGame = intent.getBooleanExtra(loadedGameExtra, false);
        if (loadedGame) {
            try {
                loadGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            startGame();
        }
    }


    /**
     * Quit the app when the "QUIT" button is tapped
     * @param view the button that was tapped (quitButton)
     */
    public void quitGameOnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);


    }

    /**
     * Draw card from either the draw pile or discard pile
     * TODO: make it so it doesn't just draw from the draw pile
     * @param view the button that was tapped (drawButton)
     */
    public void drawCardOnClick(View view) {
        if (roundModel.makeMove(RoundModel.MoveType.Draw)) {
            roundView.updateGameCards();

//            final RoundModel onClickRoundModel = roundModel;

            roundView.setTopOfCardVectorClickable(CardVector.DrawPile);
            roundView.setTopOfCardVectorClickable(CardVector.DiscardPile);

            final LinearLayout onClickDrawPileLinearLayout = findViewById(R.id.drawPile);
            final LinearLayout onClickDiscardPileLinearLayout = findViewById(R.id.discardPile);

            // Make sure we have at least 1 card
            if (onClickDrawPileLinearLayout.getChildCount() < 1) {
                return;
            }
            if (onClickDiscardPileLinearLayout.getChildCount() < 1) {
                return;
            }

            // Make background green for top of draw/discards piles for usability
            ImageButton topOfDiscard = (ImageButton) onClickDiscardPileLinearLayout.getChildAt(0);
            topOfDiscard.setBackgroundColor(Color.GREEN);
            ImageButton topOfDraw = (ImageButton) onClickDrawPileLinearLayout.getChildAt(0);
            topOfDraw.setBackgroundColor(Color.GREEN);

            // Dynamic on click listener for top of draw pile
            onClickDrawPileLinearLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Log.d("draw pile", "button clicked!");
                    roundModel.makeMove(RoundModel.MoveType.DrawFromDrawPile);
                    roundView.updateGameCards();

                    enableButton(findViewById(R.id.discardButton), true);
                    enableLinearLayout(drawPileLayout, false);
                    enableLinearLayout(discardPileLayout, false);

                    // Make discard button green
                    Button discardButton = findViewById(R.id.discardButton);
                    makeButtonGreen(discardButton, true);
                }
            });

            // Dynamic on click listener for top of discard pile
            onClickDiscardPileLinearLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Log.d("discard pile", "button clicked!");
                    roundModel.makeMove(RoundModel.MoveType.DrawFromDiscardPile);
                    roundView.updateGameCards();

                    enableButton(findViewById(R.id.discardButton), true);
                    enableLinearLayout(drawPileLayout, false);
                    enableLinearLayout(discardPileLayout, false);

                    // Make discard button green
                    Button discardButton = findViewById(R.id.discardButton);
                    makeButtonGreen(discardButton, true);
                }
            });


            // If tapping Draw is a valid move, set all cards to be disabled except top of both draw piles and help
            enableEverything(false);
            enableLinearLayout(drawPileLayout, true);
            enableLinearLayout(discardPileLayout, true);
            enableButton(findViewById(R.id.helpButton), true);
        }
    }

    /**
     * Purpose: Discard card from hand
     * @param view the button that was tapped (discardButton)
     */
    public void discardCardOnClick(View view) {
        roundModel.makeMove(RoundModel.MoveType.Discard);
        roundView.updateGameCards();


        /** Disable everything, enable human hand and help */
        enableEverything(false);
        enableLinearLayout(humanHandLayout, true);
        enableButton(findViewById(R.id.helpButton), true);

    }

    /**
     * Purpose: Attempt to go out. Increments the player index so that it should be the computer's turn after pressing
     * @param view the button that was tapped (goOutButton)
     */
    public void goOutOnClick(View view) {
        roundModel.makeMove(RoundModel.MoveType.GoOut);


        // If human has gone out
        if (roundModel.playerList[1].getPlayStatus() == PlayerModel.PlayStatus.GoneOut) {
            Vector<CardVectorModel> books = roundModel.playerList[1].getBooks();
            Vector<CardVectorModel> runs = roundModel.playerList[1].getRuns();

            final ScrollView scrollView = new ScrollView(this);

            // To hold books and runs
            // TODO: make a divider so books are to the left, runs are to the rights
            final LinearLayout booksAndRunsLayout = new LinearLayout(this);

            // Books
            final LinearLayout booksLayout = new LinearLayout(this);
            booksLayout.setOrientation(LinearLayout.VERTICAL);
            booksLayout.setGravity(Gravity.LEFT);
            TextView label = new TextView(this);
            label.setText("Books:");
            booksLayout.addView(label);

            // Runs
            final LinearLayout runsLayout = new LinearLayout(this);
            runsLayout.setOrientation(LinearLayout.VERTICAL);
            runsLayout.setGravity(Gravity.RIGHT);
            label = new TextView(this);
            label.setText("Runs:");
            runsLayout.addView(label);

            booksAndRunsLayout.addView(booksLayout);
            booksAndRunsLayout.addView(runsLayout);

            scrollView.addView(booksAndRunsLayout);

            LinearLayout layout = new LinearLayout(this);
            for (int i = 0; i < books.size(); i++) {
                layout = combinationToLayout(books.get(i));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.LEFT);
                booksLayout.addView(layout);

            }
            for (int i = 0; i < runs.size(); i++) {
                layout = combinationToLayout(runs.get(i));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.RIGHT);
                runsLayout.addView(layout);
            }

            AlertDialog.Builder playerGoneOut;
            playerGoneOut = new AlertDialog.Builder(this).setTitle("Human went out!")
                    .setMessage("Books and runs made:")
                    .setView(scrollView)
                    .setIcon(R.drawable.human)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // todo
                        }
                    });

            playerGoneOut.show();
        }

        enableEverything(false);
        enableButton(findViewById(R.id.nexTurnButton), true);
        Button nextButton = findViewById(R.id.nexTurnButton);

        // Change color of next button to green for usability
        nextButton.setBackgroundColor(Color.GREEN);
        enableButton(findViewById(R.id.saveButton), true);

        // Set human's textview to inactive, computer's to active
        TextView humanPlayerTextView = findViewById(R.id.humanTextView);
        humanPlayerTextView.setBackgroundColor(Color.TRANSPARENT);
        TextView computerPlayerTextView = findViewById(R.id.computerTextView);
        computerPlayerTextView.setBackgroundColor(Color.GREEN);


        // If computer player went out in the previous turn, the round is over!
        final LinearLayout layout = cardsRemainingToView();
        if (roundModel.playerList[0].getPlayStatus() == PlayerModel.PlayStatus.GoneOut) {
            AlertDialog roundOver = new AlertDialog.Builder(this).setTitle("Round is over!")
                    .setView(layout)
                    .setIcon(R.drawable.arrow)
                    .setCancelable(false)
                    .setPositiveButton("Next round", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addScoreHistory();
                            nextRound();
                        }
                    })
                    .show();
        }
    }


    /**
     * Purpose: Continue to the next turn to make Computer move
     * @param view the button that was tapped (nextTurnButton)
     */
    public void nextTurnOnClick(View view) {
        roundModel.makeMove(RoundModel.MoveType.NextTurn);

        displayComputersTurnReasoning(view);

        roundView.updateGameCards();

        // If computer has gone out
        if (roundModel.playerList[0].getPlayStatus() == PlayerModel.PlayStatus.GoneOut) {
            Vector<CardVectorModel> books = roundModel.playerList[0].getBooks();
            Vector<CardVectorModel> runs = roundModel.playerList[0].getRuns();

            final ScrollView scrollView = new ScrollView(this);

            // To hold books and runs
            // TODO: make a divider so books are to the left, runs are to the rights
            final LinearLayout booksAndRunsLayout = new LinearLayout(this);

            // Books
            final LinearLayout booksLayout = new LinearLayout(this);
            booksLayout.setOrientation(LinearLayout.VERTICAL);
            booksLayout.setGravity(Gravity.LEFT);
            TextView label = new TextView(this);
            label.setText("Books:");
            booksLayout.addView(label);

            // Runs
            final LinearLayout runsLayout = new LinearLayout(this);
            runsLayout.setOrientation(LinearLayout.VERTICAL);
            runsLayout.setGravity(Gravity.RIGHT);
            label = new TextView(this);
            label.setText("Runs:");
            runsLayout.addView(label);

            booksAndRunsLayout.addView(booksLayout);
            booksAndRunsLayout.addView(runsLayout);

            scrollView.addView(booksAndRunsLayout);

            LinearLayout layout = new LinearLayout(this);
            for (int i = 0; i < books.size(); i++) {
                layout = combinationToLayout(books.get(i));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.LEFT);
                booksLayout.addView(layout);

            }
            for (int i = 0; i < runs.size(); i++) {
                layout = combinationToLayout(runs.get(i));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.RIGHT);
                runsLayout.addView(layout);
            }


            AlertDialog.Builder playerGoneOut;
            playerGoneOut = new AlertDialog.Builder(this).setTitle("Computer went out!")
                    .setMessage("Books and runs made:")
                    .setView(scrollView)
                    .setIcon(R.drawable.computer)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // todo
                        }
                    });
            playerGoneOut.show();


        }

        enableEverything(false);
        enableButton(findViewById(R.id.drawButton), true);
        enableButton(findViewById(R.id.helpButton), true);
        enableButton(findViewById(R.id.saveButton), true);

        // Make draw button green so draw card isn't reset by disabling/redrawing everything to continue loop
        makeButtonGreen((Button)findViewById(R.id.drawButton), true);

        // Set human's textview to active, computer's to inactive
        TextView humanPlayerTextView = findViewById(R.id.humanTextView);
        humanPlayerTextView.setBackgroundColor(Color.GREEN);
        TextView computerPlayerTextView = findViewById(R.id.computerTextView);
        computerPlayerTextView.setBackgroundColor(Color.TRANSPARENT);


        // If human player went out in the previous turn, the round is over!
        final LinearLayout layout = cardsRemainingToView();
        if (roundModel.playerList[1].getPlayStatus() == PlayerModel.PlayStatus.GoneOut) {
            AlertDialog roundOver = new AlertDialog.Builder(this).setTitle("Round is over!")
//                    .setMessage("yes it is!")
                    .setView(layout)
                    .setIcon(R.drawable.arrow)
                    .setCancelable(false)
                    .setPositiveButton("Next round", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addScoreHistory();
                            nextRound();
                        }
                    })
                    .show();
        }

    }

    /**
     * Purpose: Start a new game of Five Crowns
     */
    private void startGame() {
        Intent intent = getIntent();
        Integer first = intent.getIntExtra(firstPlayerExtra, 0);
        Log.d("firstPlayer", first.toString());

        // Get a round
        gameModel = new GameModel(first);
        roundModel = gameModel.getRound(false);
        initializeGameScreen(first == 1);
    }

    /**
     * Purpose: Load in a saved game of Five Crowns
     */
    private void loadGame() throws IOException {
        Intent intent = getIntent();
        String fileName = intent.getStringExtra(fileNameExtra);
        Boolean fileFromAssets = intent.getBooleanExtra(fileFromAssetsExtra, false);

        // Parse the file
        GameFileParser gameFileParser;
        if (fileFromAssets) {
            // If the file is from /assets directory
            gameFileParser = new GameFileParser(getApplicationContext(), fileName, true);

        } else {
            // If the file is from data/data/app_name
            gameFileParser = new GameFileParser(getApplicationContext(), fileName, false);
        }
        gameFileParser.parseFile();
//        gameFileParser.printEverything();

        // Get and start a round
        gameModel = new GameModel(gameFileParser.getRoundNumber(), gameFileParser.getComputerScore(),
                gameFileParser.getHumanScore(), gameFileParser.getComputerHand(), gameFileParser.getHumanHand(),
                gameFileParser.getNextPlayer());
        roundModel = gameModel.getRound(true);
        roundModel.setDiscardPile(gameFileParser.getDiscardPile());
        roundModel.setDrawPile(gameFileParser.getDrawPile());
        roundModel.setComputerHand(gameFileParser.getComputerHand());
        roundModel.setHumanHand(gameFileParser.getHumanHand());
        initializeGameScreen(gameFileParser.getNextPlayer() == 1);
        Log.d(this.getClass().toString(), fileName);

    }

    /**
     * Purpose: Start the next round of the game
     */
    private void nextRound() {
        // Game has ended
        if (gameModel.getRoundNumber() >= 11) {
            gameModel.updatePlayersScores();
            Intent intent = new Intent(this, EndScreenActivity.class);
            intent.putExtra("computerScore", roundModel.playerList[0].getScore());
            intent.putExtra("humanScore", roundModel.playerList[1].getScore());
            startActivity(intent);
            finishAffinity();
        } else {
            gameModel.updatePlayersScores();
        }

        // Set up next round
        gameModel.setRoundNumber(gameModel.getRoundNumber() + 1);
        gameModel.setCurrentPlayerIndex(roundModel.getCurrentPlayerIndex());
        gameModel.clearPlayerHands();
        roundModel = gameModel.getRound(false);
        roundModel.resetPlayerStatus();

        initializeGameScreen(roundModel.getCurrentPlayerIndex() % 2 == 1);
    }

    /**
     * Purpose: Instantiate RoundView and update what's displayed
     */
    private void initializeGameScreen(boolean humanFirstPlayer) {
        TextView currentRoundTextView = findViewById(R.id.currentRoundText);
        TextView computerScoreTextView = findViewById(R.id.computerScoreTextView);
        TextView humanScoreTextView = findViewById(R.id.humanScoreTextView);

        View helpButton = findViewById(R.id.helpButton);
        View goOutButton = findViewById(R.id.goOutButton);

        TextView currentWildTextView = findViewById(R.id.wildCardTextView);

        // Create view for what's being displayed on the screen
        roundView = new RoundView(roundModel, helpButton, goOutButton, currentRoundTextView, computerScoreTextView, humanScoreTextView, currentWildTextView);
        roundView.updateGameCards();
        enableEverything(false);

        // Allow buttons that human is allowed to tap on their turn
        if (humanFirstPlayer) {
            enableButton(findViewById(R.id.drawButton), true);
            enableButton(findViewById(R.id.helpButton), true);
            enableButton(findViewById(R.id.saveButton), true);

            // Make draw button green so user knows what they should do first
            makeButtonGreen((Button)findViewById(R.id.drawButton), true);

            // Highlight human's text green
            findViewById(R.id.humanTextView).setBackgroundColor(Color.GREEN);
        } else {
            enableButton(findViewById(R.id.nexTurnButton), true);
            enableButton(findViewById(R.id.saveButton), true);

            // Make next turn button green so user knows what they should do first
            makeButtonGreen((Button)findViewById(R.id.nexTurnButton), true);

            // Highlight computer's text green
            findViewById(R.id.computerTextView).setBackgroundColor(Color.GREEN);
        }
    }

    /**
     * Purpose: Make a LinearLayout given a combination
     * @param combination the combination to make the LinearLayout for
     * @return a LinearLayout, representing the combination
     */
    private LinearLayout combinationToLayout(CardVectorModel combination) {
        LinearLayout layout = new LinearLayout(this);
        TextView combo = new TextView(this);
        combo.setText(combination.toString());
        layout.addView(combo);

        return layout;
    }

    /**
     * Purpose: Highlight the button green to indicate it's active or clickable
     * @param button the button to make green
     * @param makeGreen whether to make it green, if false, color is reset
     */
    private void makeButtonGreen(Button button, boolean makeGreen) {
        if (makeGreen) {
            button.setBackgroundColor(Color.GREEN);
        } else {
            Button defaultButton = new Button(this);
            button.setBackground(defaultButton.getBackground());
        }
    }

    /**
     * Purpose: Create a LinearLayout for the text displayed after the round is over: the score and books/runs each player made
     * @return a LinearLayout, representing round score and cards remaining of players in round
     */
    private LinearLayout cardsRemainingToView() {
        roundModel.playerList[0].setRoundScore();
        roundModel.playerList[1].setRoundScore();

        // Players with their round score and cards remaining
        // Remaining cards for computer
        String text = "";
        text = "Computer:\n";
        text += "\t\t\tRound score:  " + roundModel.playerList[0].getRoundScore() + "\n";
        text += "\t\t\tRemaining cards:  " + roundModel.playerList[0].getRemainingCards().toString() + "\n";

        // Remaining cards for human
        text += "Human:\n";
        text += "\t\t\tRound score:  " + roundModel.playerList[1].getRoundScore() + "\n";
        text += "\t\t\tRemaining cards:  " + roundModel.playerList[1].getRemainingCards().toString() + "\n";

        TextView textView = new TextView(this);
        textView.setText(text);
        LinearLayout layout = new LinearLayout(this);
        layout.addView(textView);

        return layout;
    }

    /**
     * Purpose: Add round scores of both players to GameHistoryModel
     */
    private void addScoreHistory() {
//        gameModel.updatePlayersScores();
        GameHistoryModel gameHistoryModel = GameHistoryModel.getInstance();
        String roundScores = "";
        roundScores += "ROUND:  " + roundModel.getRoundNumber() + "\n";
        roundScores += "\t\tComputer:  " + roundModel.playerList[0].getScore() + "\n";
        roundScores += "\t\tHuman:  " + roundModel.playerList[1].getScore() + "\n";

        gameHistoryModel.addHistoryScore(roundScores);
    }

    /**
     * Purpose: Create and display SnackBar to show reasons for its actions
     */
    private void displayComputersTurnReasoning(View view) {
        GameHistoryModel gameHistoryModel = GameHistoryModel.getInstance();
        ArrayList<String> reasons = gameHistoryModel.getGameHistory();
        String computerReasoning = reasons.get(reasons.size() - 2) + " and " + reasons.get(reasons.size() - 1);
        final Snackbar computerDrawSnackBar = Snackbar.make(view, "?",  Snackbar.LENGTH_INDEFINITE);

        View snackbarView = computerDrawSnackBar.getView();
        TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setText(computerReasoning);
        snackTextView.setMaxLines(5);

        computerDrawSnackBar.setText(computerReasoning);
        computerDrawSnackBar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                computerDrawSnackBar.dismiss();
            }
        });
        computerDrawSnackBar.show();
    }


    /**
     * Purpose: Provide the Human player with help on what to do by saying what it would do
     * @param view the button that was tapped (helpButton)
     */
    public void helpOnClick(View view) {
        String computersHelp = "";

        switch (roundModel.playerList[1].getPlayStatus()) {
            case FailedGoneOut:
            case Begun:
            case Drawing:
                computersHelp = ComputerModel.help(roundModel.getHumanHand(), roundModel.getDiscardPile(), ComputerModel.Help.DrawHelp);
                break;
            case Drawn:
            case Discarding:
                computersHelp = ComputerModel.help(roundModel.getHumanHand(), roundModel.getDiscardPile(), ComputerModel.Help.DiscardHelp);
                break;
            case Discarded:
                computersHelp = ComputerModel.help(roundModel.getHumanHand(), roundModel.getDiscardPile(), ComputerModel.Help.GoOutHelp);
                break;
            case GoneOut:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + roundModel.playerList[1].getPlayStatus());
        }


        final Snackbar helpSnackbar = Snackbar.make(view, "?",  Snackbar.LENGTH_INDEFINITE);
        View snackBarView = helpSnackbar.getView();
        TextView snackTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setMaxLines(5);
        helpSnackbar.setText(computersHelp);
        helpSnackbar.setAction("Thanks", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                helpSnackbar.dismiss();
            }
        });        helpSnackbar.show();



    }

    /**
     * Purpose: Display the history of the game that includes what the Computer did/suggested and why
     * @param view the button that was tapped (historyButton)
     */
    public void historyOnClick(View view) {

        final ScrollView historyScrollView = new ScrollView(this);
        final LinearLayout historyLayout = new LinearLayout(this);
        historyLayout.setOrientation(LinearLayout.VERTICAL);
        historyScrollView.addView(historyLayout);
        GameHistoryModel gameHistoryModel = GameHistoryModel.getInstance();

        ArrayList<String> gameHistory = gameHistoryModel.getGameHistory();
        for (int i = 0; i < gameHistory.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(gameHistory.get(i));
            historyLayout.addView(textView);
        }

        AlertDialog history = new AlertDialog.Builder(this).setTitle("Game history")
                .setView(historyScrollView)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.history)
                .show();
    }

    /**
     * Purpose: Save the game when tapped
     * @param view the button that was tapped (saveButton)
     */
    public void saveOnClick(View view) {
        final EditText saveEditView;
        // Get perms
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, 1);

        AlertDialog saveGame = new AlertDialog.Builder(this).setTitle("Save Game")
                .setMessage("Enter name of save file:")
                .setView(saveEditView = new EditText(this))
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(saveEditView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permission is not granted
                        }
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(saveEditView.getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            // Permission has already been granted
                            // Save current game state to name of file
                            String fileName = saveEditView.getText().toString();
                            Log.d("file in roundactivty", roundModel.toString());

                            fileName = fileName + ".txt";
                            File file = new File(saveEditView.getContext().getFilesDir(),"");
                            if(!file.exists()){
                                file.mkdir();
                            }
                            try{
                                File writeFile = new File(file, fileName);
                                FileWriter writer = new FileWriter(writeFile);
                                writer.append(roundModel.toString());
                                writer.flush();
                                writer.close();

                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                    }
                })
                .setNeutralButton("save + quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(saveEditView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permission is not granted
                        }
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(saveEditView.getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            // Permission has already been granted
                            // Save current game state to name of file
                            String fileName = saveEditView.getText().toString();
                            Log.d("file in roundactivty", roundModel.toString());

                            fileName = fileName + ".txt";
                            File file = new File(saveEditView.getContext().getFilesDir(),"");
                            if(!file.exists()){
                                file.mkdir();
                            }

                            try{
                                File writeFile = new File(file, fileName);
                                FileWriter writer = new FileWriter(writeFile);
                                writer.append(roundModel.toString());
                                writer.flush();
                                writer.close();

                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.save_icon)
                .show();
    }


    /**
     * Purpose: Enable or disable every button on the screen
     * @param enable whether or not to enable everything
     */
    public void enableEverything(boolean enable) {
        enableLinearLayout(computerHandLayout, enable);
        enableLinearLayout(humanHandLayout, enable);
        enableLinearLayout(drawPileLayout, enable);
        enableLinearLayout(discardPileLayout, enable);

        // Reset buttons to original colors
        makeButtonGreen((Button)findViewById(R.id.nexTurnButton), false);
        makeButtonGreen((Button)findViewById(R.id.discardButton), false);
        makeButtonGreen((Button)findViewById(R.id.drawButton), false);
        makeButtonGreen((Button)findViewById(R.id.goOutButton), false);


        enableButtons(roundButtons, enable);
    }

    /**
     * Purpose: Enable or disable all the buttons on a linear layout
     * @param layout the Linear Layout to enable or disable
     * @param enable whether or not to enable the layout
     */
    public void enableLinearLayout(LinearLayout layout, boolean enable) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(enable);
        }
    }

    /**
     * Purpose: Enable or disable a list of buttons
     * @param views the Views to enable or disable
     * @param enable whether or not to enable the buttons
     */
    public void enableButtons(Vector<View> views, boolean enable) {
        for (int i = 0; i < views.size(); i++) {
            enableButton(views.elementAt(i), enable);
        }
    }

    /**
     * Purpose: Enable or disable a button
     * @param view the View to enable or disable
     * @param enable whether or not to enable the button
     */
    public void enableButton(View view, boolean enable) {
        view.setEnabled(enable);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}



