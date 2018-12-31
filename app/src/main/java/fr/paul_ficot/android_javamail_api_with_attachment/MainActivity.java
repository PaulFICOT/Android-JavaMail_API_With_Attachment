package fr.paul_ficot.android_javamail_api_with_attachment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

/**
 * MailSender using JavaMail API and GMail SMTP with image attachment
 *
 * @author Paul FICOT
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    
    GridLayout mainGrid;

    /**
     * Displays the view corresponding to the method when it was created
     *
     * @param savedInstanceState Backing up the state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = findViewById(R.id.mainGrid);

        //set Event
        setSingleEvent(mainGrid);

    }

    /**
     * Creates a CardView for each "child" of mainGrid
     *
     * @param mainGrid Grid layout from the XML file linked to this Java file
     */
    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all children of mainGrid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int cardChoice = i;
            cardView.setOnClickListener(new View.OnClickListener() {

                /**
                 * Execute the activity corresponding to the cardChoice value
                 *
                 * @param view current view
                 */
                @Override
                public void onClick(View view) {
                    if (cardChoice == 0){
                            if(isNetworkStatusAvailable (getApplicationContext())) {
                                Intent intent = new Intent(MainActivity.this, MailActivity.class);
                                startActivity(intent);
                                }

                                else {
                                setContentView(R.layout.no_connection);
                            }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error, contact the admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            );
        }
    }

    /**
     * Opens the website via a browser when you click on "Paul FICOT" IF the device is connected to the internet
     *
     * @param view current view
     */
    public void openBrowserInternetCheck(View view) {

        String url = (String) view.getTag();

        if (isNetworkStatusAvailable(getApplicationContext()))
        {
            Intent internet_intent = new Intent();
            internet_intent.setAction(Intent.ACTION_VIEW);
            internet_intent.addCategory(Intent.CATEGORY_BROWSABLE);

            internet_intent.setData(Uri.parse(url));
            startActivity(internet_intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Internet is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Detects if the device is connected to the internet
     *
     * @param context context
     * @return state of the internet connection
     */
    public static boolean isNetworkStatusAvailable ( Context context ){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService ( Context.CONNECTIVITY_SERVICE );
        if ( connectivityManager != null )
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if ( netInfos != null )
                return netInfos.isConnected();
        }
        return false;
    }

    /**
     * Returns to the MainActivity activity when the device is not connected to the internet
     *
     * @param view current view
     */
    public void onClickNoConnection (View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Allow to send the mail
     *
     * @param view current view
     */
    public void onClickSend(View view) {
    }

    /**
     * Allow to add an image as an attachment in the mail
     *
     * @param view current view
     */
    public void onClickImage(View view) {
    }
}
