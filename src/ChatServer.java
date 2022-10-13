import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatServer extends Thread {
    private int PORT ;
    private final static int BUFFER = 1024;

    private DatagramSocket socket;
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;

    public ChatServer(int port) throws IOException {

        socket = new DatagramSocket(port);
        clientAddresses = new ArrayList();
        clientPorts = new ArrayList();
        PORT = port;
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        MessageSender s = new MessageSender(socket);
        Thread st = new Thread(s);
        st.start();
        while (true) {
            try {
                if(socket.isClosed()) {
                    break;
                }
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String content = new String(buf, 0, packet.getLength(), "UTF-8");
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();


                if (!clientAddresses.contains(clientAddress) && !clientPorts.contains(clientPort)) {
                    clientPorts.add(clientPort);
                    s.setPort(clientPort);
                    clientAddresses.add(clientAddress);
                }

                System.out.println(content);


            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }


    public static void main(String args[]) throws Exception {
        ChatServer s = new ChatServer(Integer.parseInt(args[1]));
        s.start();
    }
}