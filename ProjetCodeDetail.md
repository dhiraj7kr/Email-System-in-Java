Below is a complete step-by-step Java Email System project structure, including folder names, file names, code snippets, and detailed explanations.

---

## 📁 Project Name: `JavaEmailSystem`

### 🌐 Purpose

A secure, advanced email client/server system in Java using SMTP and POP3 protocols via the JavaMail API.

---

## 🔖 Step-by-Step Instructions & Code Structure

---

### ✅ **Step 1: Project Setup**

**Technologies Required:**

* Java SE (8+)
* JavaMail API
* JavaFX/Swing (for GUI)
* Maven or Gradle (for dependency management)

---

### 📁 `JavaEmailSystem/`

```plaintext
JavaEmailSystem/
├── lib/
├── src/
│   ├── client/
│   ├── server/
│   ├── model/
│   ├── utils/
├── resources/
├── README.md
├── pom.xml (if using Maven)
```

---

## ✉️ PART 1: Sending Email (SMTP)

### 📁 `src/client/EmailSender.java`

**📄 File: `EmailSender.java`**

```java
package client;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    public static void send(String to, String subject, String content) {
        final String from = "your_email@example.com";
        final String password = "your_password";

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com"); // replace with your SMTP server
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        // Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Compose email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 📥 PART 2: Receiving Email (POP3)

### 📁 `src/client/EmailReceiver.java`

**📄 File: `EmailReceiver.java`**

```java
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
```

---

## 🧑‍💻 PART 3: Simple Mail Server Simulation

### 📁 `src/server/MailServer.java`

**📄 File: `MailServer.java`**

```java
package server;

import java.net.ServerSocket;
import java.net.Socket;

public class MailServer {
    public static void main(String[] args) {
        int port = 2525;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mail server running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new SMTPHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 📁 `src/server/SMTPHandler.java`

**📄 File: `SMTPHandler.java`**

```java
package server;

import java.io.*;
import java.net.Socket;

public class SMTPHandler implements Runnable {
    private Socket socket;

    public SMTPHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("220 Welcome to Java Mail Server");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (inputLine.startsWith("HELO")) {
                    out.println("250 Hello");
                } else if (inputLine.startsWith("MAIL FROM:")) {
                    out.println("250 OK");
                } else if (inputLine.startsWith("RCPT TO:")) {
                    out.println("250 OK");
                } else if (inputLine.startsWith("DATA")) {
                    out.println("354 End with <CR><LF>.<CR><LF>");
                    while (!(inputLine = in.readLine()).equals(".")) {
                        System.out.println("Message: " + inputLine);
                    }
                    out.println("250 OK: Message accepted");
                } else if (inputLine.startsWith("QUIT")) {
                    out.println("221 Bye");
                    break;
                } else {
                    out.println("500 Unrecognized command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 📦 Additional Files

### 📁 `src/model/Email.java`

**📄 File: `Email.java`** (POJO class)

```java
package model;

public class Email {
    private String from;
    private String to;
    private String subject;
    private String content;

    // Constructors, getters, and setters
}
```

---

### 📁 `src/utils/Encryptor.java`

**📄 File: `Encryptor.java`** (Optional encryption)

```java
package utils;

import java.security.MessageDigest;

public class Encryptor {
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
```

---

### 📄 `pom.xml` (for Maven)

```xml
<dependencies>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>javax.mail</artifactId>
        <version>1.6.2</version>
    </dependency>
</dependencies>
```

---

## 🏃 How to Run (Step-by-Step)

1. Install Java and Maven.
2. Create project structure as shown.
3. Add dependencies (`javax.mail`) to `pom.xml`.
4. Replace email credentials and server details.
5. Run `MailServer.java` to simulate server.
6. Run `EmailSender.java` to send emails.
7. Run `EmailReceiver.java` to fetch inbox.

---

## 💡 Optional Enhancements

* Use JavaFX for GUI: Inbox, Compose, Sent
* Use a real SMTP/POP3 provider (Gmail, Outlook)
* Save received emails in local DB (H2 or SQLite)
* Add file attachments using `MimeBodyPart`

---

Would you like me to zip the starter files for you or provide the JavaFX GUI on top of this system?
