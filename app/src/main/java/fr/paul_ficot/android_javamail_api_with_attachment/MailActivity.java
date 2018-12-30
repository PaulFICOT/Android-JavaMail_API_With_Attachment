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
 *
 * @author Paul FICOT
 * @version 2.0
 */

public class MailActivity extends AppCompatActivity implements MediaUtils.GetImg {

    //Déclaration EditText
    private EditText subject;
    private EditText body;
    private EditText firstname;
    private EditText lastname;
    private EditText phone;

    //Initialisation des éléments d'ajout d'image
    TextView txtPath;
    MediaUtils mMediaUtils;
    private String TAG = MailActivity.class.getSimpleName();

    /**
     * Affiche la vue correspondant à la méthode lors de sa création
     *
     * @param savedInstanceState Sauvegarde de l'état de l'instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        // Initialisation des EditText de saisie d'informations
        subject = findViewById(R.id.subject_xml);
        body = findViewById(R.id.body_xml);
        firstname = findViewById(R.id.firstname_xml);
        lastname = findViewById(R.id.lastname_xml);
        phone = findViewById(R.id.phone_xml);

        // Initialisation MediaUtils
        mMediaUtils = new MediaUtils(this);

        //Initialisation du TextView affichant le path
        txtPath = findViewById(R.id.txtPath_xml);
    }

    /**
     * Envoies le mail avec les paramètres définis auparavant
     */
    private void sendEmailWithAttachment() {
        // Obtention du contenu du mail
        String subject_mail = subject.getText().toString().trim();
        String message = "Nom / Prénom : " + firstname.getText().toString().trim() + " " + lastname.getText().toString().trim() + "\n" +"Téléphone : " + phone.getText().toString().trim() + "\n" + "\n" + "Description du problème : " + "\n" + body.getText().toString().trim() + "\n" + "\n" + "Cordialement, " +  firstname.getText().toString().trim() + " " + lastname.getText().toString().trim();
        //Création de l'objet SendMail
        SendMailWithAttachment sendMailWithAttachment = new SendMailWithAttachment(this, subject_mail, message);

        //Exécuter sendEmail pour envoyer un mail
        sendMailWithAttachment.execute();
    }

    /**
     * Envoies le mail avec les paramètres définis auparavant
     */
    private void sendEmailNoAttachment() {
        // Obtention du contenu du mail
        String subject_mail = subject.getText().toString().trim();
        String message = "Nom / Prénom : " + firstname.getText().toString().trim() + " " + lastname.getText().toString().trim() + "\n" +"Téléphone : " + phone.getText().toString().trim() + "\n" + "\n" + "Description du problème : " + "\n" + body.getText().toString().trim() + "\n" + "\n" + "Cordialement, " +  firstname.getText().toString().trim() + " " + lastname.getText().toString().trim();
        //Création de l'objet SendMail
        SendMailNoAttachment sendMailNoAttachment = new SendMailNoAttachment(this, subject_mail, message);

        //Exécuter sendEmail pour envoyer un mail
        sendMailNoAttachment.execute();
    }

    /**
     * Exécuter la fonction sendEmail lorsque l'on clique sur le bouton
     *
     * @param view Vue après l'envoie
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
     * Exécuter la fonction openFolder lorsque l'on clique sur le bouton
     *
     * @param view vue actuelle
     */
    public void onClickImage(View view) {

        mMediaUtils.openImageDialog();
    }

    /**
     * Actions à effectuer en fonction de la réponse aux requêtes de permissions
     *
     * @param requestCode Code de vérification de la requête
     * @param permissions Permissions demandés
     * @param grantResults Résultat de la demande de permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMediaUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Actions à effectuer en fonction de l'image selectionnée
     *
     * @param requestCode Code de vérification de la requête
     * @param resultCode Code de vérification du résultat
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMediaUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Interface de GetImg de la classe MediaUtils
     *
     * @param imgPath chemin vers l'image selectionnée
     */
    @Override
    public void imgdata(String imgPath) {
        Log.d(TAG, "imgdata: " + imgPath);
        Config.ATTACHMENT_PATH = imgPath;
        txtPath.setText(Config.ATTACHMENT_PATH);
    }

    /**
     * Ouvre le site web via un navigateur lorsque l'on clique sur "VILLE DE RAISMES" SI l'appareil est connecté à internet
     *
     * @param view Vue actuelle
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
