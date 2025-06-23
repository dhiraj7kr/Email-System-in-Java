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
