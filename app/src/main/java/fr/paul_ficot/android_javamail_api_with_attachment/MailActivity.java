package fr.paul_ficot.android_javamail_api_with_attachment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static fr.paul_ficot.android_javamail_api_with_attachment.MainActivity.isNetworkStatusAvailable;

/**
 * Class of sending of mail via SMTP GMail
 *
 * @author Paul FICOT
 * @version 2.0
 */

public class MailActivity extends AppCompatActivity implements MediaUtils.GetImg {

    //Declaration EditText
    private EditText subject;
    private EditText body;
    private EditText firstname;
    private EditText lastname;
    private EditText phone;

    //Initializing image addition elements
    TextView txtPath;
    MediaUtils mMediaUtils;
    private String TAG = MailActivity.class.getSimpleName();

    /**
     * Displays the view corresponding to the method when it was created
     *
     * @param savedInstanceState Backing up the state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        //Initialize EditText to enter information
        subject = findViewById(R.id.subject_xml);
        body = findViewById(R.id.body_xml);
        firstname = findViewById(R.id.firstname_xml);
        lastname = findViewById(R.id.lastname_xml);
        phone = findViewById(R.id.phone_xml);

        //MediaUtils initialization
        mMediaUtils = new MediaUtils(this);

        //Initializing the TextView displaying the path
        txtPath = findViewById(R.id.txtPath_xml);
    }

    /**
     * Send the mail with the parameters defined before
     */
    private void sendEmailWithAttachment() {
        //Obtaining the contents of the email
        String subject_mail = subject.getText().toString().trim();
        String message = "Nom / Prénom : " + firstname.getText().toString().trim() + " " + lastname.getText().toString().trim() + "\n" +"Téléphone : " + phone.getText().toString().trim() + "\n" + "\n" + "Description du problème : " + "\n" + body.getText().toString().trim() + "\n" + "\n" + "Cordialement, " +  firstname.getText().toString().trim() + " " + lastname.getText().toString().trim();
        //Creating the SendMail object
        SendMailWithAttachment sendMailWithAttachment = new SendMailWithAttachment(this, subject_mail, message);

        //Run sendMail to send an email
        sendMailWithAttachment.execute();
    }

    /**
     * Send the mail with the parameters defined before
     */
    private void sendEmailNoAttachment() {
        //Obtaining the contents of the email
        String subject_mail = subject.getText().toString().trim();
        String message = "Nom / Prénom : " + firstname.getText().toString().trim() + " " + lastname.getText().toString().trim() + "\n" +"Téléphone : " + phone.getText().toString().trim() + "\n" + "\n" + "Description du problème : " + "\n" + body.getText().toString().trim() + "\n" + "\n" + "Cordialement, " +  firstname.getText().toString().trim() + " " + lastname.getText().toString().trim();
        //Creating the SendMail object
        SendMailNoAttachment sendMailNoAttachment = new SendMailNoAttachment(this, subject_mail, message);

        //Run sendMail to send an email
        sendMailNoAttachment.execute();
    }

    /**
     * Run the sendMail function when the button is clicked
     *
     * @param view current view
     */
    public void onClickSend(View view) {

        if (!Config.ATTACHMENT_PATH.equals(""))
        {
            sendEmailWithAttachment();

        }
        else {
            sendEmailNoAttachment();
        }

        subject.setText("");
        body.setText("");
        firstname.setText("");
        lastname.setText("");
        phone.setText("");
        txtPath.setText("");

        Config.ATTACHMENT_PATH = "";
    }

    /**
     * Run the openImageDialog method when the button is clicked
     *
     * @param view current view
     */
    public void onClickImage(View view) {

        mMediaUtils.openImageDialog();
    }

    /**
     * Actions to be performed based on response to permission requests
     *
     * @param requestCode Request verification code
     * @param permissions Requested permissions
     * @param grantResults Result of the request for permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMediaUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Actions to be performed according to the selected image
     *
     * @param requestCode Request verification code
     * @param resultCode Result verification code
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMediaUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * GetImg interface of the MediaUtils class
     *
     * @param imgPath path to the selected image
     */
    @Override
    public void imgdata(String imgPath) {
        Log.d(TAG, "imgdata: " + imgPath);
        Config.ATTACHMENT_PATH = imgPath;
        txtPath.setText(Config.ATTACHMENT_PATH);
    }

    /**
     * Opens the website via a browser when you click on "Paul FICOT" IF the device is connected to the internet
     *
     * @param view current view
     */
    public void openBrowserInternetCheck(View view) {

        String link_Me = Config.LINK_ME;

        if (isNetworkStatusAvailable(getApplicationContext()))
        {
            Intent raismes_internet_intent = new Intent();
            raismes_internet_intent.setAction(Intent.ACTION_VIEW);
            raismes_internet_intent.addCategory(Intent.CATEGORY_BROWSABLE);

            raismes_internet_intent.setData(Uri.parse(link_Me));
            startActivity(raismes_internet_intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Internet is unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
