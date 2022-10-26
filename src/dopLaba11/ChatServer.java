package dopLaba11;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChatServer extends Thread {
    private int PORT ;
    private final static int BUFFER = 1024;

    private ServerSocket serverSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username="Anonymous_User";
    private Socket socket;

    public ChatServer(int port, String username) throws IOException {

        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.username = username;

    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
               /* if (messageToSend.startsWith("@sendUser"))
                    bufferedWriter.write(messageToSend);
                else*/
                bufferedWriter.write(username + ":" + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name:");
        String username = scanner.nextLine();
        ChatServer chatServer = new ChatServer(50007, username);
        chatServer.listenForMessage();
        chatServer.sendMessage();

    }
}