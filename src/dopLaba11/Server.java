package dopLaba11;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server extends Thread {
    private int PORT;
    private final static int BUFFER = 1024;

    private ServerSocket serverSocket;
    private String username = "server";
    private static Socket socket;
    private int port;

    public Server() throws IOException, InterruptedException {
        setPort();
        System.out.println("Waiting for a client...");
        socket = serverSocket.accept();
        System.out.println("client connected");
        SendThread sendThread = new SendThread(socket, username);
        ListenMessage listenMessage = new ListenMessage(socket, username);
        sendThread.start();
        listenMessage.start();
        sendThread.join();
        closeEverything();
        System.exit(0);
    }

    public void setPort(){
        while (true){
            Scanner scanner = new Scanner(System.in);
            try{
                System.out.println("Input servet port: ");
                port = scanner.nextInt();
                serverSocket = new ServerSocket(port);
                break;
            } catch (Exception e){
                System.out.println("You can't set this port. Choose another one.");
                continue;
            }
        }
    }

    public static void closeEverything() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        Server server = new Server();
    }
}