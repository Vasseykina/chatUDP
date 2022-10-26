package dopLaba11;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread {
    private BufferedWriter bufferedWriter;
    private String username;
    private Socket socket;

    public SendThread(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (makeMessage());
    }

    private boolean makeMessage() {
        try {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message.startsWith("@name ")) {
                String notification = username;
                username = message.substring(6);
                notification = notification + " changed his/her name to " + username;
                sendMessage(notification);
                return true;
            } else if (message.equals("@quit")) {
                String notification = username + " was disconnected";
                sendMessage(notification);
                return false;
            } else {
                message = username + ":" + message;
                sendMessage(message);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

}
