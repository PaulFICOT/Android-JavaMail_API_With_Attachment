package fr.paul_ficot.android_javamail_api_with_attachment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailNoAttachment extends AsyncTask<Void,Void,Void> {


    //Déclaration des variables
    @SuppressLint("StaticFieldLeak")
    private Context context_mail;

    //Informations pour envoyer le mail
    private String subject_mail;
    private String message_mail;

    //Progressdialog à afficher lors de l'envoi du mail
    private ProgressDialog progressDialog;

    /**
     * Constructeur de la classe SendMail
     *
     * @param context Context
     * @param subject Corps du mail
     * @param message Titre/Objet du mail
     */
    SendMailNoAttachment(Context context, String subject, String message){
        //Initializing variables
        this.context_mail = context;
        this.subject_mail = subject;
        this.message_mail = message;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Affichage de la progression lors de l'envoi du mail
        progressDialog = ProgressDialog.show(context_mail,"Envoi en cours","Veuillez patienter...",false,false);
    }

    /**
     * Disparition du "Veuillez patienter..." et Affichage de "Message envoyé" une fois le mail envoyé
     *
     * @param aVoid paramètres
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Rejeter l'affichage de la progression
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context_mail,"Message envoyé",Toast.LENGTH_LONG).show();
    }

    /**
     * Actions réalisées en arrière-plan lorsque l'on appuie sur le bouton
     *
     * @param params Paramètres de connexion au SMTP
     * @return null
     */
    @Override
    protected Void doInBackground(Void... params) {
        //Création des propriétés
        Properties props = new Properties();

        //Configuration des propriétés pour GMail
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Création d'une nouvelle session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authentification du mot de passe

                    /**
                     * Connexion à l'adresse mail
                     *
                     * @return informations de connexion
                     */
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.MAIL_SENDER, Config.MAIL_SENDER_PASSWORD);
                    }
                });

        try {
            //Création de l'objet mimeMessage
            MimeMessage mimeMessage_mail = new MimeMessage(session);

            //Création de l'objet mimeMultipart
            MimeMultipart mimeMultipart_mail = new MimeMultipart();

            //Création de l'objet mimeBodyPart pour le message texte
            MimeBodyPart messageBodyPart_mail = new MimeBodyPart();

            //Ajout du message
            messageBodyPart_mail.setContent(message_mail, "text/plain; charset=UTF-8");

            //Ajout du BodyPart pour le message texte
            mimeMultipart_mail.addBodyPart(messageBodyPart_mail);

            //Définition de l'adresse de l'expéditeur
            mimeMessage_mail.setFrom(new InternetAddress(Config.MAIL_SENDER));

            //Ajout du destinataire
            mimeMessage_mail.addRecipient(Message.RecipientType.TO, new InternetAddress(Config.MAIL_RECEIVER));

            //Ajout de l'objet du mail
            mimeMessage_mail.setSubject(subject_mail);

            //Rassemblement des infos dans le multipart
            mimeMessage_mail.setContent(mimeMultipart_mail);

            //Envoi du mail
            Transport.send(mimeMessage_mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
