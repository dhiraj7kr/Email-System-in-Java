package client;

import java.util.Properties;
import javax.mail.*;

public class EmailReceiver {
    public static void receive() {
        final String user = "your_email@example.com";
        final String password = "your_password";

        Properties props = new Properties();
        props.put("mail.pop3.host", "pop.example.com");
        props.put("mail.pop3.port", "110");
        props.put("mail.pop3.starttls.enable", "true");

        Session session = Session.getInstance(props);

        try {
            Store store = session.getStore("pop3");
            store.connect("pop.example.com", user, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            System.out.println("Received emails:");
            for (int i = 0; i < messages.length; i++) {
                System.out.println("----------------------------------");
                System.out.println("Subject: " + messages[i].getSubject());
                System.out.println("From: " + messages[i].getFrom()[0]);
                System.out.println("Text: " + messages[i].getContent().toString());
            }

            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
