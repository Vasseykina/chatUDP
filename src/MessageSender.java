import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class MessageSender implements Runnable {
    private int port;
    private DatagramSocket sock;
    private String name = "Anonymous_User";

    MessageSender(DatagramSocket s, int p) {
        sock = s;
        port = p;
    }

    MessageSender (DatagramSocket s) {
        sock = s;
    }
    private void sendMessage(String s) throws Exception {
        byte buf[] = (name + " : " + s).getBytes();
        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        sock.send(packet);
    }

    public void setPort(int p) {
        port = p;
    }

    public void run() {
        boolean connected = false;
        do {
            try {
                sendMessage("Welcome! You can choose some option: \n 1. @name <Your name> \n 2. <send message> \n 3. @quit \n ");
                connected = true;
            } catch (Exception e) {

            }
        } while (!connected);
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
                            name = readString.substring(6);
                            continue;
                        }
                        if (readString.equals("@quit")) {
                            sendMessage(" <left chat> ");
                            name = "Anonymous_User";
                            sock.close();
                            return ;
                        }
                        else {
                            sendMessage(readString);
                        }

                    } else {
                        readString = null;
                    }
                }

            } catch (Exception e) {
//                break;
              System.err.println(e);
            }
        }
    }
}
