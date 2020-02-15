package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity created on app load
 @author Brendan Tunney
 @since 11/12/2019
  ******************************************************** */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }



}
