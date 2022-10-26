package dopLaba11;

import java.io.*;
import java.net.Socket;

public class ListenMessage extends Thread{

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "Anonymous_User";
    private Socket socket;

    public ListenMessage(Socket socket, String username){
        this.socket = socket;
        this.username = username;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void run(){
        String message;
        while (socket.isConnected()) {
            try {
                message = bufferedReader.readLine();
                if(message != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                break;
            }
        }
    }


}
