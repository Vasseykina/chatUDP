package dopLaba11;

import java.io.*;
import java.net.Socket;
import java.net.SocketOption;
import java.util.Scanner;

public class ChatClient implements Runnable{


    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "Anonymous_User";

    public ChatClient(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }



    public void run() {
            while (socket.isConnected()) {
                Scanner scanner = new Scanner(System.in);
                String readString = " ";
                while (true) {
                    try {
                        while (readString != null) {
                            while (readString.isEmpty()) {
                                Thread.sleep(100);
                            }
                            if (scanner.hasNextLine()) {
                                readString = scanner.nextLine();
                                if (readString.startsWith("@name")) {
                                    username = readString.substring(6);
                                    continue;
                                }
                                if (readString.equals("@quit")) {
                                    bufferedWriter.write(username + "<left chat>");
                                    username = "Anonymous_User";
                                    socket.close();
                                    return ;
                                } else {
                                    readString = scanner.nextLine();
                                    bufferedWriter.write(username + ":" + readString);
                                    bufferedWriter.newLine();
                                }

                            } else {
                                readString = null;
                            }
                        }

                    } catch (Exception e) {
                        break;
                    }

                }
            }
//        } catch (IOException e) {
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
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

    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your name:");
//        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 50007);
        ChatClient client = new ChatClient(socket);
        client.listenForMessage();
        Thread cl = new Thread(client);
    }

}