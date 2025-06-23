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
