package edu.ramapo.btunney.fivecrowns;

/*******************************************************
 Activity that will allow user to pick a save game file if any exist
 @author Brendan Tunney
 @since 11/12/2019
  ******************************************************** */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.File;
import java.io.IOException;


public class LoadGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        // Display all the saved game files as buttons
        try {
            displayAllFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose: Go back to main activity when pressed
     * @param view the back button
     */
    public void backButtonOnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    /**
     * Purpose: Display all the files within data/data/app_name/ as a button you can click
     * @throws IOException
     */
    private void displayAllFiles() throws IOException {
        AssetManager am = this.getAssets();
        String []assetFiles = am.list("");

        File folder = new File(this.getFilesDir(), "");
        File[] internalFiles = folder.listFiles();

        // Add a button for each file in data/data/app_name/
        LinearLayout layout = findViewById(R.id.loadGameLayout);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Files in /assets
        for (String file : assetFiles) {
            if (file.contains(".txt")) {
                layout.addView(createSaveFileButton(file, true));
            }
        }

        // Files in data/data/app_name
        for (File file : internalFiles) {
            if (file.getName().contains(".txt")) {
                layout.addView(createSaveFileButton(file.getName(), false));
            }
        }

    }

    /**
     * Purpose: Create a button given a file name
     *      Each button is given an OnClickListener which will send an intent containing
     *      the name of the file along with a boolean determining if it's being loaded (true)
     * @param fileName the name of the file to create a button for
     * @param fileFromAssets if the file is from the /assets folder or the data/data/app_name folder
     * @return a Button representing the file
     */
    private Button createSaveFileButton(final String fileName, final boolean fileFromAssets) {
        Button button = new Button(this);

        // Set text of button to name of file
        button.setText(fileName);

        /**
         * Purpose: OnClickListener for each game file
         * When a button (each save file) is pressed, it will pass the name of the file along
         * with the boolean true to determine that it is loading a game file.
         * An intent is sent to RoundActivity with this information.
         */
        View.OnClickListener buttonOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoundActivity.class);
                intent.putExtra(RoundActivity.loadedGameExtra, true);
                intent.putExtra(RoundActivity.fileNameExtra, ((Button) v).getText().toString());
                intent.putExtra(RoundActivity.fileFromAssetsExtra, fileFromAssets);

                startActivity(intent);
                finishAffinity();
            }
        };
        button.setOnClickListener(buttonOnClick);

        return button;
    }
}
