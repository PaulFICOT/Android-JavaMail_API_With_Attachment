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
 * Classe de création d'un menu afin d'organiser les différentes catégories de l'application
 *
 * @author Paul FICOT
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;

    /**
     * Affiche la vue correspondant à la méthode lors de sa création
     *
     * @param savedInstanceState Sauvegarde de l'état de l'instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = findViewById(R.id.mainGrid);

        //set Event
        setSingleEvent(mainGrid);

    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all children of mainGrid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int cardChoice = i;
            cardView.setOnClickListener(new View.OnClickListener() {

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

    public void onClickNoConnection (View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickSend(View view) {
    }

    public void onClickImage(View view) {
    }
}
