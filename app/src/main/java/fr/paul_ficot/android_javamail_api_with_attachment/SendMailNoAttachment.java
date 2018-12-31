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

/**
 * Send a mail without attachment
 * The class expands AsyncTask, it will perform a networking operation.
 *
 * @author Paul FICOT
 * @version 2.1
 */
public class SendMailNoAttachment extends AsyncTask<Void,Void,Void> {


    //Variable declaration
    @SuppressLint("StaticFieldLeak")
    private Context context_mail;

    //Information to send the mail
    private String subject_mail;
    private String message_mail;

    //Progressdialog to display when sending the mail
    private ProgressDialog progressDialog;

    /**
     * SendMail class constructor
     *
     * @param context Context
     * @param subject Body of the mail
     * @param message Mail object
     */
    SendMailNoAttachment(Context context, String subject, String message){
        //Initializing variables
        this.context_mail = context;
        this.subject_mail = subject;
        this.message_mail = message;

    }

    /**
     * Displaying an animation "Please wait ..." when clicking on the "Send" button
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress when sending the mail
        progressDialog = ProgressDialog.show(context_mail,"Sending","Please wait...",false,false);
    }

    /**
     * Disappearance of "Please wait ..." and "Message sent" display after the mail is sent
     *
     * @param aVoid Settings
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss the progress display
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context_mail,"Message sent",Toast.LENGTH_LONG).show();
    }

    /**
     * Actions performed in the background when the button is pressed
     *
     * @param params SMTP connection settings
     * @return null
     */
    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for GMail
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Password Authentication

                    /**
                     * Login to the email address
                     *
                     * @return login information
                     */
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.MAIL_SENDER, Config.MAIL_SENDER_PASSWORD);
                    }
                });

        try {
            //Creating the mimeMessage object
            MimeMessage mimeMessage_mail = new MimeMessage(session);

            //Creating the mimeMultipart object
            MimeMultipart mimeMultipart_mail = new MimeMultipart();

            //Creating the mimeBodyPart object for the text message
            MimeBodyPart messageBodyPart_mail = new MimeBodyPart();

            //Adding the message
            messageBodyPart_mail.setContent(message_mail, "text/plain; charset=UTF-8");

            //Adding the BodyPart for the text message
            mimeMultipart_mail.addBodyPart(messageBodyPart_mail);

            //Setting the sender's address
            mimeMessage_mail.setFrom(new InternetAddress(Config.MAIL_SENDER));

            //Adding the email receiver
            mimeMessage_mail.addRecipient(Message.RecipientType.TO, new InternetAddress(Config.MAIL_RECEIVER));

            //Adding the subject of the email
            mimeMessage_mail.setSubject(subject_mail);

            //Gathering info in the multipart
            mimeMessage_mail.setContent(mimeMultipart_mail);

            //Sending the mail
            Transport.send(mimeMessage_mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
