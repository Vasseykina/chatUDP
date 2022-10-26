package dopLaba11;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private Socket socket;
    private String username = "client";
    private InetAddress ip;
    private int port;

    public Client() throws InterruptedException, IOException {
        connect();
        System.out.println("Connected");

        SendThread sendThread = new SendThread(socket, username);
        ListenMessage listenMessage = new ListenMessage(socket, username);
        sendThread.start();
        listenMessage.start();

        sendThread.join();
        closeEverything();
        System.exit(0);
    }

    private void connect(){
        while (true){
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("Input server IP:");
                ip = InetAddress.getByName(scanner.nextLine());
                System.out.println("Input server port:");
                port = scanner.nextInt();
                socket = new Socket(ip, port);
                break;
            } catch (Exception e) {
                System.out.println("Wrong ip or port!");
                continue;
            }
        }

    }

    public void closeEverything() {

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
    }

}